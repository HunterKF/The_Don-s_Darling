package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToRealtimeUpdates(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(roomCode: String): Flow<GameRoom> {
       return repository.subscribeToRealtimeUpdates(roomCode)
    }
}