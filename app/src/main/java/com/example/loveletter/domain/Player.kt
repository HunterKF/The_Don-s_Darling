package com.example.loveletter.domain

data class Player(
    val avatar: Int?,
    var nickName: String,
    val uid: String,
    val ready: Boolean,
    val turn: Boolean,
    val turnOrder: Int,
    val hand: List<Int>?,
) {
    constructor() : this(0, "Hunter",
        "NPc8lG4p91VoCTC8GgMdxpcvUdx2", false, turn = false, turnOrder = 0, hand = listOf())
}