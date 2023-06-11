package com.example.thedonsdarling.data.gameserver.repository

import android.util.Log
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.models.*
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FireStoreRepositoryImpl(
    private val dbPlayers: CollectionReference,
    private val dbGame: CollectionReference,
) : FireStoreRepository {
    override suspend fun addGameToPlayer(userId: String, gameRoom: GameRoom) {
        val joinedGame =
            JoinedGame(
                roomCode = gameRoom.roomCode,
                roomNickname = gameRoom.roomNickname,
                ready = false
            )

        joinedGame.ready = true
        dbPlayers.document(userId)
            .update("joinedGames", FieldValue.arrayUnion(joinedGame))
            .addOnSuccessListener {
                Log.d(TAG, "Updated the game list.")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to update game list. ${it.localizedMessage}")
            }
    }

    override suspend fun createUserPlayer(uid: String) {
        dbPlayers.document(uid).get()
            .addOnSuccessListener { result ->
                if (result.data == null || result.data.isNullOrEmpty()) {
                    dbPlayers.document(uid).set(
                        FirestoreUser(
                            uid = uid,
                            joinedGames = listOf()
                        )
                    )
                } else {
                    println("Firebase user already exists.")
                }

            }
    }

    override suspend fun deleteUserGameRoomForAll(
        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    ) {
        val joinedGame = JoinedGame(
            roomCode = roomCode, roomNickname = roomNickname,
            ready = true
        )

        players.forEach {
            dbPlayers.document(it.uid).update("joinedGames", FieldValue.arrayRemove(joinedGame))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added game to user's joined game list.")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add game to user list. ${it.localizedMessage}")
                }
        }
    }

    override suspend fun deleteUserGameRoomForLocal(
        roomCode: String,
        roomNickname: String,
        player: Player,
    ) {
        val joinedGame = JoinedGame(
            roomCode = roomCode, roomNickname = roomNickname,
            ready = true
        )

        dbPlayers.document(player.uid).update("joinedGames", FieldValue.arrayRemove(joinedGame))
            .addOnSuccessListener {
                Log.d(TAG, "Successfully added game to user's joined game list.")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add game to user list. ${it.localizedMessage}")
            }
    }

    override suspend fun observeMyGames(currentUser: FirebaseUser): Flow<FirestoreUser> {
        return callbackFlow {
            var joinedGameList = FirestoreUser(
                "",
                listOf(),
            )
            currentUser.let {
                val listener = dbPlayers.document(currentUser.uid)
                    .addSnapshotListener { documentSnapshot, exception ->
                        exception?.let {
                            Log.d(
                                TAG,
                                "An error has occurred trying to observe my games: $exception"
                            )
                            return@addSnapshotListener
                        }
                        Log.d(TAG, "Attempting to get the document")
                        documentSnapshot?.let {
                            val firestoreUser = it.toObject(FirestoreUser::class.java)
                            Log.d(TAG, "Document grabbed... $firestoreUser")

                            firestoreUser?.let {
                                joinedGameList = firestoreUser
                                Log.d(TAG, "Changing it out $joinedGameList")

                            }
                        }
                        trySend(joinedGameList)
                    }
                awaitClose {
                    listener.remove()
                }
            }
        }
    }

    override suspend fun deleteRoomFromFireStore(gameRoom: GameRoom) {
        dbGame.document(gameRoom.roomCode)
            .get()
            .addOnSuccessListener { result ->
                result.reference.delete()
            }
            .addOnFailureListener {
                println("Failure...")
            }
    }

    override suspend fun removePlayerFromGame(roomCode: String, player: Player) {
        dbGame.document(roomCode)
            .update("players", FieldValue.arrayRemove(player))
            .addOnSuccessListener {
                Log.d(TAG, "Successfully left game!")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to leave game! ${it.localizedMessage}")
            }
    }

    override suspend fun removeSingleGameFromPlayerJoinedList(
        roomCode: String,
        player: Player,
        roomNickname: String,
    ) {
        val joinedGame = JoinedGame(
            roomCode = roomCode, roomNickname = roomNickname,
            ready = true
        )
        dbPlayers.document(player.uid).update("joinedGames", FieldValue.arrayRemove(joinedGame))
            .addOnSuccessListener {
                Log.d(TAG, "Successfully added game to user's joined game list.")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to add game to user list. ${it.localizedMessage}")
            }
    }

    override suspend fun removeSingleGameFromAllPlayersJoinedGames(
        roomCode: String,
        roomNickname: String,
        players: List<Player>,
    ) {
        val joinedGame = JoinedGame(
            roomCode = roomCode, roomNickname = roomNickname,
            ready = true
        )

        players.forEach {
            dbPlayers.document(it.uid).update("joinedGames", FieldValue.arrayRemove(joinedGame))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added game to user's joined game list.")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add game to user list. ${it.localizedMessage}")
                }
        }
    }

    override suspend fun setGameInDB_update(gameRoom: GameRoom) {
        dbGame.document(gameRoom.roomCode).set(gameRoom)
            .addOnSuccessListener {
//                    Log.d(GAMERULES_TAG, "Successfully updated game room")
            }
            .addOnFailureListener {
//                    Log.d(GAMERULES_TAG, "Failed to update room: ${it.localizedMessage}")
            }
    }

    override suspend fun subscribeToRealtimeUpdates(roomCode: String): Flow<GameRoom> {
        return callbackFlow {
            var room = GameRoom()
            val listener = dbGame.document(roomCode)
                .addSnapshotListener { querySnapshot, exception ->
                    exception?.let {
                        println(exception.localizedMessage)
                        return@addSnapshotListener
                    }
                    querySnapshot?.let {
                        val updatedRoom = it.toObject(GameRoom::class.java)
                        updatedRoom?.let {
                            room = updatedRoom
                        }

                    }
                    trySend(room)
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override suspend fun checkGame(
        roomCode: String,
    ): CheckGameResult {
        return try {
            val documentSnapshot = dbGame.document(roomCode).get().await()
            if (documentSnapshot.data == null) {
                Log.d(TAG, "Room code not found. Failure.")
                CheckGameResult.GameNotFound
            } else {
                Log.d(TAG, "Room code found. Success.")
                CheckGameResult.GameFound
            }
        } catch (e: Exception) {
            CheckGameResult.GameNotFound
        }
    }

    override suspend fun getAndReturnGame(roomCode: String, player: Player): GameRoom? {
        return try {
            val result = dbGame.document(roomCode).get().await()
            if (result.data == null) {
                null
            } else {
                val gameRoom = result.toObject(GameRoom::class.java)
                gameRoom?.let {
                    gameRoom
                }
            }
        } catch (e: Exception) {
           null
        }

    }

    override suspend fun addPlayerToGame(roomCode: String, player: Player) {
        dbGame.document(roomCode)
            .update("players", FieldValue.arrayUnion(player))
            .await()
        Log.d(TAG, "Successfully added player!")
    }

    override suspend fun updatePlayers(gameRoom: GameRoom, uid: String) {
        dbGame.document(gameRoom.roomCode)
            .update("players", gameRoom.players)
            .addOnSuccessListener {
//                    Log.d(TAG, "Successfully updated player's unread status")
            }
            .addOnFailureListener {
//                    Log.d(TAG, "Failed to update player's unread status. ${it.localizedMessage}")

            }
    }

    override suspend fun sendMessage(gameRoom: GameRoom, logMessage: LogMessage) {
        dbGame.document(gameRoom.roomCode)
            .update("gameLog", FieldValue.arrayUnion(logMessage))
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }




}