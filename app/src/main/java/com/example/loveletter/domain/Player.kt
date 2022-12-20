package com.example.loveletter.domain

data class Player(
    val avatar: Int?,
    var nickName: String,
    val uid: String,
    var ready: Boolean,
    var protected: Boolean,
    var turn: Boolean,
    var turnInProgress: Boolean,
    var turnOrder: Int,
    var hand: ArrayList<Int>,
    var isHost: Boolean,
    var isAlive: Boolean,
    var isWinner: Boolean,
    var wins: Int,
) {
    constructor() : this(
        avatar = 0,
        nickName = "",
        uid = "",
        ready = false,
        protected = false,
        turn = false,
        turnInProgress = false,
        turnOrder = 0, hand = arrayListOf(),
        isHost = false,
        isAlive = false,
        isWinner = false,
        wins =0
    )
}