package com.example.thedonsdarling.data.gameserver.repository

import com.example.thedonsdarling.domain.models.*
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFireStoreRepositoryImpl : FireStoreRepository {

    private val testingGameRoom = MutableStateFlow(GameRoom())

    private var checkGameResult: CheckGameResult = CheckGameResult.GameFound

    fun setCheckGameResult(value: CheckGameResult) {
        checkGameResult = value
    }

    override suspend fun addGameToPlayer(userId: String, gameRoom: GameRoom) {
        TODO("Not yet implemented")
    }

    override suspend fun createUserPlayer(uid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserGameRoomForAll(
        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserGameRoomForLocal(
        roomCode: String,
        roomNickname: String,
        player: Player,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun observeMyGames(currentUser: FirebaseUser): Flow<FirestoreUser> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoomFromFireStore(gameRoom: GameRoom) {
        println("It was deleted from FireStore!")
    }

    override suspend fun removePlayerFromGame(roomCode: String, player: Player) {
        testingGameRoom.value.players = testingGameRoom.value.players.minus(player)
    }

    override suspend fun removeSingleGameFromPlayerJoinedList(
        roomCode: String,
        player: Player,
        roomNickname: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeSingleGameFromAllPlayersJoinedGames(
        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun setGameInDB_update(gameRoom: GameRoom) {
        testingGameRoom.value = gameRoom
    }

    override suspend fun subscribeToRealtimeUpdates(roomCode: String): Flow<GameRoom> {
        return testingGameRoom
    }

    override suspend fun checkGame(roomCode: String): CheckGameResult {
        TODO("Not yet implemented")
    }

    override suspend fun getAndReturnGame(roomCode: String, player: Player): GameRoom? {
        return testingGameRoom.value
    }

    override suspend fun addPlayerToGame(roomCode: String, player: Player) {
        testingGameRoom.value.players = testingGameRoom.value.players.plus(player)
    }

    override suspend fun updatePlayers(gameRoom: GameRoom, uid: String) {
        testingGameRoom.value.players = gameRoom.players

    }

    override suspend fun sendMessage(gameRoom: GameRoom, logMessage: LogMessage) {
        println("Send Message has occurred!")
    }

    override fun returnUid(): String? {
        return "Uid!"
    }
}