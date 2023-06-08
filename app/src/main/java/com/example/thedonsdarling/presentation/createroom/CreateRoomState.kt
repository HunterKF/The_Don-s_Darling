package com.example.thedonsdarling.presentation.createroom

import com.example.thedonsdarling.domain.models.GameRoom

sealed class CreateRoomState {
    object Loading : CreateRoomState()
    data class Loaded(
        val gameRoom: GameRoom,
    ) : CreateRoomState()
}
