package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.CardAvatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.gamerules.GameRules

class Policeman {
    companion object {
        fun returnResult(player1: Player, player2: Player, guessedCard: Int, gameRoom: GameRoom): Result {
            var message = ""
            var toastMessage = ""
            var updatedGameRoom = gameRoom
            var guessCard = CardAvatar.setCardAvatar(guessedCard).cardName
            if (player2.hand.first() == guessedCard) {
                updatedGameRoom = GameRules.eliminatePlayer(
                    gameRoom = gameRoom,
                    player = player2
                )
                message = "${player1.nickName} played the Policeman. They guessed ${player2.nickName} has a $guessCard. ${player1.nickName} was right!  ${player2.nickName} was eliminated."
                toastMessage = "${player1.nickName} guessed correctly that ${player2.nickName} had a $guessCard."
            } else {
                message = "${player1.nickName} played the Policeman. They guessed ${player2.nickName} has a $guessCard. ${player1.nickName} was wrong!"
                toastMessage = "${player1.nickName} guessed incorrectly that ${player2.nickName} has a $guessCard."
            }
            return Result(
                message = message,
                toastMessage = toastMessage,
                player1 = player1,
                player2 = player2,
                players = null,
                game = updatedGameRoom
            )
        }
    }
}