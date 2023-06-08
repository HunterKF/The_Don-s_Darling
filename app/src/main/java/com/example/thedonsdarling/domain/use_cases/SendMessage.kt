package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class SendMessage(
    private val repository: FireStoreRepository,
) {
    suspend operator fun invoke(gameRoom: GameRoom, logMessage: LogMessage) {
        repository.sendMessage(gameRoom, logMessage)
    }
}