package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.CardAvatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Wiseguy {
    companion object {
        fun discardAndDraw(player1: Player, player2: Player, gameRoom: GameRoom): Result {
            var updatedGameRoom = gameRoom
            var player2Card = CardAvatar.setCardAvatar(player2.hand.first())
            var message = "${player1.nickName} played the Wiseguy. ${player1.nickName} forced ${player2.nickName} to discard their ${player2Card.cardName}."
            var toastMessage = "${player1.nickName} played the Wiseguy, forcing ${player2.nickName} to discard ${player2Card.cardName}"
            val isPrincess = Darling.isPrincess(
                card = player2.hand.first(),
                player1 = player1,
                player2 = player2,
                gameRoom = gameRoom
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
                        message = "${player1.nickName} played the Wiseguy. ${player1.nickName} forced ${player2.nickName} to discard their ${player2Card.cardName}. There are no more cards. ${player2.nickName} was eliminated."
                        toastMessage = "${player1.nickName} played the Wiseguy, forcing ${player2.nickName} to discard ${player2Card.cardName}. ${player2.nickName} was eliminated."
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