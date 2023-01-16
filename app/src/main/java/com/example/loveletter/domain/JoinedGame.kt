package com.example.loveletter.domain

data class JoinedGame(
    val roomCode: String,
    val roomNickname: String,
    var ready: Boolean
) {
    constructor() : this ("", "", false)
}
