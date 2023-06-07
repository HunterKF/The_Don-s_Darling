package com.example.thedonsdarling.util.game.gamerules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules
import com.google.common.truth.Truth
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GameRulesTest {

    private lateinit var gameRoom2Players: GameRoom
    private lateinit var gameRoom3Players: GameRoom
    private lateinit var gameRoom4Players: GameRoom
    private lateinit var localPlayer: Player


    @Before
    fun setUp() {
        gameRoom2Players = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2,
            )
        )
        gameRoom3Players = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2,
                testPlayer3,
            )
        )
        gameRoom4Players = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2,
                testPlayer3,
                testPlayer4
            )
        )
        localPlayer = testPlayer1
    }

    private fun getPlayer1(gameRoom: GameRoom): Player {
        return gameRoom.players.first { it.uid == testPlayer1.uid }
    }

    private fun getPlayer2(gameRoom: GameRoom): Player {
        return gameRoom.players.first { it.uid == testPlayer2.uid }
    }

    fun getPlayer3(gameRoom: GameRoom): Player {
        return gameRoom.players.first { it.uid == testPlayer3.uid }
    }

    fun getPlayer4(gameRoom: GameRoom): Player {
        return gameRoom.players.first { it.uid == testPlayer4.uid }
    }

    @Test
    fun `Launch on Turn, 2 players, Player 1 is not protected`() {
        var player1 = getPlayer1(gameRoom2Players)
        var player2 = getPlayer2(gameRoom2Players)
        player1.turn = true
        player1.turnInProgress = false
        player1.hand = arrayListOf(1)
        player2.hand = arrayListOf(2)
        val result = GameRules.launchOnTurn(gameRoom2Players)
        player1 = result.players.first { it.uid == testPlayer1.uid }

        Truth.assertThat(player1.hand).isNotEmpty()
        Truth.assertThat(player1.hand.size).isEqualTo(2)
        Truth.assertThat(player1.protected).isFalse()
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)
    }
    @Test
    fun `Launch on Turn, 2 players, Player 1 is protected, toggle protection off`() {
        var player1 = getPlayer1(gameRoom2Players)
        var player2 = getPlayer2(gameRoom2Players)
        player1.turn = true
        player1.turnInProgress = false
        player1.hand = arrayListOf(1)
        player1.protected = true
        player2.hand = arrayListOf(2)
        val result = GameRules.launchOnTurn(gameRoom2Players)
        player1 = result.players.first { it.uid == testPlayer1.uid }

        Truth.assertThat(player1.hand).isNotEmpty()
        Truth.assertThat(player1.hand.size).isEqualTo(2)
        Truth.assertThat(player1.protected).isFalse()
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)
    }

    @Test
    fun `handlePlayedCard`() {
        val card = 6
        val player1 = getPlayer1(gameRoom2Players)
        GameRules.handlePlayedCard(card, player1, gameRoom2Players)

    }
}