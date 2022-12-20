package com.example.loveletter.domain

data class Result(
    val message: String,
    val player1: Player?,
    val player2: Player?,
    val players: List<Player>?,
    val game: GameRoom?
)
