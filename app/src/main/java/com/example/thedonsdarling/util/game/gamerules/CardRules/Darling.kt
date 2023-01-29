package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

class Darling {
    companion object {
        fun eliminatePlayer(gameRoom: GameRoom, player: Player, context: Context): Result {
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                }
            }
            return Result(
                message = context.getString(R.string.card_darling_message, player.nickName),
                toastMessage = context.getString(R.string.card_darling_toast_message, player.nickName),
                player1 = player,
                player2 = null,
                players = null,
                game = gameRoom
            )
        }

        fun isPrincess(card: Int,player1: Player, player2: Player, gameRoom: GameRoom, context: Context): Result{
            var message = ""
            var toastMessage = ""
            var result = Result(
                message = message,
                toastMessage = "",
                player1 = player1,
                player2 = player2,
                players = null,
                game = gameRoom
            )
            if (card == 8) {
                message = context.getString(R.string.card_darling_forced_message, player1.nickName, player2.nickName)
                toastMessage = context.getString(R.string.card_darling_forced_toast_message, player1.nickName, player2.nickName)
                result.game = GameRules.eliminatePlayer(gameRoom = gameRoom, player = player2)
            }

            return Result(
                message = message,
                toastMessage = toastMessage,
                player1 = player1,
                player2 = player2,
                players = null,
                game = result.game
            )
        }
    }
}