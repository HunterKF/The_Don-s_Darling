package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.Courtesan
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CourtesanTest {

    @Test
    fun `Courtesan Test, card 6, return true`() {
        val card = 6
        val result = Courtesan.checkCard(card)
        Truth.assertThat(result).isTrue()
    }
    @Test
    fun `Courtesan Test, card 5, return true`() {
        val card = 5
        val result = Courtesan.checkCard(card)
        Truth.assertThat(result).isTrue()
    }
    @Test
    fun `Courtesan Test, return false`() {
        val card = 2
        val result = Courtesan.checkCard(card)
        Truth.assertThat(result).isFalse()
    }
}