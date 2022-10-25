package com.example.loveletter.util.joingame

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.loveletter.TAG
import com.example.loveletter.domain.Player
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JoinGame {
    companion object {
        private val db = Firebase.firestore
        fun checkGame(roomCode: String, roomFound: MutableState<Boolean>, context: Context): Boolean {
            db.collection("game").document(roomCode).get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "Result found: $result")
                    if (result.data == null) {
                        Log.d(TAG, "Room code not found. Failure.")
                        Toast.makeText(context, "Room not found! Please try again", Toast.LENGTH_SHORT).show()
                        roomFound.value = false
                    } else {
                        Log.d(TAG, "Room code found. Success.")
                        Toast.makeText(context, "Room found!", Toast.LENGTH_SHORT).show()
                        roomFound.value = true
                    }
                }
            return roomFound.value
        }

        fun joinGame(roomCode: String, player: Player) {
            db.collection("game").document(roomCode).update("players", FieldValue.arrayUnion(player))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added player!")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add player! ${it.localizedMessage}")
                }
        }

        fun leaveGame(roomCode: String, player: Player) {
            db.collection("game").document(roomCode).update("players", FieldValue.arrayRemove(player))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added player!")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add player! ${it.localizedMessage}")
                }
        }
    }

    }
