package com.example.loveletter.util.user

import android.util.Log
import com.example.loveletter.TAG
import com.example.loveletter.dbPlayers
import com.example.loveletter.domain.FirestoreUser
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.JoinedGame
import com.example.loveletter.domain.Player
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HandleUser {
    companion object {
        private val currentUser = Firebase.auth.currentUser
        fun createGamePlayer(avatar: Int, nickname: String): Player {
            return Player(
                avatar = avatar,
                nickName = nickname,
                uid = currentUser.uid,
                ready = false,
                turn = false,
                turnOrder = 0,
                hand = arrayListOf(),
                isHost = false
            )
        }

        fun createUserPlayer() {
            dbPlayers.document(currentUser.uid).get()
                .addOnSuccessListener { result ->
                    if (result.data == null || result.data.isNullOrEmpty()) {
                        dbPlayers.document(currentUser.uid).set(
                            FirestoreUser(
                                uid = currentUser.uid,
                                joinedGames = listOf()
                            )
                        )
                    } else {
                        println("Firebase user already exists.")
                    }

                }
        }

        fun addGameToUser(roomCode: String, roomNickname: String) {
            val joinedGame =
                JoinedGame(roomCode = roomCode, roomNickname = roomNickname, false, false)
            dbPlayers.document(currentUser.uid)
                .update("joinedGames", FieldValue.arrayUnion(joinedGame))
                .addOnSuccessListener {
                    println("Successfully added game to user's joined game list.")
                }
                .addOnFailureListener {
                    println("Failed to add game to user list. ${it.localizedMessage}")
                }
        }

        fun addGameToPlayer(userId: String, gameRoom: GameRoom) {
            val joinedGame =
                JoinedGame(roomCode = gameRoom.roomCode,
                    roomNickname = gameRoom.roomNickname,
                    false,
                    ready = false)

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


        fun returnUser(): FirebaseUser? {
            return currentUser
        }

        fun deleteUserGameRoom(roomCode: String, roomNickname: String, players: List<Player>) {
            val joinedGame =
                JoinedGame(roomCode = roomCode, roomNickname = roomNickname, false, false)

            players.forEach {
                dbPlayers.document(it.uid).update("joinedGames", FieldValue.arrayRemove(joinedGame))
                    .addOnSuccessListener {
                        println("Successfully added game to user's joined game list.")
                    }
                    .addOnFailureListener {
                        println("Failed to add game to user list. ${it.localizedMessage}")
                    }
            }

        }

        fun observeMyGames(): Flow<FirestoreUser> {
            return callbackFlow {
                var joinedGameList = FirestoreUser(
                    "",
                    listOf(),
                )
                val listener = dbPlayers.document(currentUser.uid)
                    .addSnapshotListener { documentSnapshot, exception ->
                        exception?.let {
                            Log.d(TAG,
                                "An error has occurred trying to observe my games: $exception")
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

        fun getCurrentUser(list: List<Player>, currentUser: String): Player {
            var currentPlayer = Player()
            list.forEach {
                if (it.uid == currentUser) {
                    currentPlayer = it
                }
            }
            return currentPlayer
        }
    }
}