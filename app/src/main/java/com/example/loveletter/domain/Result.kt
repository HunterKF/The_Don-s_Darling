package com.example.loveletter.domain

data class Result(
    var message: String,
    val player1: Player?,
    val player2: Player?,
    val players: List<Player>?,
    var game: GameRoom?
)
