package com.example.thedonsdarling.domain.util.game.gamerules

import androidx.compose.runtime.mutableStateOf
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.util.Tools
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.TheDoctor

val GAMERULES_TAG = "GameRules"

class GameRules {
    companion object {

        fun handlePlayedCard(card: Int, player: Player, gameRoom: GameRoom): GameRoom {
//            Log.d(TAG, "handlePlayerCard is being called.")

            gameRoom.players.forEach {

                if (it.uid == player.uid) {

                    it.hand.remove(card)
                    gameRoom.deck.discardDeck = addToDiscardPile(card, gameRoom = gameRoom)

//                    GameServer.updateGame(gameRoom = gameRoom)
                }
            }
            return gameRoom
//            Log.d(TAG, "handlePlayerCard is done")
        }

        private fun onTurn(gameRoom: GameRoom, player: Player): GameRoom {
//            Log.d(TAG, "onTurn is being called")
            if (gameRoom.deck.deck.isNotEmpty()) {
                val card = drawCard(gameRoom = gameRoom)
                val players = gameRoom.players
                var deck = gameRoom.deck.deck
                players.forEach {
                    if (it == player) {
                        if (it.protected) {
                            it.protected = TheDoctor.toggleProtection(it)
                        }
                        it.hand.add(card)
                        it.turnInProgress = true
                        deck = removeFromDeck(card = card, gameRoom = gameRoom)
                    }
                }
            }
//            Log.d(TAG, "onTurn is done")
            return gameRoom


        }


        private fun changeGameTurn(turn: Int, size: Int, players: List<Player>): Int {
//            Log.d(TAG, "changeGameTurn is being called")
            var currentTurn = turn
            //3
            currentTurn += 1
            //4
            if (currentTurn > size) {
                currentTurn = 1
            }
            val alivePlayers = players.filter { it.isAlive }.sortedBy { it.turnOrder }

            for (i in 0 until (players.size)) {

                if (players[i].turnOrder == currentTurn && !players[i].isAlive) {
                    currentTurn += 1
                    println("Found current player. ${players[i].nickName}")
                    println("if: currentTurn = $currentTurn")
                } else if (players[i].turnOrder == currentTurn && players[i].isAlive) {
                    if (players[i].turn) {
                        currentTurn += 1
                        players[i].turn = false
                    } else {
                        currentTurn = players[i].turnOrder
                        println("Found current player. ${players[i].nickName}")
                        println("else if: currentTurn = $currentTurn")
                        return currentTurn
                    }

                }
                if (currentTurn > players.size) {
                    currentTurn = alivePlayers.first().turnOrder
                    println("last if: currentTurn = $currentTurn")
                    return currentTurn
                }
            }


//            Log.d(TAG, "Returning new turn: $currentTurn")
//            Log.d(TAG, "changeGameTurn is done")

            return currentTurn
        }

        fun addToDiscardPile(card: Int, gameRoom: GameRoom): ArrayList<Int> {
//            Log.d(TAG, "addToDiscardPile is being called")

            val deck = gameRoom.deck.discardDeck
            deck.add(card)
//            Log.d(TAG, "addToDiscardPile is done")

            return deck
        }

        fun removeFromDeck(card: Int, gameRoom: GameRoom): ArrayList<Int> {
//            Log.d(TAG, "removeFromDeck is being called")

            val deck = gameRoom.deck.deck
            val index = deck.indexOf(card)
            val TAG1 = "removeFromDeck"
            if (index >= 0) {
                deck.removeAt(index)
            } else {
//                Log.d(TAG1, "An error occurred with the index: $index")
            }
//            Log.d(TAG, "removeFromDeck is done")

            return deck
        }


        fun drawCard(gameRoom: GameRoom): Int {
//            Log.d(TAG, "drawCard is being called")

            val card = Tools.randomNumber(
                size = gameRoom.deck.deck.size
            )
//            Log.d(TAG, "drawCard is done")

            return gameRoom.deck.deck[card]
        }

        fun endRound(gameRoom: GameRoom): EndRoundResult.FinalResult {
//            Log.d(TAG, "endRound is being called")

            val winningGame = determineWinner(gameRoom)
            val logMessage = LogMessage()
            var endRoundType: EndRoundResult = EndRoundResult.RoundIsOverWinner
            when (winningGame.type) {
                is DetermineWinnerResult.Winner -> {
                    endRoundType = EndRoundResult.RoundIsOverWinner
                }
                is DetermineWinnerResult.Tie -> {
                    endRoundType = EndRoundResult.RoundIsOverTie

                }
            }
            winningGame.gameRoom.roundOver = true
            val gameWinner = checkForGameWinner(gameRoom.players, gameRoom.playLimit)

            gameWinner?.let {
                winningGame.gameRoom.gameOver = true
                if (winningGame.gameRoom.gameOver && winningGame.gameRoom.roundOver) {
                    return EndRoundResult.FinalResult(
                        winningGame.gameRoom,
                        type = EndRoundResult.GameIsOver,
                        winningGame.remainingPlayers
                    )
//                    Log.d(TAG, "The game should be over! The winner is: ${gameWinner.nickName}")
                }
            }
            return EndRoundResult.FinalResult(
                gameRoom = winningGame.gameRoom,
                endRoundType,
                remainingPlayers = winningGame.remainingPlayers
            )
            /*gameRoom.gameLog.add(logMessage)

            */
//            Log.d(TAG, "endRound is done")
        }

        private fun determineWinner(
            gameRoom: GameRoom,
        ): DetermineWinnerResult.WinningGame {
            val players = gameRoom.players


            val alivePlayers = gameRoom.players.filter {
                it.isAlive
            }
            var remainingPlayers = arrayListOf<Player>()
            var endResult: DetermineWinnerResult = DetermineWinnerResult.Winner
            val logMessage = LogMessage.createLogMessage(
                message = "",
                toastMessage = null,
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
                    endResult = DetermineWinnerResult.Winner
                } else {

                    endResult = DetermineWinnerResult.Tie
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
                gameRoom.players = players
                endResult = DetermineWinnerResult.Winner
            }
            val winner = gameRoom.players.first { it.isWinner }

            return DetermineWinnerResult.WinningGame(
                gameRoom = gameRoom,
                winner = winner,
                type = endResult,
                remainingPlayers = remainingPlayers
            )
        }

        private fun winningHand(players: List<Player>): List<Player> {
//            Log.d(WINNINGTAG, "winningHand is being called")

            val winners = arrayListOf<Player>()
            var duplicates = mutableStateOf(0)
            val hands = arrayListOf<Int>()
            players.forEach { player ->
                player.hand.forEach { card ->
                    hands.add(card)
                }
            }
            val highestCard = hands.maxOrNull()!!

            val distinct = hands.distinct().count()

            if (distinct != hands.size) {
                var groupBy = hands.groupBy { it }

                groupBy.forEach {
                    if (it.value.size != 1 && it.key == highestCard) {
                        duplicates.value = it.key
                    }
                }
            }
            if (duplicates.value != 0) {
//                Log.d(WINNINGTAG, "duplicates detected. Duplicate = ${duplicates.value}")

                val winning = players.filter { it.hand.contains(duplicates.value) }
                winning.forEach {
                    winners.add(it)
//                    Log.d(WINNINGTAG, "duplicates detected. adding ${it.nickName}")

                }
            } else {
                val winner = players.filter { it.isAlive }.first { it.hand.first() == highestCard }
                winners.add(winner)
//                Log.d(WINNINGTAG, "no duplicates  detected. adding ${winner.nickName}")


            }

//            Log.d(WINNINGTAG, "winningHand is done")

            return winners
        }


        private fun checkForGameWinner(players: List<Player>, playLimit: Int): Player? {
//            Log.d(TAG, "checkForWinner is being called")

            val filterArray = players.filter {
                it.wins >= playLimit
            }
//            Log.d(TAG, "checkForWinner is done")

            return if (filterArray.isEmpty()) {
                null
            } else {
                filterArray.first()
            }
        }

        fun eliminatePlayer(gameRoom: GameRoom, player: Player): GameRoom {
//            Log.d(TAG, "eliminatePlayer is being called")

            gameRoom.deck.discardDeck = addToDiscardPile(player.hand.first(), gameRoom)
            gameRoom.players.forEach {
                if (it.uid == player.uid) {
                    it.isAlive = false
                    it.hand.clear()
                }
            }
//            Log.d(TAG, "eliminatePlayer is done")
            //returns a gameroom where player is not alive, the discard deck is updated.
            return gameRoom
        }

        fun onEnd(gameRoom: GameRoom, logMessage: LogMessage): GameRoom {
//            Log.d(TAG, "onEnd is being called")
            var updatedGameRoom = gameRoom
            var remainingPlayers = updatedGameRoom.players.filter { it.isAlive }
            updatedGameRoom.players = endPlayerTurn(gameRoom = gameRoom)
            if (updatedGameRoom.deck.deck.isEmpty()) {
                updatedGameRoom.deckClear = true
            }
            if (!updatedGameRoom.gameOver && !updatedGameRoom.roundOver) {
                val changedTurn = changeGameTurn(
                    turn = updatedGameRoom.turn,
                    size = updatedGameRoom.players.size,
                    players = updatedGameRoom.players
                )
                updatedGameRoom.turn = changeGameTurn(
                    turn = updatedGameRoom.turn,
                    size = updatedGameRoom.players.size,
                    players = updatedGameRoom.players
                )
                updatedGameRoom.players =
                    changePlayerTurn(gameRoom = updatedGameRoom, changedTurn = changedTurn)
            }
            updatedGameRoom.gameLog = updateGameLogs(updatedGameRoom.gameLog, logMessage)
                if (remainingPlayers.size != 1) {
                    updatedGameRoom = launchOnTurn(game = gameRoom)
                }
            return updatedGameRoom
//            GameServer.updateGame(gameRoom = updatedGameRoom)
//            Log.d(TAG, "onEnd is done")
        }

        private fun updateGameLogs(
            logs: ArrayList<LogMessage>,
            logMessage: LogMessage,
        ): ArrayList<LogMessage> {
            logs.add(logMessage)

            return logs
        }


        private fun endPlayerTurn(gameRoom: GameRoom): List<Player> {
//            Log.d(TAG, "endPlayerTurn is being called")

            gameRoom.players.forEach {
                if (it.turn) {
                    it.turn = false
                    it.turnInProgress = false
                }
            }
//            Log.d(TAG, "endPlayerTurn is done")

            return gameRoom.players
        }

        private fun changePlayerTurn(gameRoom: GameRoom, changedTurn: Int): List<Player> {
//            Log.d(TAG, "changePlayerTurn is being called")

            gameRoom.players.forEach {
                if (it.turnOrder == changedTurn && it.isAlive) {
                    it.turn = true
                }
            }
//            Log.d(TAG, "changePlayerTurn is done")

            return gameRoom.players
        }

        fun assignTurns(list: List<Player>, gameTurn: Int): List<Player> {
//            Log.d(TAG, "assignTurns is being called")

            val turn = mutableStateOf(1)
            list.forEach {
                it.turnOrder = turn.value
                if (it.turnOrder == gameTurn) {
                    it.turn = true
                    it.turnInProgress = true
                }
                turn.value = turn.value + 1
            }
//            Log.d(TAG, "assignTurns is done")

            return list
        }

        fun dealCards(gameRoom: GameRoom): GameRoom {
//            Log.d(TAG, "dealCards is being called")
            println("DEAL CARDS")
            val size = mutableStateOf(gameRoom.deck.deck.size)
            gameRoom.players.forEach { player ->
//                Log.d("dealCards", "deck size: ${size.value} ")

                if (player.turn) {
                    var randomCard = Tools.randomNumber(size.value)
                    size.value -= 2

//                    Log.d("dealCards", "(if) Dealing a card: $randomCard ")
//                    Log.d("dealCards", "(if) Deck size: ${size.value}")


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
//                    Log.d("dealCards", "(else) Dealing a card: $randomCard")
//                    Log.d("dealCards", "(else) Deck size: ${size.value}")

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
//            Log.d(TAG, "dealCards is done")

            return gameRoom
        }

        fun launchOnTurn(
            game: GameRoom,
        ): GameRoom {
            var currentPlayer = Player()
            game.players.forEach {
                if (it.turn) {
                    currentPlayer = it
                }
            }
            game.players.forEach { player ->
                if (currentPlayer.uid == player.uid) {
                    if (player.turn && player.hand.size < 2 && player.isAlive && !player.turnInProgress) {
//                        Log.d("LaunchedEffect", "Matching player has been found: ${player.nickName}")
                        return onTurn(
                            gameRoom = game,
                            player = player
                        )
                    }
                }

            }
            return game
        }


    }
}

sealed class DetermineWinnerResult {
    object Winner : DetermineWinnerResult()
    object Tie : DetermineWinnerResult()
    data class WinningGame(
        val gameRoom: GameRoom,
        val winner: Player,
        val type: DetermineWinnerResult = Winner,
        val remainingPlayers: ArrayList<Player>,
    )
}

sealed class EndRoundResult {
    object RoundIsOverWinner : EndRoundResult()
    object RoundIsOverTie : EndRoundResult()
    object GameIsOver : EndRoundResult()
    data class FinalResult(
        val gameRoom: GameRoom,
        val type: EndRoundResult = RoundIsOverWinner,
        val remainingPlayers: ArrayList<Player>,
    )
}