package com.example.loveletter.data

class GameRoom(
    val deck: Deck,
    val whoseTurn: Int,
    val roomCode: String,
    val roomNickname: String,
    val playLimit: Int,
    val players: List<String>
)