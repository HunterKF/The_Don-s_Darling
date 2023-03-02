package com.example.thedonsdarling.utilTesting.gamerules

import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.util.game.gamerules.GameRules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRulesTest {
    private lateinit var gameRules: GameRules
    private lateinit var player1: Player
    private lateinit var player2: Player

    @Before
    fun setUp() {
        gameRules = GameRules()
        player1 = Player().copy(
            avatar = 1,
            nickName = "Player1",
            uid = "",
            ready = false,
            protected = false,
            turn = false,
            turnInProgress = false,
            turnOrder = 1,
            hand = arrayListOf(1),
            isHost = false,
            isAlive = true,
            isWinner = false,
            wins = 0,
            unread = false,
            guide = true
        )
        player2 = Player().copy(
            avatar = 2,
            nickName = "Player2",
            uid = "",
            ready = false,
            protected = false,
            turn = false,
            turnInProgress = false,
            turnOrder = 1,
            hand = arrayListOf(2),
            isHost = false,
            isAlive = true,
            isWinner = false,
            wins = 0,
            unread = false,
            guide = true
        )
    }
}