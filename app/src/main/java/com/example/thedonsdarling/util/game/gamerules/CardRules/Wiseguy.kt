package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

sealed class Wiseguy {
    object ForcedToDiscard : Wiseguy()
    object ForcedToDiscardDarling: Wiseguy()
    object ForcedToDiscardAndEmptyDeck: Wiseguy()
    companion object {
        fun discardAndDraw(player1: Player, player2: Player, gameRoom: GameRoom): Result {
            var updatedGameRoom = gameRoom
            val currentGame = Darling.checkForDarling(
                card = player2.hand.first(),
                player1 = player1,
                player2 = player2,
                gameRoom = gameRoom
            )
            var wiseGuyMessage: Wiseguy = ForcedToDiscard

            when (currentGame.cardResult) {
                is Darling.PlayerSafe -> {
                    updatedGameRoom.deck.discardDeck = GameRules.addToDiscardPile(player2.hand.first(), gameRoom)
                }
                is Darling.PlayerEliminated -> {
                    wiseGuyMessage = ForcedToDiscardDarling
                    updatedGameRoom = currentGame.game!!
                }
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
                        wiseGuyMessage = ForcedToDiscardAndEmptyDeck
                        updatedGameRoom = GameRules.eliminatePlayer(gameRoom, it)
                    }
                }
            }

            return Result(
                cardResult = wiseGuyMessage,
                message = "",
                player1 = null,
                player2 = null,
                players = null,
                game = updatedGameRoom,
            )
        }
    }
}