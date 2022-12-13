package com.example.loveletter.util.game.gamerules

import android.util.Log
import com.example.loveletter.TAG
import com.example.loveletter.dbGame
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.util.Tools

val GAMERULES_TAG = "GameRules"

class GameRules {
    companion object {
        fun onPlay(card: Int, player: Player, gameRoom: GameRoom) {
            gameRoom.players.forEach {
                if (it == player) {
                    player.hand.remove(card)
                    gameRoom.deck.discardDeck = addToDiscardPile(card, gameRoom = gameRoom)
                    gameRoom.deck.deck = removeFromDeck(card, gameRoom = gameRoom)
                    gameRoom.players = endPlayerTurn(gameRoom = gameRoom)
                    gameRoom.turn = changeGameTurn(turn = gameRoom.turn, size = gameRoom.players.size)
                    gameRoom.players = changePlayerTurn(gameRoom = gameRoom)
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

        private fun changeGameTurn(turn: Int, size: Int): Int {
            var currentTurn = turn
            currentTurn += 1
            if (currentTurn > size) {
                currentTurn = 1
            }
            Log.d(TAG, "Returning new turn: $currentTurn")
            return currentTurn
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

        private fun drawCard(gameRoom: GameRoom): Int {
            val card = Tools.randomNumber(
                size = 8
            )
            if (gameRoom.deck.deck.contains(card)) {
                removeFromDeck(
                    card = card,
                    gameRoom = gameRoom
                )
            } else {

            }
            return card
        }

        fun endGame() {

        }

        fun toggleProtection() {

        }

        fun eliminatePlayer() {

        }

        fun selectPlayer() {

        }


        private fun endPlayerTurn(gameRoom: GameRoom): List<Player> {
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
            return gameRoom.players
        }

        private fun changePlayerTurn(gameRoom: GameRoom): List<Player> {
            val card = drawCard(gameRoom = gameRoom)
            gameRoom.players.forEach {
                if (it.turnOrder == gameRoom.turn) {
                    it.turn = true
                    it.hand.add(card)
                }
            }
            return gameRoom.players
        }
    }
}