package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.CardAvatar
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

class Wiseguy {
    companion object {
        fun discardAndDraw(player1: Player, player2: Player, gameRoom: GameRoom, context: Context): Result {
            var updatedGameRoom = gameRoom
            var player2Card = context.getString(CardAvatar.setCardAvatar(player2.hand.first()).cardName)
            var message = context.getString(R.string.card_wiseguy_message, player1.nickName, player2.nickName, player2Card)
            var toastMessage = context.getString(R.string.card_wiseguy_toast_message, player1.nickName, player2.nickName, player2Card)
            val isPrincess = Darling.isPrincess(
                card = player2.hand.first(),
                player1 = player1,
                player2 = player2,
                gameRoom = gameRoom,
                context = context
            )
            if (isPrincess.message != "") {
                updatedGameRoom = isPrincess.game!!
                message = isPrincess.message
                toastMessage = isPrincess.toastMessage!!
            } else {
                updatedGameRoom.deck.discardDeck = GameRules.addToDiscardPile(player2.hand.first(), gameRoom)
            }

            if (gameRoom.deck.deck.isNotEmpty()) {
                val card = GameRules.drawCard(gameRoom = gameRoom)
                val players = gameRoom.players
                players.forEach {
                    if (it == player2) {
                        it.hand.clear()
                        if (it.isAlive) {
                            it.hand.add(card)
                            gameRoom.deck.deck =
                                GameRules.removeFromDeck(card = card, gameRoom = gameRoom)
                        }
                    }
                }
            } else {
                val players = gameRoom.players
                players.forEach {
                    if (it == player2) {
                        message = context.getString(R.string.card_wiseguy_message_empty_deck, player1.nickName, player2.nickName, player2Card)
                        toastMessage = context.getString(R.string.card_wiseguy_toast_message_empty_deck, player1.nickName, player2.nickName, player2Card)
                        updatedGameRoom = GameRules.eliminatePlayer(gameRoom, it)
                    }
                }
            }

            return Result(
                message = message,
                toastMessage = toastMessage,
                player1 = null,
                player2 = null,
                players = null,
                game = updatedGameRoom,
            )
        }
    }
}