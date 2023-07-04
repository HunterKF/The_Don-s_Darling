package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class DeleteUserGameRoomForAll(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(

        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    ) {
        repository.deleteUserGameRoomForAll(roomCode, roomNickname, players)
    }
}