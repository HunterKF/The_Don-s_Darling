package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.TheDoctor
import com.example.thedonsdarling.util.game.gamerules.testGameRoom
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class TheDoctorTest {

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
    fun `Toggle Player 1's protection on`() {
        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.protected = TheDoctor.toggleProtection(player1)
        Truth.assertThat(player1.protected).isTrue()
    }
    @Test
    fun `Toggle Player 1's protection off`() {
        val player1 = gameRoom.players.filter { it.uid == testPlayer1.uid }.first()
        player1.protected = true
        player1.protected = TheDoctor.toggleProtection(player1)
        Truth.assertThat(player1.protected).isFalse()
    }
}