package com.example.loveletter.domain

class Deck(
    val deck: ArrayList<Int> = arrayListOf(
        1,
        1,
        1,
        1,
        1,
        2,
        2,
        3,
        3,
        4,
        4,
        5,
        5,
        6,
        7,
        8
    ),
    val discardDeck: ArrayList<Int>?,
) {
    constructor() : this(
        arrayListOf<Int>(
            1,
            1,
            1,
            1,
            1,
            2,
            2,
            3,
            3,
            4,
            4,
            5,
            5,
            6,
            7,
            8),
        arrayListOf()
    )
}

