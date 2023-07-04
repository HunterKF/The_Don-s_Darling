package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class AddGameToMultiplePlayers(
    private val repository: FireStoreRepository,
) {
    suspend operator fun invoke(
        gameRoom: GameRoom,
    ) {
        gameRoom.players.forEach { player ->
            repository.addGameToPlayer(player.uid, gameRoom)
        }
    }
}