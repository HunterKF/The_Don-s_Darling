package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.GameMessage
import com.example.thedonsdarling.domain.models.GameMessageType
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules

class StartNewGame {
    operator fun invoke(gameRoom: GameRoom): GameRoom{
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
            it.wins = 0
        }
        val gameLogs = gameRoom.gameLog.filter { it.type == "userMessage" || it.type == "serverMessage" || it.type == "winnerMessage" }

        val logMessage = LogMessage.createLogMessage(
            chatMessage = null,
            gameMessage = GameMessage(
                gameMessageType = GameMessageType.NewRound.messageType,
                players = null,
                player1 = null,
                player2 = null
            ),
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
        return game
    }
}