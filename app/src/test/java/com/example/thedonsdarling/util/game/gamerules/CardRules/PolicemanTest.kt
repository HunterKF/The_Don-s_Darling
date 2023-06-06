package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.util.game.gamerules.testGameRoom
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.google.common.truth.Truth

import org.junit.Before
import org.junit.Test

class PolicemanTest {

    private lateinit var gameRoom: GameRoom

    @Before
    fun setUp() {
        gameRoom = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2
            )
        )
    }

    @Test
    fun `Player 1 guess (6) on Player 2(1), return WrongGuess`() {
        for (i in 2..8) {
            val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
            player1.hand = arrayListOf(5, 1)
            val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }
            player2.hand = arrayListOf(1)

            val result = Policeman.returnResult(player1, player2, guessedCard = 6, gameRoom)
            Truth.assertThat(result.cardResult).isEqualTo(Policeman.WrongGuess)
        }
    }

    @Test
    fun `Player 1 guess (2-8) on Player 2(2-8), return GoodGuess, eliminate Player 2`() {
        for (i in 2..8) {
            val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
            player1.hand = arrayListOf(5, 1)
            val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }
            player2.hand = arrayListOf(i)

            val result = Policeman.returnResult(player1, player2, guessedCard = i, gameRoom)
            Truth.assertThat(result.cardResult).isEqualTo(Policeman.CorrectGuess)
            val alteredPlayer2 = result.game!!.players.first { it.uid == testPlayer2.uid }
            Truth.assertThat(alteredPlayer2.isAlive).isFalse()
            Truth.assertThat(alteredPlayer2.hand).isEmpty()
        }
    }
}