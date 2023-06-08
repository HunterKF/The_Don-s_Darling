package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.FirestoreUser
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class ObserveMyGames(
    private val repository: FireStoreRepository
) {
    suspend operator fun invoke(currentUser: FirebaseUser): Flow<FirestoreUser> {
        return repository.observeMyGames(currentUser)
    }
}