package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.util.Log

class Courtesan {
    companion object {
        fun checkCard(card: Int): Boolean {
            return card == 6 || card == 5
        }
    }
}