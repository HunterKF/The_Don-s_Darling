package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Prince {
    companion object {
        fun discardAndDraw(player1: Player, player2: Player, gameRoom: GameRoom): Result {
            gameRoom.deck.discardDeck = GameRules.addToDiscardPile(player2.hand.first(), gameRoom)

            val card = GameRules.drawCard(gameRoom = gameRoom)
            val players = gameRoom.players
            players.forEach {
                if (it == player2) {
                    it.hand.clear()
                    it.hand.add(card)
                    gameRoom.deck.deck = GameRules.removeFromDeck(card = card, gameRoom = gameRoom)
                }
            }
            return Result(
                message = "${player1.nickName} forced ${player2.nickName} to discard his hand.",
                player1 = null,
                player2 = null,
                players = null,
                game = gameRoom,
            )
        }
    }
}