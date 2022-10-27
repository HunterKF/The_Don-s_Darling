package com.example.loveletter.presentation.game

import com.example.loveletter.domain.GameRoom


sealed class GameState {
    object Loading : GameState()
    data class Loaded(
        val gameRoom: GameRoom
    ) : GameState()
}
