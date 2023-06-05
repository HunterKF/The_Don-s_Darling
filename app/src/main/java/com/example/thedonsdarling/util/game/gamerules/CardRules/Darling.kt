package com.example.thedonsdarling.util.game.gamerules.CardRules

import android.content.Context
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.domain.Result
import com.example.thedonsdarling.util.game.gamerules.GameRules

sealed class Darling {
    object PlayerSafe : Darling()
    object PlayerEliminated : Darling()
    companion object {
        fun eliminatePlayer(gameRoom: GameRoom, player: Player): Result {
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                }
            }
            return Result(
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
        ): Result {
            var message = ""
            var toastMessage = ""
            if (card == 8) {
                return Result(
                    cardResult = PlayerEliminated,
                    message = "",
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = GameRules.eliminatePlayer(gameRoom = gameRoom, player = player2)
                )
            } else {
                return Result(
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