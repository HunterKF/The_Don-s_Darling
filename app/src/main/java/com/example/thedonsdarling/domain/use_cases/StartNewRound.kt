package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.models.UiText
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules

class StartNewRound {
    operator fun invoke(gameRoom: GameRoom): GameRoom {
        val turn = (0 until gameRoom.players.size).shuffled().random()
        gameRoom.players.forEach {
            it.hand.clear()
            it.isWinner = false
            it.turn = false
            it.turnInProgress = false
            it.protected = false
            it.isAlive = true
            it.turnOrder = 0
            it.ready = true
        }
        val gameLogs = gameRoom.gameLog.filter { it.type == "userMessage" || it.type == "serverMessage" || it.type == "winnerMessage" }

        val logMessage = LogMessage.createLogMessage(
            message = UiText.StringResource(R.string.new_round_message),
            toastMessage = null,
            uid = null,
            type = "serverMessage"
        )

        var game = GameRoom()
        gameLogs.forEach {
            game.gameLog.add(it)
        }
        game.gameLog.add(logMessage)
        game.gameLog.sortByDescending { it.date }
        game.roomNickname = gameRoom.roomNickname
        game.roomCode = gameRoom.roomCode
        game.playLimit = gameRoom.playLimit
        game.players = gameRoom.players
        game.players = GameRules.assignTurns(game.players, turn)
        game.roundOver = false
        game.gameOver = false
        gameRoom.start = true

        game.turn = turn
        game = GameRules.dealCards(game)
        //TODO - Make this return the new game and just call update from VM
        return game
    }
}