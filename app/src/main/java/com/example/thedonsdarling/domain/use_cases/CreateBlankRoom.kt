package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.Deck
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository

class CreateBlankRoom(
    private val repository: FireStoreRepository,
) {
     operator fun invoke(
        roomNickname: String,
        playLimit: Int,
        players: List<Player>,
        roomCode: String,
        uid: String
    ): GameRoom {
        val deck = Deck()

        return GameRoom(
            deck = deck,
            turn = 0,
            roomNickname = roomNickname,
            playLimit = playLimit,
            players = players,
            roomCode = roomCode,
            start = false,
            host = uid,
            roundOver = false,
            gameOver = false,
            showLogs = true,
            deleteRoom = false,
            deckClear = false,
            gameLog = arrayListOf()
        )
    }
}