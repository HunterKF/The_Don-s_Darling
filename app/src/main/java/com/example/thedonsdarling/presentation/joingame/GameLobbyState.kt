package com.example.thedonsdarling.presentation.joingame

import com.example.thedonsdarling.domain.GameRoom

sealed class GameLobbyState {
    object Loading : GameLobbyState()
    data class Loaded(
        val gameRoom: GameRoom
    ) : GameLobbyState()
}