package com.example.loveletter.domain

data class GameRoom(
    val deck: Deck,
    var turn: Int,
    var roomCode: String,
    val roomNickname: String,
    var playLimit: Int,
    var players: List<Player>,
    var start: Boolean,
    var host: String,
    var roundOver: Boolean,
    var gameOver: Boolean,
) {
    constructor() : this(Deck(),
        0,
        "",
        "",
        5,
        listOf(Player(), Player()),
        false,
        "",
        false,
        false)
}