package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class DeleteUserGameRoomForLocal(
    private val repository: FireStoreRepository,
) {
    suspend operator fun invoke(
        roomCode: String,
        roomNickname: String,
        player: Player,
    ) {
        repository.deleteUserGameRoomForLocal(roomCode, roomNickname, player)
    }
}