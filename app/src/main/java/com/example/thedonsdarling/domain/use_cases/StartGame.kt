package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules

class StartGame {
    operator fun invoke(
        gameRoom: GameRoom,
        logMessage: LogMessage
    ): GameRoom {
        /*val logMessage = LogMessage.createLogMessage(
            context.getString(R.string.start_game_message),
            toastMessage = null,
            null,
            "serverMessage"
        )*/
        var updatedGameRoom = gameRoom
        updatedGameRoom.gameLog.add(logMessage)
        val size = gameRoom.players.size -1
        val turn = (0..size).shuffled().random()
        gameRoom.players = GameRules.assignTurns(gameRoom.players, gameTurn = turn)
        gameRoom.turn = turn

        gameRoom.start = true

        updatedGameRoom = GameRules.dealCards(gameRoom)

        return updatedGameRoom
    }
}