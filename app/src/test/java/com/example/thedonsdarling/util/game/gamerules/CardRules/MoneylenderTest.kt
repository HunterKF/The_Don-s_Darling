package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.Moneylender
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.example.thedonsdarling.util.game.gamerules.testPlayer3
import com.google.common.truth.Truth
import org.junit.Test

class MoneylenderTest {

    @Test
    fun `Player 1 hand(7), P2 hand (2), return P1 wins`() {
        val gameRoom = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(7),
                    isAlive = true,
                    turn = false,
                ),
                testPlayer2.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = true,

                    ),
                testPlayer3.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false,
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
        val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
        val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }

        val result = Moneylender.compareCards(
            player1 = player1,
            player2 = player2,
            players = gameRoom.players,
            game = gameRoom
        )

        Truth.assertThat(result.cardResult).isEqualTo(Moneylender.Player1Wins)
    }
    @Test
    fun `Player 1 hand(2), P2 hand (5), return P2 wins`() {
        val gameRoom = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false,
                ),
                testPlayer2.copy(
                    hand = arrayListOf(5),
                    isAlive = true,
                    turn = true,

                    ),
                testPlayer3.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false,
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
        val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
        val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }

        val result = Moneylender.compareCards(
            player1 = player1,
            player2 = player2,
            players = gameRoom.players,
            game = gameRoom
        )

        Truth.assertThat(result.cardResult).isEqualTo(Moneylender.Player2Wins)
    }
    @Test
    fun `Player 1 hand(5), P2 hand (5), return draw`() {
        val gameRoom = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(5),
                    isAlive = true,
                    turn = false,
                ),
                testPlayer2.copy(
                    hand = arrayListOf(5),
                    isAlive = true,
                    turn = true,

                    ),
                testPlayer3.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false,
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
        val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
        val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }

        val result = Moneylender.compareCards(
            player1 = player1,
            player2 = player2,
            players = gameRoom.players,
            game = gameRoom
        )

        Truth.assertThat(result.cardResult).isEqualTo(Moneylender.Draw)
    }
}