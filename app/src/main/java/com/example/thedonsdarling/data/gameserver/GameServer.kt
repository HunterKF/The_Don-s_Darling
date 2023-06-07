package com.example.thedonsdarling.util.game

import android.util.Log
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.dbGame
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.LogMessage
import com.example.thedonsdarling.domain.util.game.gamerules.GAMERULES_TAG
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.collections.ArrayList

class GameServer {
    companion object {

        suspend fun subscribeToRealtimeUpdates(roomCode: String): Flow<GameRoom> {
            return callbackFlow {
                var room = GameRoom()
//                Log.d(TAG, "RoomCode is: $roomCode")
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

        fun updateGameLog(
            logs: ArrayList<LogMessage>,
            logMessage: LogMessage,
        ): ArrayList<LogMessage> {
//            Log.d(TAG, "updateGameLog is being called")
            logs.add(logMessage)
//            Log.d(TAG, "updateGameLog is done")

            return logs
        }

        fun sendMessage(gameRoom: GameRoom, logMessage: LogMessage) {
//            Log.d(TAG, "sendMessage is being called")
            dbGame.document(gameRoom.roomCode)
                .update("gameLog", FieldValue.arrayUnion(logMessage))
                .addOnSuccessListener {
//                    Log.d(TAG, "Successfully added player!")
                    logMessage.uid?.let { uid -> updateUnreadStatusForAll(gameRoom = gameRoom, uid = uid) }
                }
                .addOnFailureListener {
//                    Log.d(TAG, "Failed to add player! ${it.localizedMessage}")
                }
//            Log.d(TAG, "sendMessage is done")

        }
        private fun updateUnreadStatusForAll(gameRoom: GameRoom, uid: String) {
            gameRoom.players.forEach {
                if (it.uid != uid) {
                    it.unread = true
                }
            }
            dbGame.document(gameRoom.roomCode)
                .update("players", gameRoom.players)
                .addOnSuccessListener {
//                    Log.d(TAG, "Successfully updated player's unread status")
                }
                .addOnFailureListener {
//                    Log.d(TAG, "Failed to update player's unread status. ${it.localizedMessage}")

                }
        }
        fun updateUnreadStatusForLocal(gameRoom: GameRoom, uid: String) {
            gameRoom.players.forEach {
                if (it.uid == uid) {
                    it.unread = false
                }
            }
            dbGame.document(gameRoom.roomCode)
                .update("players", gameRoom.players)
                .addOnSuccessListener {
//                    Log.d(TAG, "Successfully updated player's unread status")
                }
                .addOnFailureListener {
//                    Log.d(TAG, "Failed to update player's unread status. ${it.localizedMessage}")

                }
        }
        fun deleteRoom(game: GameRoom) {
            game.deleteRoom = true
            updateGame(game)
            dbGame.document(game.roomCode)
                .get()
                .addOnSuccessListener { result ->
                    result.reference.delete()
                }
                .addOnFailureListener {
                    println("Failure...")
                }
        }
        fun startNewRound(gameRoom: GameRoom) {
//            Log.d(TAG, "startNewGame is being called")

            val turn = (1..gameRoom.players.size).shuffled().random()
            gameRoom.players.forEach {
                it.hand.clear()
                it.isWinner = false
                it.turn = false
                it.turnInProgress = false
                it.protected = false
                it.isAlive = true
                it.turnOrder = 0
                it.ready = true
            }
            val gameLogs = gameRoom.gameLog.filter { it.type == "userMessage" || it.type == "serverMessage" || it.type == "winnerMessage" }

            val logMessage = LogMessage.createLogMessage(
                message = "A new round is starting.",
                toastMessage = null,
                uid = null,
                type = "serverMessage"
            )

            var game = GameRoom()
            gameLogs.forEach {
                game.gameLog.add(it)
            }
            game.gameLog.add(logMessage)
            game.gameLog.sortByDescending { it.date }
            game.roomNickname = gameRoom.roomNickname
            game.roomCode = gameRoom.roomCode
            game.playLimit = gameRoom.playLimit
            game.players = gameRoom.players
            game.players = GameRules.assignTurns(game.players, turn)
            game.roundOver = false
            game.gameOver = false
            gameRoom.start = true

            game.turn = turn
            game = GameRules.dealCards(game)
            updateGame(game)
//            Log.d(TAG, "startNewGame is done")

        }
        fun startNewGame(gameRoom: GameRoom) {
//            Log.d(TAG, "startNewGame is being called")

            val turn = (1..gameRoom.players.size).shuffled().random()
            gameRoom.players.forEach {
                it.hand.clear()
                it.isWinner = false
                it.turn = false
                it.turnInProgress = false
                it.protected = false
                it.isAlive = true
                it.turnOrder = 0
                it.ready = true
                it.wins = 0
            }
            val gameLogs = gameRoom.gameLog.filter { it.type == "userMessage" || it.type == "serverMessage" || it.type == "winnerMessage" }

            val logMessage = LogMessage.createLogMessage(
                message = "A new round is starting.",
                toastMessage = null,
                uid = null,
                type = "serverMessage"
            )

            var game = GameRoom()
            gameLogs.forEach {
                game.gameLog.add(it)
            }
            game.gameLog.add(logMessage)
            game.gameLog.sortByDescending { it.date }
            game.roomNickname = gameRoom.roomNickname
            game.roomCode = gameRoom.roomCode
            game.playLimit = gameRoom.playLimit
            game.players = gameRoom.players
            game.players = GameRules.assignTurns(game.players, turn)
            game.roundOver = false
            game.gameOver = false
            gameRoom.start = true

            game.turn = turn
            game = GameRules.dealCards(game)
            updateGame(game)
//            Log.d(TAG, "startNewGame is done")

        }
         fun updateGame(gameRoom: GameRoom) {
//            Log.d(TAG, "updateGame is being called")
//            Log.d(TAG, "updateGame is done")


            dbGame.document(gameRoom.roomCode).set(gameRoom)
                .addOnSuccessListener {
//                    Log.d(GAMERULES_TAG, "Successfully updated game room")
                }
                .addOnFailureListener {
//                    Log.d(GAMERULES_TAG, "Failed to update room: ${it.localizedMessage}")
                }
//            Log.d(TAG, "updateGame is done")

        }


    }

}