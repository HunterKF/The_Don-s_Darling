package com.example.loveletter.util.startgame

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.loveletter.TAG
import com.example.loveletter.dbGame
import com.example.loveletter.domain.Deck
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.util.user.HandleUser
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
            context: Context,
            onSuccess: () -> Unit
        ) {
            var updatedGameRoom = gameRoom
            gameRoom.players = assignTurns(gameRoom.players)
            /*OPTIONS: Make a random list, and pass it to 2 different functions to adjust the deck and the players.
            * or...
            * Make one function that returns a game room...*/

            gameRoom.start = true

            updatedGameRoom = dealCards(gameRoom)

            updatedGameRoom.players.forEach {
                HandleUser.updateJoinedGame(it.uid, gameRoom = updatedGameRoom)
            }

            dbGame.document(gameRoom.roomCode).set(updatedGameRoom)
                .addOnSuccessListener {
                    Log.d(TAG, "The game has started")
                    Toast.makeText(context, "Game started!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to make game: ${it.localizedMessage}")
                }

        }

        private fun assignTurns(list: List<Player>): List<Player> {
            val turn = mutableStateOf(1)
            list.forEach {
                it.turnOrder = turn.value
                if (it.turnOrder == 1) {
                    it.turn = true
                }
                turn.value = turn.value +1
            }
            return list
        }

        private fun dealCards(gameRoom: GameRoom): GameRoom {

            gameRoom.players.forEach { player->
                val size = gameRoom.deck.deck.size
                if (player.turn) { player
                    val randomCard = listOf(randomNumber(size), randomNumber(size))
                    randomCard.forEach {
                        player.hand.add(gameRoom.deck.deck[it])
                        gameRoom.deck.deck.remove(it)
                    }
                } else {
                    val randomCard = randomNumber(size)
                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.remove(randomCard)
                }

            }
            return gameRoom
        }

        fun randomNumber(size: Int): Int {
            return (1..size).shuffled().random()
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
