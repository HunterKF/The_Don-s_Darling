package com.example.loveletter.presentation.game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loveletter.TAG
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.LogMessage
import com.example.loveletter.domain.Player
import com.example.loveletter.domain.Result
import com.example.loveletter.util.game.GameServer
import com.example.loveletter.util.game.gamerules.CardRules.*
import com.example.loveletter.util.game.gamerules.GameRules
import com.example.loveletter.util.user.HandleUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class GameViewModel : ViewModel() {

    private val loadingState = MutableStateFlow<GameState>(GameState.Loading)

    val state = loadingState.asStateFlow()


    val roomCode = mutableStateOf("1234")
    val currentUser: FirebaseUser? = Firebase.auth.currentUser
    var currentPlayer = mutableStateOf(Player())
    val selectedPlayer = mutableStateOf(Player())
    private val playedCard = mutableStateOf(0)
    var selectPlayerAlert = mutableStateOf(false)
    val guessCardAlert = mutableStateOf(false)
    val revealCardAlert = mutableStateOf(false)
    val emptyCard = mutableStateOf(0)
    val resultAlert = mutableStateOf(false)
    val resultMessage = mutableStateOf("")


    val chatOpen = mutableStateOf(false)

    val listOfPlayers = mutableStateOf(listOf(Player()))



    fun onPlay(card: Int, gameRoom: GameRoom, player: Player) {
        playedCard.value = card
        when (playedCard.value) {
            1 -> {
                selectPlayerAlert.value = true
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
            }
            2 -> {
                selectPlayerAlert.value = true
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
            }
            3 -> {
                selectPlayerAlert.value = true
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
            }
            4 -> {
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
                var message = ""
                gameRoom.players.forEach {
                    if (it.uid == currentPlayer.value.uid) {
                        it.protected = Handmaid.toggleProtection(it)
                        message = "${it.nickName} has played the handmaid. They are protected."
                    }
                }
                val logMessage = LogMessage.createLogMessage(message = message, type = "gameLog", uid = null)

                GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
            }
            5 -> {
                selectPlayerAlert.value = true
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
            }
            6 -> {
                selectPlayerAlert.value = true
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
            }
            7 -> {
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
                val logMessage =
                    LogMessage.createLogMessage("${currentPlayer.value.nickName} has played the Countess.", type = "gameLog", uid = null)
                GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
            }
            8 -> {
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
                val result = Princess.eliminatePlayer(gameRoom, player)
                val logMessage = LogMessage.createLogMessage(result.message, type = "gameLog", uid = null)
                result.game?.let { GameRules.onEnd(gameRoom = it, logMessage) }

            }
        }
        /*TODO - Add played card into the discard pile*/
        /*TODO - Remove card from player hand*/
        /*TODO - Perform card logic*/
        /*TODO - End player turn*/
    }

    fun onSelectPlayer(selectedPlayer: Player, gameRoom: GameRoom) {
        val players = gameRoom.players
        this.selectedPlayer.value = selectedPlayer
        gameRoom.players.forEach {
            if (currentPlayer.value.uid == it.uid) {
                currentPlayer.value = it
            }
        }
        if (!selectedPlayer.protected) {
            when (playedCard.value) {
                1 -> {
                    guessCardAlert.value = true
                }
                2 -> {
                    revealCardAlert.value = true
                    emptyCard.value = selectedPlayer.hand.first()
                    var message =
                        "${currentPlayer.value.nickName} looked at ${selectedPlayer.nickName}'s card."
                    val logMessage = LogMessage.createLogMessage(message = message, type = "gameLog", uid = null)

                    GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)

                }
                3 -> {
                    gameRoom.players.forEach {
                        if (currentPlayer.value.uid == it.uid) {
                            currentPlayer.value = it
                        }
                    }
                    val result = Baron.compareCards(
                        player1 = currentPlayer.value,
                        player2 = selectedPlayer,
                        players = players
                    )
                    result.players?.let {
                        gameRoom.players = result.players!!
                    }
                    val logMessage = LogMessage.createLogMessage(result.message, type = "gameLog", uid = null)

                    updateResult(result)

                    GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
                }
                5 -> {

                    val result = Prince.discardAndDraw(
                        player1 = currentPlayer.value,
                        player2 = selectedPlayer,
                        gameRoom = gameRoom
                    )
                    val logMessage = LogMessage.createLogMessage(result.message, type = "gameLog", uid = null)

                    val updatedGame = result.game

                    if (updatedGame != null) {
                        GameRules.onEnd(gameRoom = updatedGame, logMessage = logMessage)
                    }

                }
                6 -> {
                    gameRoom.players.forEach {
                        Log.d("King", "(before)${it.nickName}'s hand is now ${it.hand}")
                    }
                    val result = King.swapCards(
                        player1 = currentPlayer.value,
                        player2 = selectedPlayer,
                        gameRoom = gameRoom
                    )
                    val logMessage = LogMessage.createLogMessage(result.message, type = "gameLog", uid = null)


                    gameRoom.players = result.players!!
                    gameRoom.players.forEach {
                        Log.d("King", "(after)${it.nickName}'s hand is now ${it.hand}")
                    }
                    GameRules.onEnd(gameRoom = gameRoom, logMessage)

                }
            }
        } else {
            Log.d("Handmaid", "Selected player was protected.")
            var message = "${selectedPlayer.nickName} was protected, nothing happened."
            val logMessage = LogMessage.createLogMessage(message, type = "gameLog", uid = null)

            GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
        }
        selectPlayerAlert.value = false
    }

    fun onGuess(card: Int, gameRoom: GameRoom) {
        guessCardAlert.value = false
        emptyCard.value = card
        val result = Guard.returnResult(
            player1 = currentPlayer.value,
            player2 = selectedPlayer.value,
            guessedCard = card
        )
        gameRoom.players.forEach {
            if (result.player2?.uid == it.uid) {
                it.isAlive = result.player2.isAlive
            }
        }
        val logMessage = LogMessage.createLogMessage(result.message, type = "gameLog", uid = null)

        updateResult(result)

        GameRules.onEnd(gameRoom = gameRoom, logMessage)

    }

    private fun updateResult(result: Result) {
        resultMessage.value = result.message
        resultAlert.value = true
    }

    fun observeRoom() {
        Log.d(TAG, "Observe room is being called again and again.")
        viewModelScope.launch {
            Log.d(TAG, "roomCode in viewModel: ${roomCode.value}")
            GameServer.subscribeToRealtimeUpdates(roomCode.value).map { gameRoom ->
                GameState.Loaded(
                    gameRoom = gameRoom
                )
            }.collect {
                loadingState.emit(it)
            }
        }
    }

    fun randomFloat(): Float {
        return (-5..5).random().toFloat()
    }

    fun removeCurrentPlayer(list: List<Player>): List<Player> {

        val newList = arrayListOf<Player>()
        list.forEach {
            if (it.uid != currentPlayer.value.uid) {
                Log.d(TAG, "removing player: $it")
                newList.add(it)
            }
        }
        Log.d(TAG, "removing current player: $list")
        Log.d(TAG, "Check size: ${list.size}")
        return newList.sortedBy { it.turnOrder }

    }

    fun assignRoomCode(roomCode2: String) {
        roomCode.value = roomCode2
    }

    fun eliminate(gameRoom: GameRoom, player: Player) {
        GameRules.eliminatePlayer(
            gameRoom = gameRoom,
            player = player
        )
    }
}