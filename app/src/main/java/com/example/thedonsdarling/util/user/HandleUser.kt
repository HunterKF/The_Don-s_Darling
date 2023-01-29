package com.example.thedonsdarling.util.user

import android.util.Log
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.dbGame
import com.example.thedonsdarling.dbPlayers
import com.example.thedonsdarling.domain.FirestoreUser
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.JoinedGame
import com.example.thedonsdarling.domain.Player
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
        fun createGamePlayer(avatar: Int, nickname: String, isHost: Boolean): Player {
            return Player(
                avatar = avatar,
                nickName = nickname,
                uid = currentUser.uid,
                ready = false,
                protected = false,
                turn = false,
                turnInProgress = false,
                turnOrder = 0,
                hand = arrayListOf(),
                isHost = isHost,
                isAlive = true,
                isWinner = false,
                wins = 0,
                unread = false,
                guide = true
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


        fun addGameToPlayer(userId: String, gameRoom: GameRoom) {
            val joinedGame =
                JoinedGame(roomCode = gameRoom.roomCode,
                    roomNickname = gameRoom.roomNickname,
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

        fun deleteUserGameRoomForAll(
            roomCode: String,
            roomNickname: String,
            players: List<Player>,
        ) {
            val joinedGame = JoinedGame(roomCode = roomCode, roomNickname = roomNickname,
                ready = true)

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

        fun deleteUserGameRoomForLocal(roomCode: String, roomNickname: String, player: Player) {
            val joinedGame = JoinedGame(roomCode = roomCode, roomNickname = roomNickname,
                ready = true)

            dbPlayers.document(player.uid).update("joinedGames", FieldValue.arrayRemove(joinedGame))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully added game to user's joined game list.")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to add game to user list. ${it.localizedMessage}")
                }
        }

        fun observeMyGames(): Flow<FirestoreUser> {
            return callbackFlow {
                var joinedGameList = FirestoreUser(
                    "",
                    listOf(),
                )
                currentUser?.let {
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
        }

        fun getCurrentUser(list: List<Player>, currentUser: String): Player {
            Log.d(TAG, "getCurrentUser is being called.")
            var currentPlayer = Player()
            list.forEach {
                if (it.uid == currentUser) {
                    currentPlayer = it
                }
            }
            Log.d(TAG, "getCurrentUser is done.")

            return currentPlayer
        }

        fun toggleCardGuideSetting(player: Player, gameRoom: GameRoom) {
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.guide = !it.guide
                }
            }
            val players = gameRoom.players
            dbGame.document(gameRoom.roomCode)
                .update("players", players)
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully updated player's card guide status.")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to update player's card guide status.")
                }
        }
    }
}