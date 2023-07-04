package com.example.thedonsdarling.domain.models

import java.util.*

data class LogMessage(
    var chatMessage: String?,
    var gameMessage: GameMessage?,
    val type: String,
    var uid: String?,
    val date: Date,
) {
    constructor() : this(
        null,
        null,
        "",
        null,
        Date()
    )

    /*Log Message Types:
    * gameLog
    * serverMessage
    * userMessage*/
    companion object {
        fun createLogMessage(
            chatMessage: String?,
            gameMessage: GameMessage?,
            uid: String?,
            type: String,
        ): LogMessage {

            return LogMessage().copy(
                chatMessage = chatMessage,
                gameMessage = gameMessage,
                type = type,
                uid = uid,
                date = Date()
            )
        }

    }

}

