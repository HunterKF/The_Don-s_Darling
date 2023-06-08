package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.JoinGameResult
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class JoinGame(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(roomCode: String, player: Player): JoinGameResult {
        return repository.joinGame(roomCode, player)
    }
}