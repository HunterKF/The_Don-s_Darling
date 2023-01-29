package com.example.thedonsdarling.domain

class Deck(
    var deck: ArrayList<Int> = arrayListOf(
    ),
    var discardDeck: ArrayList<Int>,
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

