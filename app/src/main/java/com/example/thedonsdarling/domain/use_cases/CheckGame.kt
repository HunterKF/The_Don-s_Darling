package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.CheckGameResult
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class CheckGame(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(roomCode: String): CheckGameResult {
        return repository.checkGame(roomCode)
    }
}