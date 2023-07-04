package com.example.thedonsdarling.utilTesting.gamerules

import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.util.Tools
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextInt

class ToolsTest {


    private lateinit var listOfPlayers: ArrayList<Player>

    @Before
    fun setUp() {
        val list = arrayListOf<Player>()
        for (i in 1..4) {
            list.add(
                Player().copy(
                    nickName = "Player $i",
                    wins = Random.nextInt(1..4),
                    hand = arrayListOf(Random.nextInt(1..8)),
                    turnOrder = i,
                )
            )
        }
        listOfPlayers = list
    }
    @Test
    fun `Test random string is 4 characters`() {
        val randomString = Tools.getRandomString()
        assertThat(randomString.length).isEqualTo(4)
    }

    @Test
    fun `Random number is within size`() {
        val size = 8
        val randomNumber = Tools.randomNumber(size)
        assertThat(randomNumber).isIn(0..size)
    }

    @Test
    fun `Test player is still playing`() {
        listOfPlayers[Random.nextInt(0..listOfPlayers.size)].turnInProgress = true
        val playerInProgress = Tools.checkCards(listOfPlayers)
        assertThat(playerInProgress).isTrue()
    }

    @Test
    fun `Test player is not playing`() {
        val playerInProgress = Tools.checkCards(listOfPlayers)
        assertThat(playerInProgress).isFalse()
    }
}