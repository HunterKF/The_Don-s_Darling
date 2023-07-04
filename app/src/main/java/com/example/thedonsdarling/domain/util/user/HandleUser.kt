package com.example.thedonsdarling.domain.util.user

import android.util.Log
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player

class HandleUser {
    companion object {
        fun createGamePlayer(avatar: Int, nickname: String, isHost: Boolean, uid: String): Player {
            return Player(
                avatar = avatar,
                nickName = nickname,
                uid = uid,
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
            /*TODO - This should be a preference toggle, not db use*/
            /*gameRoom.players.forEach {
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
                }*/
        }
    }
}