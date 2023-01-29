package com.example.thedonsdarling.domain

data class FirestoreUser(
    val uid: String,
    val joinedGames: List<JoinedGame>
) {
    constructor() : this("", listOf())
}
