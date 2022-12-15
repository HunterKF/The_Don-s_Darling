package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result

class Baron {
    companion object {
        fun compareCards(player1: Player, player2: Player, players: List<Player>): Result {
            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()
            var message = ""
            when {
                player1Card > player2Card -> {
                    player2.isAlive = false
                    message = "${player1.nickName} wins! ${player2.nickName} was eliminated."
                }
                player1Card < player2Card -> {
                    player1.isAlive = false
                    message = "${player2.nickName} wins! ${player1.nickName} was eliminated."

                }
                player1Card == player2Card -> {
                    message =
                        "No one died? ${player1.nickName} and ${player2.nickName} live to tell another tale."
                }
            }
            players.forEach {
                when (it.uid) {
                    player1.uid -> it.isAlive = player1.isAlive
                    player2.uid -> it.isAlive = player2.isAlive
                }
            }
            return Result(
                message = message,
                player1 = player1,
                player2 = player2,
                players = players
            )
        }
    }
}