package com.example.thedonsdarling.presentation.mygames

import com.example.thedonsdarling.domain.models.FirestoreUser

sealed class MyGamesState {
    object Loading : MyGamesState()
    data class Loaded(
        val firestoreUser: FirestoreUser
    ) : MyGamesState()
}