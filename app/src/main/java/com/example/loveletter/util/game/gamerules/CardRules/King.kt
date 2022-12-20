package com.example.loveletter.util.game.gamerules.CardRules

import android.util.Log
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result

class King {
    companion object {
        fun swapCards(player1: Player, player2: Player, gameRoom: GameRoom): Result {
            Log.d("King", "swapCards has started...getting cards: ${player1.nickName}'s card: ${player1.hand} and ${player2.nickName}'s card: ${player2.hand}")

            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()
            Log.d("King", "initiating cards: ${player1.nickName} is ${player1Card}")
            Log.d("King", "initiating cards: ${player2.nickName} is ${player2Card}")


            gameRoom.players.forEach {
                Log.d("King", "forEach is firing")

                when (it.uid) {
                    player1.uid -> {
                        Log.d("King", "([forEach]before- p1)${it.nickName}'s hand is now ${it.hand}")
                        it.hand.clear()
                        it.hand.add(player2Card)
                        Log.d("King", "([forEach]after- p1)${it.nickName}'s hand is now ${it.hand}")

                    }
                    player2.uid -> {
                        Log.d("King", "([forEach]before- p2)${it.nickName}'s hand is now ${it.hand}")
                        it.hand.clear()
                        it.hand.add(player1Card)
                        Log.d("King", "([forEach]before- p2)${it.nickName}'s hand is now ${it.hand}")

                    }
                }
            }

            return Result(
                message = "${player1.nickName} swapped cards with ${player2.nickName}.",
                player1 = null,
                player2 = null,
                players = gameRoom.players,
                game = null
            )
        }
    }
}