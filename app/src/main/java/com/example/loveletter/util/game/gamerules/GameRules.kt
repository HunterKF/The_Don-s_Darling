package com.example.loveletter.util.game.gamerules

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.loveletter.TAG
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.LogMessage
import com.example.loveletter.domain.Player
import com.example.loveletter.util.Tools
import com.example.loveletter.util.game.GameServer
import com.example.loveletter.util.game.gamerules.CardRules.Handmaid

val GAMERULES_TAG = "GameRules"

class GameRules {
    companion object {
        fun handlePlayedCard(card: Int, player: Player, gameRoom: GameRoom) {
            Log.d(TAG, "handlePlayerCard is being called.")

            gameRoom.players.forEach {

                if (it.uid == player.uid) {

                    it.hand.remove(card)
                    gameRoom.deck.discardDeck = addToDiscardPile(card, gameRoom = gameRoom)

                    GameServer.updateGame(gameRoom = gameRoom)
                }
            }
            Log.d(TAG, "handlePlayerCard is done")


        }

        private fun onTurn(gameRoom: GameRoom, player: Player) {
            Log.d(TAG, "onTurn is being called")
            if (gameRoom.deck.deck.isNotEmpty()) {
                val card = drawCard(gameRoom = gameRoom)
                val players = gameRoom.players
                var deck = gameRoom.deck.deck
                players.forEach {
                    if (it == player) {
                        if (it.protected) {
                            it.protected = Handmaid.toggleProtection(it)
                        }
                        it.hand.add(card)
                        it.turnInProgress = true
                        deck = removeFromDeck(card = card, gameRoom = gameRoom)
                    }
                }
                GameServer.updateGame(gameRoom = gameRoom)
            }
            Log.d(TAG, "onTurn is done")


        }


        private fun changeGameTurn(turn: Int, size: Int, players: List<Player>): Int {
            Log.d(TAG, "changeGameTurn is being called")
            var currentTurn = turn
            currentTurn += 1
            if (currentTurn > size) {
                currentTurn = 1
            }
            val sortedPlayers = players.sortedBy { it.turnOrder }.reversed()
            sortedPlayers.forEach {
                if (it.turnOrder == currentTurn && !it.isAlive) {
                    currentTurn += 1
                    if (currentTurn > size) {
                        currentTurn = 1
                    }
                }
            }
            Log.d(TAG, "Returning new turn: $currentTurn")
            Log.d(TAG, "changeGameTurn is done")

            return currentTurn
        }

        fun addToDiscardPile(card: Int, gameRoom: GameRoom): ArrayList<Int> {
            Log.d(TAG, "addToDiscardPile is being called")

            val deck = gameRoom.deck.discardDeck
            deck.add(card)
            Log.d(TAG, "addToDiscardPile is done")

            return deck
        }

        fun removeFromDeck(card: Int, gameRoom: GameRoom): ArrayList<Int> {
            Log.d(TAG, "removeFromDeck is being called")

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
            Log.d(TAG, "removeFromDeck is done")

            return deck
        }


        fun drawCard(gameRoom: GameRoom): Int {
            Log.d(TAG, "drawCard is being called")

            val card = Tools.randomNumber(
                size = gameRoom.deck.deck.size
            )
            Log.d(TAG, "drawCard is done")

            return gameRoom.deck.deck[card]
        }

        fun endRound(gameRoom: GameRoom) {
            Log.d(TAG, "endRound is being called")

            val players = gameRoom.players


            val alivePlayers = gameRoom.players.filter {
                it.isAlive
            }
            var remainingPlayers = arrayListOf<Player>()
            val logMessage = LogMessage.createLogMessage(
                message = "",
                type = "winnerMessage",
                uid = null
            )
            if (alivePlayers.size > 1) {
                val winningCard = winningHand(players = players)
                players.forEach { player ->
                    if (winningCard.contains(player)) {
                        player.isWinner = true
                        player.wins += 1
                        remainingPlayers.add(player)
                    }
                }
                if (remainingPlayers.size == 1) {
                    val roundWinner = remainingPlayers.first()
                    logMessage.message = "Round over! ${roundWinner.nickName} has won the round!"
                    logMessage.uid = roundWinner.uid
                } else {
                    logMessage.message = "Appears to have been a tie..."
                }
                gameRoom.players = players
            } else if (alivePlayers.size == 1) {
                players.forEach {
                    if (it.isAlive) {
                        it.isWinner = true
                        it.wins += 1
                        remainingPlayers.add(it)

                    }
                }
                val roundWinner = remainingPlayers.first()
                logMessage.message = "Round over! ${roundWinner.nickName} has won the round!"
                logMessage.uid = roundWinner.uid
                gameRoom.players = players
            }
            gameRoom.roundOver = true
            val winner = checkForWinner(gameRoom.players, gameRoom.playLimit)

            winner?.let {
                gameRoom.gameOver = true
                if (gameRoom.gameOver && gameRoom.roundOver) {
                    logMessage.message = "The game is done! ${winner.nickName} won the game!"
                    Log.d(TAG, "The game should be over! The winner is: ${winner.nickName}")
                }
            }
            gameRoom.gameLog.add(logMessage)

            GameServer.updateGame(gameRoom)
            Log.d(TAG, "endRound is done")
        }

        private fun winningHand(players: List<Player>): List<Player> {
            Log.d(TAG, "winningHand is being called")

            val winners = arrayListOf<Player>()
            var duplicates = mutableStateOf(0)
            val hands = arrayListOf<Int>()
            players.forEach { player ->
                player.hand.forEach { card ->
                    hands.add(card)
                }
            }
            hands.sortDescending()
            val highestCard = hands.first()

            val distinct = hands.distinct().count()

            if (distinct != hands.size) {
                var groupBy = hands.groupBy { it }

                groupBy.forEach {
                    if (it.value.size != 1 && it.key > highestCard) {
                        duplicates.value = it.key

                    }
                }
            }
            if (duplicates.value != 0) {
                val winning = players.filter { it.hand.first() == duplicates.value }
                winning.forEach {
                    winners.add(it)
                }
            } else {
                val winner = players.first { it.hand.first() == highestCard }
                winners.add(winner)
            }

            Log.d(TAG, "winningHand is done")

            return winners
        }


        private fun checkForWinner(players: List<Player>, playLimit: Int): Player? {
            Log.d(TAG, "checkForWinner is being called")

            val filterArray = players.filter {
                it.wins >= playLimit
            }
            Log.d(TAG, "checkForWinner is done")

            return if (filterArray.isEmpty()) {
                null
            } else {
                filterArray.first()
            }
        }

        fun eliminatePlayer(gameRoom: GameRoom, player: Player): GameRoom {
            Log.d(TAG, "eliminatePlayer is being called")

            gameRoom.deck.discardDeck = addToDiscardPile(player.hand.first(), gameRoom)
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                    it.hand.clear()
                }
            }
            Log.d(TAG, "eliminatePlayer is done")
            //returns a gameroom where player is not alive, the discard deck is updated.
            return gameRoom
        }

        fun onEnd(gameRoom: GameRoom, logMessage: LogMessage) {
            Log.d(TAG, "onEnd is being called")

            gameRoom.players = endPlayerTurn(gameRoom = gameRoom)
            if (!gameRoom.gameOver) {
                val changedTurn = changeGameTurn(turn = gameRoom.turn,
                    size = gameRoom.players.size,
                    players = gameRoom.players)
                gameRoom.turn = changeGameTurn(turn = gameRoom.turn,
                    size = gameRoom.players.size,
                    players = gameRoom.players)
                gameRoom.players = changePlayerTurn(gameRoom = gameRoom, changedTurn = changedTurn)
            }
            gameRoom.gameLog = GameServer.updateGameLog(gameRoom.gameLog, logMessage)
            GameServer.updateGame(gameRoom = gameRoom)
            Log.d(TAG, "onEnd is done")

        }


        private fun endPlayerTurn(gameRoom: GameRoom): List<Player> {
            Log.d(TAG, "endPlayerTurn is being called")

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
            Log.d(TAG, "endPlayerTurn is done")

            return gameRoom.players
        }

        private fun changePlayerTurn(gameRoom: GameRoom, changedTurn: Int): List<Player> {
            Log.d(TAG, "changePlayerTurn is being called")

            gameRoom.players.forEach {
                if (it.turnOrder == changedTurn && it.isAlive) {
                    it.turn = true
                }
            }
            Log.d(TAG, "changePlayerTurn is done")

            return gameRoom.players
        }

        fun assignTurns(list: List<Player>, gameTurn: Int): List<Player> {
            Log.d(TAG, "assignTurns is being called")

            val turn = mutableStateOf(1)
            list.forEach {
                it.turnOrder = turn.value
                if (it.turnOrder == gameTurn) {
                    it.turn = true
                    it.turnInProgress = true
                }
                turn.value = turn.value + 1
            }
            Log.d(TAG, "assignTurns is done")

            return list
        }

        fun dealCards(gameRoom: GameRoom): GameRoom {
            Log.d(TAG, "dealCards is being called")

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
                    } else if (randomCard < 0) {
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
                    } else if (randomCard < 0) {
                        randomCard + 1
                    }
                    size.value -= 1

                    player.hand.add(gameRoom.deck.deck[randomCard])
                    gameRoom.deck.deck.removeAt(randomCard)

                }

            }
            Log.d(TAG, "dealCards is done")

            return gameRoom
        }

        fun launchOnTurn(
            game: GameRoom,
            localPlayer: Player,
        ) {
            game.players.forEach { player ->

                if (localPlayer.uid == player.uid) {
                    if (player.turn && player.hand.size < 2 && player.isAlive && !player.turnInProgress) {
                        Log.d("LaunchedEffect",
                            "Matching player has been found: ${player.nickName}")
                        GameRules.onTurn(
                            gameRoom = game,
                            player = player
                        )
                    }
                }
            }
        }


    }
}