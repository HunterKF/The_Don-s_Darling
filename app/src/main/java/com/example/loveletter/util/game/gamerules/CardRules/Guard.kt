package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result

class Guard {
    companion object {
        fun returnResult(player1: Player, player2: Player, guessedCard: Int): Result {
            var message = ""
            if (player2.hand.first() == guessedCard) {
                player2.isAlive = false
                message = "${player1.nickName} guessed correctly! ${player2.nickName} was eliminated."
            } else {
                message = "${player1.nickName} guessed $guessedCard, but ${player2.nickName} lives to tell the tale."
            }
            return Result(
                message = message,
                player1 = player1,
                player2 = player2,
                players = null,
                game = null
            )
        }
    }
}