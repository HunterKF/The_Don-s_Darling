package com.example.loveletter.util.game

import android.util.Log
import com.example.loveletter.TAG
import com.example.loveletter.dbGame
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.LogMessage
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
                Log.d(TAG, "RoomCode is: $roomCode")
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
            Log.d(TAG, "updateGameLog is being called")
            logs.add(logMessage)
            Log.d(TAG, "updateGameLog is done")

            return logs
        }

        fun sendMessage(gameRoom: GameRoom, logMessage: LogMessage) {
            Log.d(TAG, "sendMessage is being called")
            dbGame.document(gameRoom.roomCode)
                .update("gameLog", FieldValue.arrayUnion(logMessage))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added player!")
                    logMessage.uid?.let { uid -> updateUnreadStatusForAll(gameRoom = gameRoom, uid = uid) }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add player! ${it.localizedMessage}")
                }
            Log.d(TAG, "sendMessage is done")

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
                    Log.d(TAG, "Successfully updated player's unread status")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to update player's unread status. ${it.localizedMessage}")

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
                    Log.d(TAG, "Successfully updated player's unread status")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to update player's unread status. ${it.localizedMessage}")

                }
        }
    }

}