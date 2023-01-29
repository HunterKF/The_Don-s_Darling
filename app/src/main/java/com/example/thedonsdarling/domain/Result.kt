package com.example.thedonsdarling.domain

data class Result(
    var message: String,
    var toastMessage: String?,
    val player1: Player?,
    val player2: Player?,
    val players: List<Player>?,
    var game: GameRoom?
)
