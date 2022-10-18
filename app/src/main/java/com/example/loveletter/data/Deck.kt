package com.example.loveletter.data

class Deck(
    val guard: Int = 1,
    val priest: Int = 2,
    val baron: Int = 3,
    val handmaid: Int = 4,
    val prince: Int = 5,
    val king: Int = 6,
    val countess: Int = 7,
    val princess: Int = 8,
    val deck: List<Int> = listOf(
        guard,
        guard,
        guard,
        guard,
        guard,
        priest,
        priest,
        baron,
        baron,
        handmaid,
        handmaid,
        prince,
        prince,
        king,
        countess,
        princess
    ),
    val deckSize: Int = deck.size
)

