package com.example.thedonsdarling.domain.repository

import com.example.thedonsdarling.domain.models.FirestoreUser
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {
    suspend fun addGameToPlayer(userId: String, gameRoom: GameRoom)
    suspend fun createUserPlayer(uid: String)
    suspend fun deleteUserGameRoomForAll(
        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    )
    suspend fun deleteUserGameRoomForLocal(
        roomCode: String,
        roomNickname: String,
        player: Player,
    )
    suspend fun observeMyGames(currentUser: FirebaseUser): Flow<FirestoreUser>

}