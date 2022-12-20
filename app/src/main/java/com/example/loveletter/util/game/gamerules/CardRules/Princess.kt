package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Princess {
    companion object {
        fun eliminatePlayer(gameRoom: GameRoom, player: Player): Result {
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                }
            }
            return Result(
                message = "The Princess has been played. ${player.nickName} has been eliminated.",
                player1 = player,
                player2 = null,
                players = null,
                game = gameRoom
            )
        }

        fun isPrincess(card: Int,player1: Player, player2: Player, gameRoom: GameRoom): Result{
            var message = ""
            if (card == 8) {
                message = "${player1.nickName} forced ${player2.nickName} to discard his hand. ${player2} has been eliminated."
                gameRoom.players.forEach {
                    if (it.uid == player2.uid) {
                        it.isAlive = false
                    }
                }
            }

            return Result(
                message = message,
                player1 = player1,
                player2 = player2,
                players = null,
                game = gameRoom
            )
        }
    }
}