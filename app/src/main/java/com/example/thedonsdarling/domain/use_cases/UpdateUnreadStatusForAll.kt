package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class UpdateUnreadStatusForAll(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(gameRoom: GameRoom, uid: String) {
        gameRoom.players.forEach {
            if (it.uid != uid) {
                it.unread = true
            }
        }
        repository.updatePlayers(gameRoom, uid)
    }
}