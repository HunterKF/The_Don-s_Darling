package com.example.loveletter.util.game.gamerules.CardRules

import android.util.Log

class Courtesan {
    companion object {
        fun checkHand(hand: ArrayList<Int>): Boolean {
            Log.d("Countess", "(checkHand) Returning ${hand.contains(7)}")
            return hand.contains(7)
        }
        fun checkCard(card: Int): Boolean {
            Log.d("Countess", "(checkCard) Returning ${card == 6 || card == 5}")

            return card == 6 || card == 5
        }
    }
}