package com.example.thedonsdarling.util.game.gamerules.gameserver

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.thedonsdarling.R
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.dbGame
import com.example.thedonsdarling.domain.Deck
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.LogMessage
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.util.game.gamerules.GameRules
import com.example.thedonsdarling.util.user.HandleUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StartGame() {

    companion object {
        val currentUser = Firebase.auth.currentUser
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
                start = false,
                host = currentUser.uid,
                roundOver = false,
                gameOver = false,
                showLogs = true,
                deleteRoom = false,
                deckClear = false,
                gameLog = arrayListOf()
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
            onSuccess: () -> Unit,
        ) {
            val logMessage = LogMessage.createLogMessage(
                context.getString(R.string.start_game_message),
                toastMessage = null,
                null,
                "serverMessage"
            )
            var updatedGameRoom = gameRoom
            updatedGameRoom.gameLog.add(logMessage)
            val size = gameRoom.players.size -1
            val turn = (0..size).shuffled().random()
            gameRoom.players = GameRules.assignTurns(gameRoom.players, gameTurn = turn)
            gameRoom.turn = turn

            gameRoom.start = true

            updatedGameRoom = GameRules.dealCards(gameRoom)

            updatedGameRoom.players.forEach {
                HandleUser.addGameToPlayer(it.uid, gameRoom = updatedGameRoom)
            }

            dbGame.document(gameRoom.roomCode).set(updatedGameRoom)
                .addOnSuccessListener {
                    Log.d(TAG, "The game has started")
                    Toast.makeText(context, context.getString(R.string.start_game_announcement), Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to make game: ${it.localizedMessage}")
                }

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

    }


}
