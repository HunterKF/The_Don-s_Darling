package com.example.loveletter.util.game.gamerules.CardRules

import android.util.Log
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Moneylender {
    companion object {
        fun compareCards(player1: Player, player2: Player, players: List<Player>, game: GameRoom): Result {
            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()
            var message = ""
            var toastMessage = ""
            var updatedGameRoom = game
            when {
                player1Card > player2Card -> {
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player2)
                    message = "${player1.nickName} compared hands with ${player2.nickName} using the Moneylender! ${player1.nickName}'s hand was better. ${player2.nickName} was eliminated."
                    toastMessage = "${player1.nickName} used the Moneylender and eliminated ${player2.nickName}."
                }
                player1Card < player2Card -> {
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player1)

                    message = "${player1.nickName} compared hands with ${player2.nickName} using the Moneylender! ${player2.nickName}'s hand was better. ${player1.nickName} was eliminated."
                    toastMessage = "${player1.nickName} used the Moneylender but was eliminated by ${player2.nickName}."


                }
                player1Card == player2Card -> {
                    message = "${player1.nickName} used the Moneylender to compare hands with ${player2.nickName}. But no one died? ${player1.nickName} and ${player2.nickName} live to tell another tale."
                    toastMessage = "${player1.nickName} used the Moneylender with ${player2.nickName}, but it was a tie."

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
                toastMessage = toastMessage,
                player1 = player1,
                player2 = player2,
                players = players,
                game = updatedGameRoom
            )
        }
    }
}