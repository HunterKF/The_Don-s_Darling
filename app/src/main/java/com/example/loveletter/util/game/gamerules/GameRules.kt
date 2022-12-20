package com.example.loveletter.util.game.gamerules

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.loveletter.TAG
import com.example.loveletter.dbGame
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.util.Tools
import com.example.loveletter.util.game.gamerules.CardRules.Handmaid

val GAMERULES_TAG = "GameRules"

class GameRules {
    companion object {
        fun handlePlayedCard(card: Int, player: Player, gameRoom: GameRoom) {

            gameRoom.players.forEach {
                if (it == player) {
                    player.hand.remove(card)
                    gameRoom.deck.discardDeck = addToDiscardPile(card, gameRoom = gameRoom)
//                    gameRoom.deck.deck = removeFromDeck(card, gameRoom = gameRoom)
//                    gameRoom.players = endPlayerTurn(gameRoom = gameRoom)

                    updateGame(gameRoom = gameRoom)
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
                        if(it.protected) {
                            it.protected = Handmaid.toggleProtection(it)
                        }
                        it.hand.add(card)
                        it.turnInProgress = true
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

        private fun changeGameTurn(turn: Int, size: Int, players: List<Player>): Int {

            var currentTurn = turn
            currentTurn += 1
            if (currentTurn > size) {
                currentTurn = 1
            }
            val sortedPlayers = players.sortedBy { it.turnOrder }.reversed()
            sortedPlayers.forEach {
                if (it.turnOrder == currentTurn && !it.isAlive) {
                    currentTurn += 1
                }
            }
            Log.d(TAG, "Returning new turn: $currentTurn")
            return currentTurn
        }

         fun addToDiscardPile(card: Int, gameRoom: GameRoom): ArrayList<Int> {
            val deck = gameRoom.deck.discardDeck
            deck.add(card)
            return deck
        }

        fun removeFromDeck(card: Int, gameRoom: GameRoom): ArrayList<Int> {
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

        fun drawCard(gameRoom: GameRoom): Int {
            val card = Tools.randomNumber(
                size = gameRoom.deck.deck.size
            )
            return gameRoom.deck.deck[card]
        }

        fun endRound(gameRoom: GameRoom) {
            val players = gameRoom.players

            val filteredPlayers = gameRoom.players.filter {
                it.isAlive
            }
            if (filteredPlayers.size > 1) {
                val winningCard = winningHand(players = players)
                players.forEach {
                    if (it.hand.contains(winningCard)) {
                        it.isWinner = true
                        it.wins += 1
                    }
                }
                gameRoom.players = players
            } else if (filteredPlayers.size == 1) {
                players.forEach {
                    if (it.isAlive) {
                        it.isWinner = true
                        it.wins += 1
                    }
                }
                gameRoom.players = players
            }
            gameRoom.roundOver = true
            val winner = checkForWinner(gameRoom.players, gameRoom.playLimit)
            winner?.let {
                gameRoom.gameOver = true
                if (gameRoom.gameOver && gameRoom.roundOver) {
                    Log.d(TAG, "The game should be over! The winner is: ${winner.nickName}")
                }
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
                it.turnInProgress = false
                it.isAlive = true
                it.turnOrder = 0
                it.ready = true
            }
            var game = GameRoom()
            game.roomCode = gameRoom.roomCode
            game.playLimit = gameRoom.playLimit
            game.players = gameRoom.players
            game.players = assignTurns(game.players, turn)
            game.roundOver = false
            game.gameOver = false
            gameRoom.start = true
            game.turn = turn
            game = dealCards(game)
            updateGame(game)
        }

        private fun checkForWinner(players: List<Player>, playLimit: Int): Player? {
            val filterArray = players.filter {
                it.wins >= playLimit
            }
            return if (filterArray.isEmpty()) {
                null
            } else {
                filterArray.first()

            }
        }

        fun toggleProtection() {

        }

        fun eliminatePlayer(gameRoom: GameRoom, player: Player) {
            gameRoom.deck.discardDeck = addToDiscardPile(player.hand.first(), gameRoom)
            updateGame(gameRoom)
        }

        fun onEnd(gameRoom: GameRoom) {
            Log.d("King", "Ending turn")
            gameRoom.players = endPlayerTurn(gameRoom = gameRoom)
            if (!gameRoom.gameOver) {
                gameRoom.turn = changeGameTurn(turn = gameRoom.turn, size = gameRoom.players.size, players = gameRoom.players)
                gameRoom.players = changePlayerTurn(gameRoom = gameRoom)
            }
            updateGame(gameRoom = gameRoom)
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
                    it.turnInProgress = false
                }
            }
            return gameRoom.players
        }

        private fun changePlayerTurn(gameRoom: GameRoom): List<Player> {
            gameRoom.players.forEach {
                if (it.turnOrder == gameRoom.turn && it.isAlive) {
                    it.turn = true
                }
            }
            return gameRoom.players
        }
        fun assignTurns(list: List<Player>, gameTurn: Int): List<Player> {
            val turn = mutableStateOf(1)
            list.forEach {
                it.turnOrder = turn.value
                if (it.turnOrder == gameTurn) {
                    it.turn = true
                    it.turnInProgress = true
                }
                turn.value = turn.value + 1
            }
            return list
        }

        fun dealCards(gameRoom: GameRoom): GameRoom {
            val size = mutableStateOf(gameRoom.deck.deck.size)
            gameRoom.players.forEach { player ->
                Log.d("dealCards", "deck size: ${size.value} ")

                if (player.turn) {
                    var randomCard = Tools.randomNumber(size.value)
                    size.value -= 2

                    Log.d("dealCards", "(if) Dealing a card: $randomCard ")
                    Log.d("dealCards", "(if) Deck size: ${size.value}")


                    if (randomCard == size.value) {
                        randomCard - 1
                    } else if (randomCard < 0 ) {
                        randomCard + 1
                    }
                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)
                    randomCard = Tools.randomNumber(size.value)
                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)

                } else {

                    val randomCard = Tools.randomNumber(size.value)
                    Log.d("dealCards", "(else) Dealing a card: $randomCard")
                    Log.d("dealCards", "(else) Deck size: ${size.value}")

                    if (randomCard == size.value) {
                        randomCard - 1
                    } else if (randomCard < 0 ) {
                        randomCard + 1
                    }
                    size.value -= 1

                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)

                }

            }
            return gameRoom
        }
    }
}