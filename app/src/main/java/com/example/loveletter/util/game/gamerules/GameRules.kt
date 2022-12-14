package com.example.loveletter.util.game.gamerules

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
//                    gameRoom.deck.deck = removeFromDeck(card, gameRoom = gameRoom)
                    gameRoom.players = endPlayerTurn(gameRoom = gameRoom)
                    if (!gameRoom.gameOver) {
                        gameRoom.turn =
                            changeGameTurn(turn = gameRoom.turn, size = gameRoom.players.size)
                        gameRoom.players = changePlayerTurn(gameRoom = gameRoom)
                    }
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

        fun onTurn(gameRoom: GameRoom, player: Player) {
            Log.d(TAG, "onTurn is being called")
            if (gameRoom.deck.deck.isNotEmpty()) {
                val card = drawCard(gameRoom = gameRoom)
                val players = gameRoom.players
                var deck = gameRoom.deck.deck
                players.forEach {
                    if (it == player) {
                        it.hand.add(card)
                        deck = removeFromDeck(card = card, gameRoom = gameRoom)
                    }
                }
                updateGame(gameRoom = gameRoom)
            }

        }

        private fun updateGame(gameRoom: GameRoom) {
            dbGame.document(gameRoom.roomCode).set(gameRoom)
                .addOnSuccessListener {
                    Log.d(GAMERULES_TAG, "Successfully updated game room")
                }
                .addOnFailureListener {
                    Log.d(GAMERULES_TAG, "Failed to update room: ${it.localizedMessage}")
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
            val index = deck.indexOf(card)
            val TAG1 = "removeFromDeck"
            Log.d(TAG1, "Array values: $deck")
            Log.d(TAG1, "Array size: ${deck.size}")
            Log.d(TAG1, "Removing card number: $card")
            Log.d(TAG1, "Index: $index")
            if (index >= 0) {
                deck.removeAt(index)
            } else {
                Log.d(TAG1, "An error occurred with the index: $index")
            }
            return deck
        }

        fun checkForSeven() {
            /*This can be done in the UI.*/
        }

        private fun drawCard(gameRoom: GameRoom): Int {
            val card = Tools.randomNumber(
                size = gameRoom.deck.deck.size
            )
            return gameRoom.deck.deck[card]
        }

        fun endRound(gameRoom: GameRoom) {
            val players = arrayListOf<Player>()
            gameRoom.players.forEach {
                if (it.isAlive) {
                    players.add(it)
                }
            }
            if (players.size > 1) {
                val winningCard = winningHand(players = players)
                players.forEach {
                    if (it.hand.contains(winningCard)) {
                        it.isWinner = true
                        it.wins += 1
                    }
                }
                gameRoom.players = players
            } else if (players.size == 1) {
                /*TODO - Declare winner for this case*/
            }
            /*TODO - Project a winner*/
            /*TODO - Compare remaining players' cards and return a player who won. Can do this as a Boolean in the player status*/
            /*TODO - Update game with the new game*/
            gameRoom.roundOver = true
            val winner = checkForWinner(gameRoom.players, gameRoom.playLimit)
            winner?.let {
                gameRoom.gameOver = true
            }
            if (gameRoom.gameOver && gameRoom.roundOver) {
                /*TODO - End the whole game*/
            }
            updateGame(gameRoom)
        }

        private fun winningHand(players: List<Player>): Int {
            val hands = arrayListOf<Int>()
            players.forEach { player ->
                player.hand.forEach { card ->
                    hands.add(card)
                }
            }
            hands.sortDescending()
            hands.first()
            return hands.first()
        }

        fun startNewGame(gameRoom: GameRoom) {
            val turn = (1..gameRoom.players.size).shuffled().random()
            gameRoom.players.forEach {
                it.hand.clear()
                it.isWinner = false
                it.turn = false
                it.isAlive = true
                it.turnOrder = 0
                it.ready = true
            }
            var game = GameRoom()
            game.roomCode = gameRoom.roomCode
            game.playLimit = gameRoom.playLimit
            game.players = gameRoom.players
            game.players = assignTurns(game.players)
            gameRoom.start = true
            game.turn = turn
            game = dealCards(game)
            updateGame(game)
        }

        private fun checkForWinner(players: List<Player>, playLimit: Int): Player? {
            val filterArray = players.filter {
                it.wins == playLimit
            }
            return if (filterArray.isEmpty()) {
                null
            } else {
                filterArray.first()

            }
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
            gameRoom.players.forEach {
                if (it.turnOrder == gameRoom.turn) {
                    it.turn = true
                }
            }
            return gameRoom.players
        }
        fun assignTurns(list: List<Player>): List<Player> {
            val turn = mutableStateOf(1)
            list.forEach {
                it.turnOrder = turn.value
                if (it.turnOrder == 1) {
                    it.turn = true
                }
                turn.value = turn.value + 1
            }
            return list
        }

        fun dealCards(gameRoom: GameRoom): GameRoom {
            val size = gameRoom.deck.deck.size
            gameRoom.players.forEach { player ->
                if (player.turn) {
                    var randomCard = Tools.randomNumber(size)
                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)
                    randomCard = Tools.randomNumber(size)
                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)

                } else {
                    val randomCard = Tools.randomNumber(size)
                    if (randomCard == size) {
                        randomCard - 1
                    } else if (randomCard < 0 ) {
                        randomCard + 1
                    }
                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)
                }

            }
            return gameRoom
        }
    }
}