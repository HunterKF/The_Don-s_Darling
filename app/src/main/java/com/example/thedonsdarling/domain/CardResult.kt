package com.example.thedonsdarling.domain

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.models.UiText

data class CardResult(
    var cardResult: Any?,
    var message: UiText,
    var toastMessage: UiText? = null,
    val player1: Player?,
    val player2: Player?,
    val players: List<Player>?,
    var game: GameRoom?
)

