package com.example.thedonsdarling.domain.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.CardResult
import com.example.thedonsdarling.domain.models.GameMessageType
import com.example.thedonsdarling.domain.models.UiText
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
                message = GameMessageType.Darling,
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
            if (card == 8) {
                return CardResult(
                    cardResult = PlayerEliminated,
                    message = GameMessageType.WiseGuyForcedToDiscardDarling,
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = GameRules.eliminatePlayer(gameRoom = gameRoom, player = player2)
                )
            } else {
                return CardResult(
                    cardResult = PlayerSafe,
                    message = null,
                    player1 = player1,
                    player2 = player2,
                    players = null,
                    game = gameRoom
                )
            }


        }
    }
}