package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class RemovePlayerFromGame(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(roomCode: String, player: Player) {
        repository.removePlayerFromGame(roomCode, player)
    }
}