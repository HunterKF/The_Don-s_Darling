package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import android.util.Log
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result

class TheDon {
    companion object {
        fun swapCards(player1: Player, player2: Player, gameRoom: GameRoom): Result {

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

            return Result(
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