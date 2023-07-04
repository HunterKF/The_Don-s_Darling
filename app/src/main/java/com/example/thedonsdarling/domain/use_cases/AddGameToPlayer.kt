package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class AddGameToPlayer(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(
        userId: String, gameRoom: GameRoom
    ) {
        repository.addGameToPlayer(userId, gameRoom)
    }
}