package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.TheDon
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class TheDonTest {
    private lateinit var gameRoom: GameRoom


    @Before
    fun setUp() {
        gameRoom = GameRoom()
        gameRoom = gameRoom.copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1,
                testPlayer2
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
    }

    @Test
    fun `Swap cards, 1 to 8, return swapped player cards`() {
        for (i in 1..8) {
            val localPlayerOne = gameRoom.players.first { it.uid == testPlayer1.uid }
            localPlayerOne.hand = arrayListOf(i)
            val localPlayerOneCard = gameRoom.players.first { it.uid == testPlayer1.uid }.hand.first()
            val localPlayerTwo = gameRoom.players.first { it.uid == testPlayer2.uid }
            var offsetI = i - 1
            if (offsetI == 0 ) {
                offsetI = 1
            }
            localPlayerTwo.hand = arrayListOf(offsetI)
            val localPlayerTwoCard = gameRoom.players.first { it.uid == testPlayer2.uid }.hand.first()

            val result = TheDon.swapCards(localPlayerOne, localPlayerTwo, gameRoom)
            val newPlayerOne = result.players!!.first { it.uid == localPlayerOne.uid }
            val newPlayerTwo = result.players!!.first { it.uid == localPlayerTwo.uid }

            Truth.assertThat(newPlayerOne.hand.first()).isEqualTo(localPlayerTwoCard)
            Truth.assertThat(newPlayerTwo.hand.first()).isEqualTo(localPlayerOneCard)
        }
    }
}