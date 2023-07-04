package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class SetGameInDB(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(
        gameRoom: GameRoom
    ) {
        repository.setGameInDB_update(gameRoom)
    }
}