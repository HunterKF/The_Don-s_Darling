package com.example.thedonsdarling.domain.models

import java.util.*

data class LogMessage(
    var message: UiText,
    var toastMessage: UiText?,
    val type: String,
    var uid: String?,
    val date: Date,
) {
    constructor(): this(
        UiText.DynamicString(""),
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
        fun createLogMessage(message: UiText, toastMessage: UiText?, uid: String?, type: String): LogMessage {

            return LogMessage(
                message = message,
                toastMessage = toastMessage,
                type = type,
                uid = uid,
                date = Date()
            )
        }

    }

}

