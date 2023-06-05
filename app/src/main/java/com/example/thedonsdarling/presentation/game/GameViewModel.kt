package com.example.thedonsdarling.presentation.game

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.*
import com.example.thedonsdarling.util.Tools
import com.example.thedonsdarling.util.game.GameServer
import com.example.thedonsdarling.util.game.gamerules.CardRules.*
import com.example.thedonsdarling.util.game.gamerules.GameRules
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val loadingState = MutableStateFlow<GameState>(GameState.Loading)

    val state = loadingState.asStateFlow()

    private val context = getApplication<Application>().applicationContext


    val roomCode = mutableStateOf("1234")
    val currentUser: FirebaseUser? = Firebase.auth.currentUser
    var localPlayer = mutableStateOf(Player())
    val selectedPlayer = mutableStateOf(Player())
    private val playedCard = mutableStateOf(0)
    var selectPlayerAlert = mutableStateOf(false)
    val guessCardAlert = mutableStateOf(false)
    val revealCardAlert = mutableStateOf(false)
    private val emptyCard = mutableStateOf(0)
    val resultAlert = mutableStateOf(false)
    val resultMessage = mutableStateOf("")
    val isHost = mutableStateOf(false)
    val endRoundAlert = mutableStateOf(false)
    var winner = mutableStateOf(Player())


    val chatOpen = mutableStateOf(false)
    val settingsOpen = mutableStateOf(false)


    val listOfPlayers = mutableStateOf(listOf(Player()))
    fun getSomeString(string: Int, variableString1: String?, variableString2: String?): String {
        var returningString = ""
        if (variableString1 != null && variableString2 == null) {
            returningString = getApplication<Application>().resources.getString(string)
        } else if (variableString1 != null && variableString2 != null) {
            returningString = getApplication<Application>().resources.getString(string)
        } else {
            getApplication<Application>().resources.getString(string)
        }
        return returningString
    }

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
                var toastMessage = ""
                gameRoom.players.forEach {
                    if (it.uid == localPlayer.value.uid) {
                        it.protected = TheDoctor.toggleProtection(it)
                        message = context.getString(R.string.card_doctor_message, it.nickName)
                        toastMessage =
                            context.getString(R.string.card_doctor_toast_message, it.nickName)
                    }
                }
                val logMessage =
                    LogMessage.createLogMessage(
                        message = message,
                        toastMessage = toastMessage,
                        type = "gameLog",
                        uid = null
                    )

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
                    LogMessage.createLogMessage(
                        message = context.getString(
                            R.string.card_courtesan_message,
                            localPlayer.value.nickName
                        ),
                        toastMessage = context.getString(
                            R.string.card_courtesan_toast_message,
                            localPlayer.value.nickName
                        ),
                        type = "gameLog",
                        uid = null
                    )
                GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
            }
            8 -> {
                val updatedGameRoom = GameRules.eliminatePlayer(
                    gameRoom = gameRoom,
                    player = player
                )
                GameRules.handlePlayedCard(
                    card = playedCard.value,
                    player = player,
                    gameRoom = updatedGameRoom
                )
                val result = Darling.eliminatePlayer(gameRoom, player)
                val logMessage =
                    LogMessage.createLogMessage(
                        message = context.getString(R.string.card_darling_message, player.nickName),
                        toastMessage = context.getString(
                            R.string.card_darling_toast_message,
                            player.nickName
                        ),
                        type = "gameLog",
                        uid = null
                    )
                result.game?.let { GameRules.onEnd(gameRoom = it, logMessage) }

            }
        }
    }

    fun onSelectPlayer(selectedPlayer: Player, gameRoom: GameRoom) {
        val players = gameRoom.players
        this.selectedPlayer.value = selectedPlayer
        gameRoom.players.forEach {
            if (localPlayer.value.uid == it.uid) {
                localPlayer.value = it
            }
        }
        val card = CardAvatar.setCardAvatar(playedCard.value)
        if (!selectedPlayer.protected) {
            when (playedCard.value) {
                1 -> {
                    guessCardAlert.value = true
                }
                2 -> {
                    revealCardAlert.value = true
                    emptyCard.value = selectedPlayer.hand.first()
                    val privateEyeMessage = context.getString(
                        R.string.card_private_eye_message,
                        localPlayer.value.nickName,
                        selectedPlayer.nickName
                    )
                    val logMessage =
                        LogMessage.createLogMessage(
                            message = privateEyeMessage,
                            toastMessage = privateEyeMessage,
                            type = "gameLog",
                            uid = null
                        )

                    GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)

                }
                3 -> {
                    gameRoom.players.forEach {
                        if (localPlayer.value.uid == it.uid) {
                            localPlayer.value = it
                        }
                    }
                    val result = Moneylender.compareCards(
                        player1 = localPlayer.value,
                        player2 = selectedPlayer,
                        players = players,
                        game = gameRoom
                    )
                    when (result.cardResult) {
                        is Moneylender.Player1Wins -> {
                            result.message = context.getString(
                                R.string.card_moneylender_message_win,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )
                            result.toastMessage = context.getString(
                                R.string.card_moneylender_toast_message_win,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )

                        }
                        is Moneylender.Player2Wins -> {
                            result.message = context.getString(
                                R.string.card_moneylender_message_lose,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )
                            result.toastMessage = context.getString(
                                R.string.card_moneylender_toast_message_lose,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )
                        }
                        is Moneylender.Draw -> {
                            result.message = context.getString(
                                R.string.card_moneylender_message_tie,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )
                            result.toastMessage = context.getString(
                                R.string.card_moneylender_toast_message_tie,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )
                        }
                    }
                    result.players?.let {
                        gameRoom.players = result.players!!
                    }
                    val logMessage =
                        LogMessage.createLogMessage(
                            result.message,
                            toastMessage = result.toastMessage,
                            type = "gameLog",
                            uid = null
                        )

                    updateResult(result)

                    result.game?.let { GameRules.onEnd(gameRoom = it, logMessage = logMessage) }
                }
                5 -> {
                    val result = Wiseguy.discardAndDraw(
                        player1 = localPlayer.value,
                        player2 = selectedPlayer,
                        gameRoom = gameRoom,
                    )
                    var player2Card =
                        context.getString(CardAvatar.setCardAvatar(selectedPlayer.hand.first()).cardName)

                    when (result.cardResult) {
                        is Wiseguy.ForcedToDiscard -> {
                            result.message = context.getString(
                                R.string.card_wiseguy_message,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName,
                                player2Card
                            )
                            result.toastMessage = context.getString(
                                R.string.card_wiseguy_toast_message,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName,
                                player2Card
                            )

                        }
                        is Wiseguy.ForcedToDiscardDarling -> {
                            result.message = context.getString(
                                R.string.card_darling_forced_message,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName,
                            )
                            result.toastMessage = context.getString(
                                R.string.card_darling_forced_toast_message,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName,
                            )
                        }
                        is Wiseguy.ForcedToDiscardAndEmptyDeck -> {
                            result.message = context.getString(
                                R.string.card_wiseguy_message_empty_deck,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName,
                                player2Card
                            )
                            result.toastMessage = context.getString(
                                R.string.card_wiseguy_toast_message_empty_deck,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName,
                                player2Card
                            )
                        }
                    }
                    val logMessage =
                        LogMessage.createLogMessage(
                            result.message,
                            toastMessage = result.toastMessage,
                            type = "gameLog",
                            uid = null
                        )

                    val updatedGame = result.game

                    if (updatedGame != null) {
                        GameRules.onEnd(gameRoom = updatedGame, logMessage = logMessage)
                    }

                }
                6 -> {

                    val result = TheDon.swapCards(
                        player1 = localPlayer.value,
                        player2 = selectedPlayer,
                        gameRoom = gameRoom
                    )
                    result.message = context.getString(
                        R.string.card_the_don_message,
                        localPlayer.value.nickName,
                        selectedPlayer.nickName
                    )
                    result.toastMessage = context.getString(
                        R.string.card_the_don_toast_message,
                        localPlayer.value.nickName,
                        selectedPlayer.nickName
                    )

                    val logMessage =
                        LogMessage.createLogMessage(
                            message = result.message,
                            toastMessage = result.toastMessage,
                            type = "gameLog",
                            uid = null
                        )


                    gameRoom.players = result.players!!

                    GameRules.onEnd(gameRoom = gameRoom, logMessage)

                }
            }
        } else {
            Log.d("Handmaid", "Selected player was protected.")
            val cardName = context.getString(card.cardName)
            var message = context.getString(
                R.string.card_doctor_protection_message,
                localPlayer.value.nickName,
                cardName,
                selectedPlayer.nickName
            )
            var toastMessage = context.getString(
                R.string.card_doctor_protection_toast_message,
                localPlayer.value.nickName,
                cardName,
                selectedPlayer.nickName
            )
            val logMessage = LogMessage.createLogMessage(
                message = message,
                toastMessage = toastMessage,
                type = "gameLog",
                uid = null
            )

            GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
        }
        selectPlayerAlert.value = false
    }

    fun onGuess(card: Int, gameRoom: GameRoom) {
        guessCardAlert.value = false
        emptyCard.value = card
        val result = Policeman.returnResult(
            player1 = localPlayer.value,
            player2 = selectedPlayer.value,
            guessedCard = card,
            gameRoom = gameRoom
        )
        var updatedGameRoom = gameRoom
        updatedGameRoom.players.forEach {
            if (result.player2?.uid == it.uid) {
                it.isAlive = result.player2.isAlive
            }
        }
        val cardName = context.getString(CardAvatar.setCardAvatar(card).cardName)
        when (result.cardResult) {
            is Policeman.GoodGuess -> {
                result.message = context.getString(
                    R.string.card_policemen_message_correct,
                    localPlayer.value.nickName,
                    selectedPlayer.value.nickName,
                    cardName
                )
                result.toastMessage = context.getString(
                    R.string.card_policemen_toast_message_correct,
                    localPlayer.value.nickName,
                    selectedPlayer.value.nickName,
                    cardName
                )
            }
            is Policeman.WrongGuess -> {
                result.message = context.getString(
                    R.string.card_policemen_message_incorrect,
                    localPlayer.value.nickName,
                    selectedPlayer.value.nickName,
                    context.getString(CardAvatar.setCardAvatar(card).cardName)
                )
                result.toastMessage = context.getString(
                    R.string.card_policemen_toast_message_incorrect,
                    localPlayer.value.nickName,
                    selectedPlayer.value.nickName,
                    cardName
                )
            }
        }
        val logMessage = LogMessage.createLogMessage(
            message = result.message,
            toastMessage = result.toastMessage,
            type = "gameLog",
            uid = null
        )

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

    fun removeCurrentPlayer(list: List<Player>): List<Player> {

        val newList = arrayListOf<Player>()
        list.forEach {
            Log.d(TAG, "Comparing uids: ${it.uid} and ${localPlayer.value.uid}")
            if (it.uid != localPlayer.value.uid) {
                Log.d(TAG, "adding player to new list: ${it.nickName}")
                newList.add(it)
            } else {
                Log.d(TAG, "Found local player: ${it.nickName}")
            }
        }
        Log.d(TAG, "removing current player: $newList")
        Log.d(TAG, "Check size: ${newList.size}")
        return newList.sortedBy { it.turnOrder }

    }

    fun assignRoomCode(roomCode2: String) {
        roomCode.value = roomCode2
    }

    /*LaunchedEffect functions*/
    fun declareIsHost(gameRoom: GameRoom) {
        val currentUser: FirebaseUser? = Firebase.auth.currentUser
        currentUser?.let {
            isHost.value = Tools.getHost(gameRoom.players, currentUser)
        }
    }

    fun localizeCurrentPlayer(
        game: GameRoom,
    ) {
        if (localPlayer.value.uid == "") {
            localPlayer.value = Tools.getPlayer(game.players, currentUser)
        }
    }

    fun checkRoundOver(gameRoom: GameRoom) {
        if (gameRoom.roundOver && !gameRoom.gameOver) {
            endRoundAlert.value = true
        }
    }

    fun getPlayerFromGameList(
        game: GameRoom,
    ): Player {
        game.players.forEach {
            if (it.uid == localPlayer.value.uid) {
                localPlayer.value = it
            }
        }
        return localPlayer.value
    }

    fun endRound(
        alivePlayers: List<Player>,
        game: GameRoom,
        playerIsPlaying: Boolean,
    ) {
        if (alivePlayers.size == 1 && !game.roundOver && !playerIsPlaying) {
            Log.d(TAG, "(if) ending game")
            GameRules.endRound(gameRoom = game, context = context)

            game.players.forEach {
                if (it.isWinner) {
                    winner.value = it
                }
            }
            endRoundAlert.value = true


        } else if (game.deck.deck.isEmpty() && !playerIsPlaying && !game.roundOver && game.deckClear) {
            Log.d(TAG, "(else-if) ending game")
            GameRules.endRound(gameRoom = game, context = context)

            var winner = Player()
            game.players.forEach {
                if (it.isWinner) {
                    winner = it
                }
            }
            endRoundAlert.value = true

        }
    }

    fun handleDeletedRoom(
        game: GameRoom,
        navController: NavController,
    ) {
        if (game.deleteRoom) {
            navController.navigate(Screen.Home.route)
        }
    }

}