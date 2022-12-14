package com.example.loveletter.domain

data class Player(
    val avatar: Int?,
    var nickName: String,
    val uid: String,
    var ready: Boolean,
    var turn: Boolean,
    var turnOrder: Int,
    val hand: ArrayList<Int>,
    var isHost: Boolean,
    var isAlive: Boolean,
    var isWinner: Boolean,
    var wins: Int
) {
    constructor() : this(0, "",
        "", false, turn = false, turnOrder = 0, hand = arrayListOf(), isHost = false, isAlive = false, isWinner = false, 0)
}