package com.example.loveletter.presentation.mygames

import com.example.loveletter.domain.FirestoreUser
import com.example.loveletter.domain.JoinedGame

sealed class MyGamesState {
    object Loading : MyGamesState()
    data class Loaded(
        val firestoreUser: FirestoreUser
    ) : MyGamesState()
}