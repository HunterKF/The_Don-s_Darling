package com.example.loveletter.util.user

import com.example.loveletter.domain.Player
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HandleUser {
    companion object {
        private val currentUser = Firebase.auth.currentUser

        fun createPlayer(avatar: Int, nickname: String): Player {
            return Player(
                avatar = avatar,
                nickName = nickname,
                uid = currentUser.uid
            )
        }
    }
}