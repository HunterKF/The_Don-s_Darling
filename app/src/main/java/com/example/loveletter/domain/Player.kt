package com.example.loveletter.domain

data class Player(
    val avatar: Int?,
    var nickName: String,
    val uid: String,
    val ready: Boolean,
    val isTurn: Boolean,
    val turnOrder: Int,
) {
    constructor() : this(0, "Hunter", "", false, isTurn = false, turnOrder = 0)
}