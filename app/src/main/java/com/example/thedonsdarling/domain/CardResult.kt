package com.example.thedonsdarling.domain

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player

data class CardResult(
    var cardResult: Any?,
    var message: String,
    var toastMessage: String? = null,
    val player1: Player?,
    val player2: Player?,
    val players: List<Player>?,
    var game: GameRoom?
)

