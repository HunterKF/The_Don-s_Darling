package com.example.loveletter.util.startgame

import android.util.Log
import com.example.loveletter.data.Deck
import com.example.loveletter.data.GameRoom
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StartGame() {

    companion object {
         fun createCodedRoom(roomCode: String) {
            val deck = Deck()
            val gameRoom = GameRoom(
                deck = deck,
                whoseTurn = 0,
                roomNickname = "",
                playLimit = 5,
                players = listOf(""),
                roomCode = roomCode
            )
            val db = Firebase.firestore
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
            players: List<String>,
            roomCode: String
        ) {
            val deck = Deck()
            val gameRoom = GameRoom(
                deck = deck,
                whoseTurn = 0,
                roomNickname = roomNickname,
                playLimit = playLimit,
                players = players,
                roomCode = roomCode
            )
            val db = Firebase.firestore
            db.collection("game").document(roomCode)
                .set(gameRoom)
                .addOnSuccessListener {
                    println("Success!")
                }
                .addOnFailureListener {
                    println("Failure...")
                }
        }
        fun deleteRoom(roomCode: String) {
            val db = Firebase.firestore
            db.collection("game").document(roomCode)
                .get()
                .addOnSuccessListener { result ->
                    result.reference.delete()
                }
                .addOnFailureListener {
                    println("Failure...")
                }
        }
        fun getRandomString() : String {
            val allowedChars = ('A'..'Z') +  ('0'..'9')
            return (1..4)
                .map { allowedChars.random() }
                .joinToString("")
        }
        fun createPlayers(players: List<Player>) {

        }
    }

}