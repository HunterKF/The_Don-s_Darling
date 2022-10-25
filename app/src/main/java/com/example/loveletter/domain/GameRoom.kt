package com.example.loveletter.domain

class GameRoom(
    val deck: Deck,
    val turn: Int,
    val roomCode: String,
    val roomNickname: String,
    val playLimit: Int,
    val players: List<Player>,
) {
    constructor() : this(Deck(), 0, "1234", "", 0, listOf())
}