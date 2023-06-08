package com.example.thedonsdarling.data.gameserver.repository

import android.util.Log
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.models.FirestoreUser
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.JoinedGame
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.example.thedonsdarling.domain.util.user.HandleUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FireStoreRepositoryImpl(
    private val dbPlayers: CollectionReference,
    private val dbGame: CollectionReference
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
}