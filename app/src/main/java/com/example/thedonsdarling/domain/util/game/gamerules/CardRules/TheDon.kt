package com.example.thedonsdarling.domain.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.CardResult

class TheDon {
    companion object {
        fun swapCards(player1: Player, player2: Player, gameRoom: GameRoom): CardResult {

            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()


            gameRoom.players.forEach {

                when (it.uid) {
                    player1.uid -> {
                        it.hand.clear()
                        it.hand.add(player2Card)

                    }
                    player2.uid -> {
                        it.hand.clear()
                        it.hand.add(player1Card)

                    }
                }
            }

            return CardResult(
                cardResult = null,
                message = "",
                player1 = null,
                player2 = null,
                players = gameRoom.players,
                game = null
            )
        }
    }
}