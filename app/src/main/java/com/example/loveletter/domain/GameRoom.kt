package com.example.loveletter.domain

data class GameRoom(
    val deck: Deck,
    var turn: Int,
    var roomCode: String,
    var roomNickname: String,
    var playLimit: Int,
    var players: List<Player>,
    var start: Boolean,
    var host: String,
    var roundOver: Boolean,
    var gameOver: Boolean,
    var showLogs: Boolean,
    var deleteRoom: Boolean,
    var gameLog: ArrayList<LogMessage>,
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
        false,
        true,
        false,
        arrayListOf()
    )
}