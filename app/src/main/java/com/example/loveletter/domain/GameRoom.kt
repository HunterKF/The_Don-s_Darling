package com.example.loveletter.domain

data class GameRoom(
    val deck: Deck,
    var turn: Int,
    val roomCode: String,
    val roomNickname: String,
    val playLimit: Int,
    var players: List<Player>,
    var start: Boolean,
    var host: String
) {
    constructor() : this(Deck(), 0, "", "", 5, listOf(Player(), Player()), false, "")
}