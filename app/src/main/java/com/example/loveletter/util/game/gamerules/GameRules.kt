package com.example.loveletter.util.game.gamerules

import android.util.Log
import com.example.loveletter.dbGame
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player

val GAMERULES_TAG = "GameRules"

class GameRules {
    companion object {
        fun onPlay(card: Int, player: Player, gameRoom: GameRoom) {
            gameRoom.players.forEach {
                if (it == player) {
                    player.hand.remove(card)
                    gameRoom.deck.discardDeck = addToDiscardPile(card, gameRoom = gameRoom)
                    gameRoom.deck.deck = removeFromDeck(card, gameRoom = gameRoom)
                    gameRoom.players = changeTurn(gameRoom = gameRoom)
                    dbGame.document(gameRoom.roomCode).set(gameRoom)
                        .addOnSuccessListener {
                            Log.d(GAMERULES_TAG, "Successfully updated game room")
                        }
                        .addOnFailureListener {
                            Log.d(GAMERULES_TAG, "Failed to update room: ${it.localizedMessage}")
                        }
                }
            }

        }

        private fun addToDiscardPile(card: Int, gameRoom: GameRoom): ArrayList<Int> {
            val deck = gameRoom.deck.discardDeck
            deck.add(card)
            return deck
        }

        private fun removeFromDeck(card: Int, gameRoom: GameRoom): ArrayList<Int> {
            val deck = gameRoom.deck.deck
            deck.remove(card)
            return deck
        }

        fun checkForSeven() {
            /*This can be done in the UI.*/
        }

        fun drawCard() {

        }

        fun endGame() {

        }

        fun toggleProtection() {

        }

        fun eliminatePlayer() {

        }

        fun selectPlayer() {

        }

        fun changeTurn(gameRoom: GameRoom): List<Player> {
            var currentTurn = 0
            gameRoom.players.forEach {
                if (it.turn) {
                    currentTurn = it.turnOrder
                    if (currentTurn >= gameRoom.players.size) {
                        currentTurn = 1
                    }
                    it.turn = false
                }
            }
            gameRoom.players.forEach {
                if (it.turnOrder == currentTurn) {
                    it.turn = true
                }
            }
            return gameRoom.players
        }
    }
}