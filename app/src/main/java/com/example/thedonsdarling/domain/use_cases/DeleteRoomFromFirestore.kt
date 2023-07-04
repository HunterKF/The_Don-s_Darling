package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class DeleteRoomFromFirestore(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(
        gameRoom: GameRoom
    ) {
        repository.deleteRoomFromFireStore(gameRoom)
    }
}