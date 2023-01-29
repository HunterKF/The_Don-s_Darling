package com.example.thedonsdarling.util.game.gamerules.gameserver

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.thedonsdarling.R
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.dbGame
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.google.firebase.firestore.FieldValue

class ConnectionRules {
    companion object {
        fun checkGame(
            roomCode: String,
            roomFound: MutableState<Boolean>,
            context: Context,
        ): Boolean {

            dbGame.document(roomCode).get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "Result found: $result")
                    if (result.data == null) {
                        Log.d(TAG, "Room code not found. Failure.")
                        Toast.makeText(context,
                            context.getText(R.string.check_game_not_found),
                            Toast.LENGTH_SHORT).show()
                        roomFound.value = false
                    } else {
                        Log.d(TAG, "Room code found. Success.")
                        Toast.makeText(context,
                            context.getText(R.string.check_game_found),
                            Toast.LENGTH_SHORT).show()
                        roomFound.value = true
                    }
                }
            return roomFound.value
        }

        fun joinGame(roomCode: String, player: Player, context: Context, onSuccess: () -> Unit) {
            dbGame.document(roomCode).get()
                .addOnSuccessListener { result ->
                    if (result.data == null) {
                        Toast.makeText(context,
                            context.getText(R.string.check_game_not_found),
                            Toast.LENGTH_SHORT).show()
                    } else {
                        val gameRoom = result.toObject(GameRoom::class.java)
                        gameRoom?.let {
                            if (it.players.size == 4) {
                                Toast.makeText(context,
                                    context.getText(R.string.check_game_full),
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                dbGame.document(roomCode)
                                    .update("players", FieldValue.arrayUnion(player))
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Successfully added player!")
                                        onSuccess()
                                    }
                                    .addOnFailureListener {
                                        Log.d(TAG, "Failed to add player! ${it.localizedMessage}")
                                    }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context,
                        context.getText(R.string.check_game_not_found),
                        Toast.LENGTH_SHORT).show()
                }
        }

        fun leaveGame(roomCode: String, player: Player) {
            dbGame.document(roomCode)
                .update("players", FieldValue.arrayRemove(player))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully left game!")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to leave game! ${it.localizedMessage}")
                }

        }
    }

}
