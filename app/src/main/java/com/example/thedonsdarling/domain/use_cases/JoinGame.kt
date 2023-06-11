package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.JoinGameResult
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class JoinGame(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(roomCode: String, player: Player): JoinGameResult {
        val result = repository.getAndReturnGame(roomCode, player) ?: return JoinGameResult.CodeNotFound

        return if (result.players.size >= 4) {
            JoinGameResult.GameFull
        } else {
            repository.addPlayerToGame(roomCode, player)
            JoinGameResult.Success
        }
    }
}