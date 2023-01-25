package com.example.loveletter.domain

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

data class LogMessage(
    var message: String,
    var toastMessage: String?,
    val type: String,
    var uid: String?,
    val date: Date,
) {
    constructor(): this(
        "",
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
        fun createLogMessage(message: String, toastMessage: String?, uid: String?, type: String): LogMessage {

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

