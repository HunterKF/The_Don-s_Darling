package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import org.junit.Assert.*
import org.junit.Before

class TheDonTest {
    private lateinit var gameRoom: GameRoom
    private lateinit var player1: Player
    private lateinit var player2: Player


    @Before
    fun setUp() {
        gameRoom = GameRoom()
        gameRoom = gameRoom.copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                Player().copy(
                    avatar = 1,
                    nickName = "Apple",
                    uid = "apple_uid",
                    ready = true,
                    protected = false,
                    turn = true,
                    turnInProgress = true,
                    turnOrder = 1,
                    hand = arrayListOf(1, 2),
                    isHost = true,
                    isAlive = true,
                    isWinner = false,
                    wins = 0,
                    unread = false,
                    guide = true
                ),
                Player().copy(
                    avatar = 1,
                    nickName = "Bear",
                    uid = "bear_uid",
                    ready = true,
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
            ),
            start = true,
            host = "apple_uid",
            roundOver = false,
            gameOver = false,
            showLogs = true,
            deleteRoom = false,
            deckClear = false,
            gameLog = arrayListOf()
        )
        player1 = Player().copy(
            avatar = 1,
            nickName = "Apple",
            uid = "apple_uid",
            ready = true,
            protected = false,
            turn = true,
            turnInProgress = true,
            turnOrder = 1,
            hand = arrayListOf(1, 2),
            isHost = true,
            isAlive = true,
            isWinner = false,
            wins = 0,
            unread = false,
            guide = true
        )
        player2 =
            Player().copy(
                avatar = 1,
                nickName = "Bear",
                uid = "bear_uid",
                ready = true,
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
    }
}