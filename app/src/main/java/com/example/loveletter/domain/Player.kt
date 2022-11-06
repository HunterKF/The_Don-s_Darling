package com.example.loveletter.domain

data class Player(
    val avatar: Int?,
    var nickName: String,
    val uid: String,
    val ready: Boolean,
    var turn: Boolean,
    var turnOrder: Int,
    val hand: ArrayList<Int>,
) {
    constructor() : this(0, "Hunter",
        "", false, turn = false, turnOrder = 0, hand = arrayListOf())
}