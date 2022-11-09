package com.example.loveletter.domain

data class GameRoom(
    val deck: Deck,
    val turn: Int,
    val roomCode: String,
    val roomNickname: String,
    val playLimit: Int,
    var players: List<Player>,
    var start: Boolean
) {
    constructor() : this(Deck(), 0, "", "", 5, listOf(Player(), Player()), false)
}