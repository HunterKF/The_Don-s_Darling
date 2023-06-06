package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

sealed class Policeman {
    object CorrectGuess : Policeman()
    object WrongGuess : Policeman()
    companion object {
        fun returnResult(
            player1: Player,
            player2: Player,
            guessedCard: Int,
            gameRoom: GameRoom,
        ): Result {
            var updatedGameRoom = gameRoom
            if (player2.hand.first() == guessedCard) {
                updatedGameRoom = GameRules.eliminatePlayer(
                    gameRoom = gameRoom,
                    player = player2
                )
                return Result(
                    cardResult = CorrectGuess,
                    message = "",
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = updatedGameRoom
                )
            } else {
                return Result(
                    cardResult = WrongGuess,
                    message = "",
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = updatedGameRoom
                )
            }

        }
    }
}