package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class UpdateUnreadStatusForLocal(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(gameRoom: GameRoom, uid: String) {
        gameRoom.players.forEach {
            if (it.uid == uid) {
                it.unread = false
            }
        }
        repository.updatePlayers(gameRoom, uid)
    }
}