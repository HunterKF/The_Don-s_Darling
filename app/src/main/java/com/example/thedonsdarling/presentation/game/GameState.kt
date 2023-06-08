package com.example.thedonsdarling.presentation.game

import com.example.thedonsdarling.domain.models.GameRoom


sealed class GameState {
    object Loading : GameState()
    data class Loaded(
        val gameRoom: GameRoom
    ) : GameState()
}
