package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

class Moneylender {
    companion object {
        fun compareCards(
            player1: Player,
            player2: Player,
            players: List<Player>,
            game: GameRoom,
            context: Context
        ): Result {
            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()
            var message = ""
            var toastMessage = ""
            var updatedGameRoom = game
            when {
                player1Card > player2Card -> {
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player2)
                    message = context.getString(R.string.card_moneylender_message, player1.nickName, player2.nickName)
                    toastMessage = context.getString(R.string.card_moneylender_toast_message_win, player1.nickName, player2.nickName)
                }
                player1Card < player2Card -> {
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player1)

                    message = context.getString(R.string.card_moneylender_message, player1.nickName, player2.nickName)
                    toastMessage = context.getString(R.string.card_moneylender_toast_message_lose, player1.nickName, player2.nickName)


                }
                player1Card == player2Card -> {
                    message = context.getString(R.string.card_moneylender_message_tie, player1.nickName, player2.nickName)
                    toastMessage = context.getString(R.string.card_moneylender_toast_message_tie, player1.nickName, player2.nickName)

                }
            }
            updatedGameRoom.players.forEach {
                when (it.uid) {
                    player1.uid -> it.isAlive = player1.isAlive
                    player2.uid -> it.isAlive = player2.isAlive
                }
            }
            return Result(
                message = message,
                toastMessage = toastMessage,
                player1 = player1,
                player2 = player2,
                players = players,
                game = updatedGameRoom
            )
        }
    }
}