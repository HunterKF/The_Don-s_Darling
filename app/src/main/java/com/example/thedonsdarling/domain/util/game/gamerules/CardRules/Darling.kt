package com.example.thedonsdarling.domain.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.CardResult
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules

sealed class Darling {
    object PlayerSafe : Darling()
    object PlayerEliminated : Darling()
    companion object {
        fun eliminatePlayer(gameRoom: GameRoom, player: Player): CardResult {
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                }
            }
            return CardResult(
                cardResult = PlayerEliminated,
                message = "",
                player1 = player,
                player2 = null,
                players = null,
                game = gameRoom
            )
        }

        fun checkForDarling(
            card: Int,
            player1: Player,
            player2: Player,
            gameRoom: GameRoom,
        ): CardResult {
            var message = ""
            var toastMessage = ""
            if (card == 8) {
                return CardResult(
                    cardResult = PlayerEliminated,
                    message = "",
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = GameRules.eliminatePlayer(gameRoom = gameRoom, player = player2)
                )
            } else {
                return CardResult(
                    cardResult = PlayerSafe,
                    message = message,
                    toastMessage = toastMessage,
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = gameRoom
                )
            }


        }
    }
}