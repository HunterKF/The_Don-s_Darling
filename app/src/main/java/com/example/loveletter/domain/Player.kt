package com.example.loveletter.domain

data class Player(
    val avatar: Int?,
    var nickName: String,
    val uid: String
) {
    constructor(): this(0, "", "")
}