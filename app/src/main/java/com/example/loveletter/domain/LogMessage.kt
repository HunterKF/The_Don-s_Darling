package com.example.loveletter.domain

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

data class LogMessage(
    var message: String,
    val type: String,
    var uid: String?,
    val date: Date,
) {
    constructor(): this(
        "",
        "",
        null,
        Date()
    )
    companion object {
        fun createLogMessage(message: String, uid: String?, type: String): LogMessage {

            return LogMessage(
                message = message,
                type = type,
                uid = uid,
                date = Date()
            )
        }

    }

}

