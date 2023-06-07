package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.Darling
import com.example.thedonsdarling.util.game.gamerules.testGameRoom
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


private const val FAKE_STRING = "HELLO WORLD"

@RunWith(MockitoJUnitRunner::class)
class DarlingTest {
    private lateinit var gameRoom: GameRoom
    private lateinit var player1: Player
    private lateinit var player2: Player


    @Before
    fun setUp() {
        gameRoom = testGameRoom
        gameRoom = gameRoom.copy(
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

    @Test
    fun `Test and eliminate a player`() {
        val player1 = player1
        val result = Darling.eliminatePlayer(gameRoom, this.player1)
        Truth.assertThat(result.cardResult).isEqualTo(Darling.PlayerEliminated)
        result.game?.let {
            it.players.forEach { gamePlayer ->
                if (gamePlayer.uid == player1.uid) {
                    Truth.assertThat(gamePlayer.isAlive).isFalse()
                } else {
                    Truth.assertThat(gamePlayer.isAlive).isTrue()
                }
            }
        }
        Truth.assertThat(result.cardResult).isEqualTo(Darling.PlayerEliminated)
    }

    @Test
    fun `Check for Darling, give 1 to 7, return PlayerSafe`() {
        for (i in 1..7) {
            val result = Darling.checkForDarling(i, player1, player2, gameRoom)
            Truth.assertThat(result.cardResult).isEqualTo(Darling.PlayerSafe)
        }
    }

    @Test
    fun `Check for Darling, give 8, return PlayerEliminated`() {
        val result = Darling.checkForDarling(8, player1, player2, gameRoom)
        Truth.assertThat(result.cardResult).isEqualTo(Darling.PlayerEliminated)
    }

}