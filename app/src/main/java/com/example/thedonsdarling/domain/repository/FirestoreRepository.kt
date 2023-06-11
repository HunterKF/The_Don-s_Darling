package com.example.thedonsdarling.domain.repository

import com.example.thedonsdarling.domain.models.*
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
    suspend fun deleteRoomFromFireStore(gameRoom: GameRoom)
    suspend fun removePlayerFromGame(roomCode: String, player: Player)
    suspend fun removeSingleGameFromPlayerJoinedList(
        roomCode: String,
        player: Player,
        roomNickname: String,
    )

    suspend fun removeSingleGameFromAllPlayersJoinedGames(
        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    )

    suspend fun setGameInDB_update(gameRoom: GameRoom)

    suspend fun subscribeToRealtimeUpdates(roomCode: String): Flow<GameRoom>

    suspend fun checkGame(
        roomCode: String
    ): CheckGameResult

    suspend fun getAndReturnGame(roomCode: String, player: Player) : GameRoom?
    suspend fun addPlayerToGame(roomCode: String, player: Player)

    suspend fun updatePlayers(gameRoom: GameRoom, uid: String)
    suspend fun sendMessage(gameRoom: GameRoom, logMessage: LogMessage)
}