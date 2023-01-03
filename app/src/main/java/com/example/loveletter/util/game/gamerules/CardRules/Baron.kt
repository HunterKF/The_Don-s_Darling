package com.example.loveletter.util.game.gamerules.CardRules

import android.util.Log
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Baron {
    companion object {
        fun compareCards(player1: Player, player2: Player, players: List<Player>, game: GameRoom): Result {
            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()
            var message = ""
            var updatedGameRoom = game
            when {
                player1Card > player2Card -> {
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player2)
                    message = "${player1.nickName} wins! ${player2.nickName} was eliminated. The cards were $player1Card and $player2Card"
                }
                player1Card < player2Card -> {
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player1)

                    message = "${player2.nickName} wins! ${player1.nickName} was eliminated. The cards were $player1Card and $player2Card"
                }
                player1Card == player2Card -> {
                    message =
                        "No one died? ${player1.nickName} and ${player2.nickName} live to tell another tale. The cards were $player1Card and $player2Card"
                }
            }
            updatedGameRoom.players.forEach {
                when (it.uid) {
                    player1.uid -> it.isAlive = player1.isAlive
                    player2.uid -> it.isAlive = player2.isAlive
                }
            }
            return Result(
                message = message,
                player1 = player1,
                player2 = player2,
                players = players,
                game = updatedGameRoom
            )
        }
    }
}