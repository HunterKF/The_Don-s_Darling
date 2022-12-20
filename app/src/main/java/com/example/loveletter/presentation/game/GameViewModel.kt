package com.example.loveletter.presentation.game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loveletter.TAG
import com.example.loveletter.domain.GameRoom
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
                gameRoom.players.forEach {
                    if (it.uid == currentPlayer.value.uid) {
                        it.protected = Handmaid.toggleProtection(it)
                    }
                }
                GameRules.onEnd(gameRoom = gameRoom)
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
            }
            8 -> {
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = gameRoom
                )
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
        when (playedCard.value) {
            1 -> {
                guessCardAlert.value = true
            }
            2 -> {
                revealCardAlert.value = true
                emptyCard.value = selectedPlayer.hand.first()
                GameRules.onEnd(gameRoom = gameRoom)

            }
            3 -> {
                val result = Baron.compareCards(
                    player1 = currentPlayer.value,
                    player2 = selectedPlayer,
                    players = players
                )
                result.players?.let {
                    gameRoom.players = result.players!!
                }
                updateResult(result)

                GameRules.onEnd(gameRoom = gameRoom)
            }
            5 -> {
                val result = Prince.discardAndDraw(
                    player1 = currentPlayer.value,
                    player2 = selectedPlayer,
                    gameRoom = gameRoom
                )
                val updatedGame = result.game

                if (updatedGame != null) {
                    GameRules.onEnd(gameRoom = updatedGame)
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

                gameRoom.players = result.players!!
                gameRoom.players.forEach {
                    Log.d("King", "(after)${it.nickName}'s hand is now ${it.hand}")
                }
                GameRules.onEnd(gameRoom = gameRoom)

            }
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
        updateResult(result)

        GameRules.onEnd(gameRoom = gameRoom)

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
        list.forEach {
            if (it.uid == HandleUser.returnUser()?.uid) {
                list.minus(it)
            }
        }
        return list

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