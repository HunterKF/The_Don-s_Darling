package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.CardAvatar
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

class Policeman {
    companion object {
        fun returnResult(player1: Player, player2: Player, guessedCard: Int, gameRoom: GameRoom, context: Context): Result {
            var message: String
            var toastMessage: String
            var updatedGameRoom = gameRoom
            var guessCard = context.getString(CardAvatar.setCardAvatar(guessedCard).cardName)
            if (player2.hand.first() == guessedCard) {
                updatedGameRoom = GameRules.eliminatePlayer(
                    gameRoom = gameRoom,
                    player = player2
                )
                message = context.getString(R.string.card_policemen_message_correct, player1.nickName, player2.nickName, guessCard)
                toastMessage = context.getString(R.string.card_policemen_toast_message_correct, player1.nickName, player2.nickName, guessCard)
            } else {
                message = context.getString(R.string.card_policemen_message_incorrect, player1.nickName, player2.nickName, guessCard)
                toastMessage = context.getString(R.string.card_policemen_toast_message_incorrect, player1.nickName, player2.nickName, guessCard)
            }
            return Result(
                message = message,
                toastMessage = toastMessage,
                player1 = player1,
                player2 = player2,
                players = null,
                game = updatedGameRoom
            )
        }
    }
}