package com.example.thedonsdarling.domain.models

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
    var deckClear: Boolean,
    var gameLog: ArrayList<LogMessage>,
) {

    constructor() : this(
        Deck(),
        turn = 0,
        roomCode = "",
        roomNickname = "",
        playLimit = 5,
        players = listOf(Player(), Player()),
        start = false,
        host = "",
        roundOver = false,
        gameOver = false,
        showLogs = true,
        deleteRoom = false,
        deckClear = false,
        gameLog = arrayListOf()
    )
}