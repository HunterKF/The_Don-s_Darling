package com.example.thedonsdarling.domain.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.CardResult
import com.example.thedonsdarling.domain.models.UiText
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules

sealed class Policeman {
    object CorrectGuess : Policeman()
    object WrongGuess : Policeman()
    companion object {
        fun returnResult(
            player1: Player,
            player2: Player,
            guessedCard: Int,
            gameRoom: GameRoom,
        ): CardResult {
            var updatedGameRoom = gameRoom
            if (player2.hand.first() == guessedCard) {
                updatedGameRoom = GameRules.eliminatePlayer(
                    gameRoom = gameRoom,
                    player = player2
                )
                return CardResult(
                    cardResult = CorrectGuess,
                    message = UiText.DynamicString(""),
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = updatedGameRoom
                )
            } else {
                return CardResult(
                    cardResult = WrongGuess,
                    message = UiText.DynamicString(""),
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = updatedGameRoom
                )
            }

        }
    }
}