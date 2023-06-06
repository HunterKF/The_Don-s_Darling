package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.util.game.gamerules.testGameRoom
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.google.common.truth.Truth

import org.junit.Before
import org.junit.Test

class WiseguyTest {

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
    fun `Play wiseguy, Player 1 (1) force Player 2 (7) discard, return ForcedToDiscard`() {
        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.hand = arrayListOf(1)
        val player2 = gameRoom.players.filter { it.uid == testPlayer2.uid }.first()
        player2.hand = arrayListOf(7)
        gameRoom.deck.deck.minus(7)
        println(gameRoom)
        val result = Wiseguy.discardAndDraw(player1, player2, gameRoom)
        val alteredPlayer2 = result.game!!.players.filter { it.uid == player2.uid }.first()

        Truth.assertThat(result.cardResult).isEqualTo(Wiseguy.ForcedToDiscard)
        Truth.assertThat(alteredPlayer2.hand.first()).isNotEqualTo(7)
        Truth.assertThat(result.game!!.deck.discardDeck).isNotEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck.first()).isEqualTo(7)
    }

    @Test
    fun `Play wiseguy, Player 1 (1) force Player 2 (8) discard, return ForcedToDiscardDarling`() {
       gameRoom = gameRoom.copy(
           host = "THIS IS FROM THE FIRST TEST"
       )

        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.hand = arrayListOf(1)
        val player2 = gameRoom.players.filter { it.uid == testPlayer2.uid }.first()
        player2.hand = arrayListOf(8)
        gameRoom.deck.deck.minus(8)
        val result = Wiseguy.discardAndDraw(player1, player2, gameRoom)
        val alteredPlayer2 = result.game!!.players.filter { it.uid == player2.uid }.first()

        Truth.assertThat(result.cardResult).isEqualTo(Wiseguy.ForcedToDiscardDarling)
        Truth.assertThat(alteredPlayer2.isAlive).isFalse()
        Truth.assertThat(result.game!!.deck.discardDeck).isNotEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck).contains(8)
    }

    @Test
    fun `Play wiseguy, Player 1 (1) force Player 2 (5) discard, return ForcedToDiscardAndEmptyDeck`() {
        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.hand = arrayListOf(1)
        val player2 = gameRoom.players.filter { it.uid == testPlayer2.uid }.first()
        player2.hand = arrayListOf(5)
        gameRoom.deck.deck.clear()
        val result = Wiseguy.discardAndDraw(player1, player2, gameRoom)
        val alteredPlayer2 = result.game!!.players.first { it.uid == player2.uid }

        Truth.assertThat(result.cardResult).isEqualTo(Wiseguy.ForcedToDiscardAndEmptyDeck)
        Truth.assertThat(alteredPlayer2.isAlive).isFalse()
        Truth.assertThat(alteredPlayer2.hand).isEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck).isNotEmpty()
        Truth.assertThat(result.game!!.deck.deck).isEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck).contains(5)
    }

    @Test
    fun `Play wiseguy, Player 1 (1) force Player 2 (7) discard, return ForcedToDiscardAndEmptyDeck`() {
        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.hand = arrayListOf(1)
        val player2 = gameRoom.players.filter { it.uid == testPlayer2.uid }.first()
        player2.hand = arrayListOf(7)
        gameRoom.deck.deck.clear()
        val result = Wiseguy.discardAndDraw(player1, player2, gameRoom)
        val alteredPlayer2 = result.game!!.players.first { it.uid == player2.uid }

        Truth.assertThat(result.cardResult).isEqualTo(Wiseguy.ForcedToDiscardAndEmptyDeck)
        Truth.assertThat(alteredPlayer2.isAlive).isFalse()
        Truth.assertThat(alteredPlayer2.hand).isEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck).isNotEmpty()
        Truth.assertThat(result.game!!.deck.deck).isEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck.first()).isEqualTo(7)
    }

    @Test
    fun `Play wiseguy, deck empty, Player 1 (1) force Player 2 (8) discard, return ForcedToDiscardDarling`() {
        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.hand = arrayListOf(1)
        val player2 = gameRoom.players.filter { it.uid == testPlayer2.uid }.first()
        player2.hand = arrayListOf(8)
        gameRoom.deck.deck.clear()
        val result = Wiseguy.discardAndDraw(player1, player2, gameRoom)
        val alteredPlayer2 = result.game!!.players.first { it.uid == player2.uid }

        Truth.assertThat(result.cardResult).isEqualTo(Wiseguy.ForcedToDiscardDarling)
        Truth.assertThat(alteredPlayer2.isAlive).isFalse()
        Truth.assertThat(alteredPlayer2.hand).isEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck).isNotEmpty()
        Truth.assertThat(result.game!!.deck.deck).isEmpty()
        Truth.assertThat(result.game!!.deck.discardDeck).contains(8)
    }
}