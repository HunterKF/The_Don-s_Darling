package com.example.loveletter.presentation.joingame

import com.example.loveletter.domain.GameRoom

sealed class GameLobbyState {
    object Loading : GameLobbyState()
    data class Loaded(
        val gameRoom: GameRoom
    ) : GameLobbyState()
}