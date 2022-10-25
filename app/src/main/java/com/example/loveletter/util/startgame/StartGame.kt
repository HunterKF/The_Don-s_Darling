package com.example.loveletter.util.startgame

import com.example.loveletter.domain.Deck
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StartGame() {

    companion object {
        private val db = Firebase.firestore
        fun createCodedRoom(roomCode: String) {
            val deck = Deck()
            val gameRoom = GameRoom(
                deck = deck,
                turn = 0,
                roomNickname = "",
                playLimit = 5,
                players = listOf(Player(0, "", "")),
                roomCode = roomCode
            )
            db.collection("game").document(roomCode)
                .set(gameRoom)
                .addOnSuccessListener {
                    println("Success!")
                }
                .addOnFailureListener {
                    println("Failure...")
                }
        }

        fun createRoom(
            roomNickname: String,
            playLimit: Int,
            players: List<Player>,
            roomCode: String,
        ) = CoroutineScope(Dispatchers.IO).launch {
            val deck = Deck()
            val gameRoom = GameRoom(
                deck = deck,
                turn = 0,
                roomNickname = roomNickname,
                playLimit = playLimit,
                players = players,
                roomCode = roomCode
            )
            try {
                db.collection("game").document(roomCode)
                    .set(gameRoom).await()
            } catch (e: Exception) {
                println(e.localizedMessage)
            }

        }

        suspend fun subscribeToRealtimeUpdates(roomCode: String): Flow<GameRoom> {
            return callbackFlow {
                var room = GameRoom()
                val listener = db.collection("game").document(roomCode)
                    .addSnapshotListener { querySnapshot, exception ->
                        exception?.let {
                            println(exception.localizedMessage)
                            return@addSnapshotListener
                        }
                        querySnapshot?.let {
                            val updatedRoom = it.toObject(GameRoom::class.java)
                            updatedRoom?.let {
                                room = updatedRoom
                            }

                        }
                        trySend(room)
                    }
                awaitClose {
                    listener.remove()
                }
            }

        }

        fun deleteRoom(roomCode: String) {
            db.collection("game").document(roomCode)
                .get()
                .addOnSuccessListener { result ->
                    result.reference.delete()
                }
                .addOnFailureListener {
                    println("Failure...")
                }
        }

        fun getRandomString(): String {
            val allowedChars = ('A'..'Z') + ('0'..'9')
            return (1..4)
                .map { allowedChars.shuffled().random() }
                .joinToString("")
        }

        suspend fun updateRoom(roomCode: String, roomNickname: String) = coroutineScope {
            db.collection("game").document(roomCode).update("roomNickname", roomNickname)
        }

        fun createPlayers(players: List<Player>) {

        }
    }


}
