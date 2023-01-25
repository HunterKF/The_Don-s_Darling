package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Darling {
    companion object {
        fun eliminatePlayer(gameRoom: GameRoom, player: Player): Result {
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                }
            }
            return Result(
                message = "Darling was played. ${player.nickName} has been eliminated.",
                toastMessage = "Darling was played. ${player.nickName} has been eliminated.",
                player1 = player,
                player2 = null,
                players = null,
                game = gameRoom
            )
        }

        fun isPrincess(card: Int,player1: Player, player2: Player, gameRoom: GameRoom): Result{
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
                message = "${player1.nickName} forced ${player2.nickName} to discard his hand. ${player2.nickName} had Darling! ${player2.nickName} has been eliminated."
                toastMessage = "${player1.nickName} forced ${player2.nickName} to discard Darling. ${player2.nickName} is knocked out."
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