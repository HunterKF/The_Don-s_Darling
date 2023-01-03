package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Guard {
    companion object {
        fun returnResult(player1: Player, player2: Player, guessedCard: Int, gameRoom: GameRoom): Result {
            var message = ""
            var updatedGameRoom = gameRoom
            if (player2.hand.first() == guessedCard) {
                updatedGameRoom = GameRules.eliminatePlayer(
                    gameRoom = gameRoom,
                    player = player2
                )
                message = "${player1.nickName} guessed correctly! ${player2.nickName} was eliminated."
            } else {
                message = "${player1.nickName} guessed $guessedCard, but ${player2.nickName} lives to tell the tale."
            }
            return Result(
                message = message,
                player1 = player1,
                player2 = player2,
                players = null,
                game = updatedGameRoom
            )
        }
    }
}