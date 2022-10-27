package com.example.loveletter.util.startgame

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.loveletter.TAG
import com.example.loveletter.dbGame
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
                roomCode = roomCode,
                start = false
            )
            try {
                dbGame.document(roomCode)
                    .set(gameRoom).await()
            } catch (e: Exception) {
                println(e.localizedMessage)
            }

        }

        fun startGame(
            gameRoom: GameRoom,
            context: Context
        ) {
            dbGame.document(gameRoom.roomCode).update("start", true)
                .addOnSuccessListener {
                    Log.d(TAG, "The game has started")
                    Toast.makeText(context, "Game started!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to make game: ${it.localizedMessage}")
                }

        }

        fun startGame(roomCode: String) {
            dbGame.document(roomCode).update("start", true)
        }

        suspend fun subscribeToRealtimeUpdates(roomCode: String): Flow<GameRoom> {
            return callbackFlow {
                var room = GameRoom()
                val listener = dbGame.document(roomCode)
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
            dbGame.document(roomCode)
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

    }


}
