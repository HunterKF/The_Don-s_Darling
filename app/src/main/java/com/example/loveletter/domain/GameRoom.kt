package com.example.loveletter.domain

data class GameRoom(
    val deck: Deck,
    val turn: Int,
    val roomCode: String,
    val roomNickname: String,
    val playLimit: Int,
    val players: List<Player>,
    val start: Boolean
) {
    constructor() : this(Deck(), 0, "ABCD", "Testing", 5, listOf(Player(), Player()), false)
}