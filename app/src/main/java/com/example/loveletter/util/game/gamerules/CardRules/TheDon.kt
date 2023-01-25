package com.example.loveletter.util.game.gamerules.CardRules

import android.util.Log
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result

class TheDon {
    companion object {
        fun swapCards(player1: Player, player2: Player, gameRoom: GameRoom): Result {
            Log.d("King", "swapCards has started...getting cards: ${player1.nickName}'s card: ${player1.hand} and ${player2.nickName}'s card: ${player2.hand}")

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
                message = "${player1.nickName} swapped cards with ${player2.nickName}.",
                toastMessage = "${player1.nickName} swapped cards with ${player2.nickName}.",
                player1 = null,
                player2 = null,
                players = gameRoom.players,
                game = null
            )
        }
    }
}