package com.example.loveletter.util.joingame

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.loveletter.TAG
import com.example.loveletter.dbGame
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class JoinGame {
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
                            "Game not found! Please try again",
                            Toast.LENGTH_SHORT).show()
                        roomFound.value = false
                    } else {
                        Log.d(TAG, "Room code found. Success.")
                        Toast.makeText(context, "Game found!", Toast.LENGTH_SHORT).show()
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
                            "Room code not find, try again!",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        val gameRoom = result.toObject(GameRoom::class.java)
                        gameRoom?.let {
                            if (it.players.size == 4) {
                                Toast.makeText(context, "Game is full!", Toast.LENGTH_SHORT).show()
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

                   /* if (result.data!!.size == 4) {
                        Log.d(TAG, "${result.data!!.size}")
                        Log.d(TAG, "Room code found, but too many players.")
                        Toast.makeText(context, "Game is full!", Toast.LENGTH_SHORT).show()
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
                    }*/
                }
                .addOnFailureListener {
                    Toast.makeText(context,
                        "Room code not find, try again!",
                        Toast.LENGTH_SHORT).show()
                }


        }

        fun leaveGame(roomCode: String, player: Player) {
            dbGame.document(roomCode)
                .update("players", FieldValue.arrayRemove(player))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added player!")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add player! ${it.localizedMessage}")
                }
        }
    }

}
