package com.example.thedonsdarling.domain.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.CardResult
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules

sealed class Moneylender {
    object Player1Wins : Moneylender()
    object Player2Wins : Moneylender()
    object Draw : Moneylender()
    companion object {
        fun compareCards(
            player1: Player,
            player2: Player,
            players: List<Player>,
            game: GameRoom
        ): CardResult {
            val player1Card = player1.hand.first()
            val player2Card = player2.hand.first()
            var updatedGameRoom = game

            var comparisonResult: Moneylender = Draw
            when {
                player1Card > player2Card -> {
                    comparisonResult = Player1Wins
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player2)
                }
                player1Card < player2Card -> {
                    comparisonResult = Player2Wins
                    updatedGameRoom = GameRules.eliminatePlayer(updatedGameRoom, player1)
                }
                player1Card == player2Card -> {
                    comparisonResult = Draw
                }
            }
            updatedGameRoom.players.forEach {
                when (it.uid) {
                    player1.uid -> it.isAlive = player1.isAlive
                    player2.uid -> it.isAlive = player2.isAlive
                }
            }
            return CardResult(
                cardResult = comparisonResult,
                message = "",
                player1 = player1,
                player2 = player2,
                players = players,
                game = updatedGameRoom
            )
        }
    }
}