package com.example.loveletter.presentation.createroom

import com.example.loveletter.domain.GameRoom

sealed class CreateRoomState {
    object Loading : CreateRoomState()
    data class Loaded(
        val gameRoom: GameRoom,
    ) : CreateRoomState()
}
