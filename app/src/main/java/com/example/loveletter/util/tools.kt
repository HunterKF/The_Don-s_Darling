package com.example.loveletter.util

import com.example.loveletter.domain.Player
import com.google.firebase.auth.FirebaseUser

class Tools {
    companion object {
        fun randomNumber(size: Int): Int {
            val measuredSize = size -1
            return (0..measuredSize).shuffled().random()
        }
        fun checkCards(players: List<Player>): Boolean {
            val filterPlayed = players.filter { player ->
                player.hand.size == 2
            }
            return filterPlayed.isNotEmpty()
        }
        fun getHost(players: List<Player>, currentUser: FirebaseUser?): Boolean {
            val host = players.filter { player ->
                player.isHost
            }
            return host.first().uid == currentUser!!.uid
        }
        fun getPlayer(players: List<Player>, currentUser: FirebaseUser?): Player {
            val player = players.filter {
                it.uid == currentUser!!.uid
            }
            return player.first()
        }
        fun getRandomString(): String {
            val allowedChars = ('A'..'Z') + ('0'..'9')
            return (1..4)
                .map { allowedChars.shuffled().random() }
                .joinToString("")
        }
    }
}