package com.example.thedonsdarling.domain

data class Result(
    var cardResult: Any?,
    var message: String,
    var toastMessage: String? = null,
    val player1: Player?,
    val player2: Player?,
    val players: List<Player>?,
    var game: GameRoom?
)

sealed class CardResult {

}

