package com.example.thedonsdarling.presentation.game

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.*
import com.example.thedonsdarling.domain.models.*
import com.example.thedonsdarling.domain.preferences.Preferences
import com.example.thedonsdarling.domain.use_cases.UseCases
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.*
import com.example.thedonsdarling.domain.util.Tools
import com.example.thedonsdarling.domain.util.game.gamerules.EndRoundResult
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules
import com.example.thedonsdarling.util.UiEvent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(
    private val useCases: UseCases,
    private val preferences: Preferences,
) : ViewModel() {

    private val loadingState = MutableStateFlow<GameState>(GameState.Loading)

    val state = loadingState.asStateFlow()

    val roomCode = mutableStateOf("1234")
    val currentUserUid = useCases.getUid()
    var localPlayer = mutableStateOf(Player())
    val selectedPlayer = mutableStateOf(Player())
    private val playedCard = mutableStateOf(0)
    var selectPlayerAlert = mutableStateOf(false)
    val guessCardAlert = mutableStateOf(false)
    val revealCardAlert = mutableStateOf(false)
    private val emptyCard = mutableStateOf(0)
    val resultAlert = mutableStateOf(false)
    val isHost = mutableStateOf(false)
    val endRoundAlert = mutableStateOf(false)
    var winner = mutableStateOf(Player())

    val showGuides = mutableStateOf(preferences.getGuideEnabled())


    val chatOpen = mutableStateOf(false)
    val settingsOpen = mutableStateOf(false)


    val listOfPlayers = mutableStateOf(listOf(Player()))

    fun onPlay(card: Int, gameRoom: GameRoom, player: Player, context: Context) {
        playedCard.value = card
        when (playedCard.value) {
            1 -> {
                selectPlayerAlert.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                }


            }
            2 -> {
                selectPlayerAlert.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                }
            }
            3 -> {
                selectPlayerAlert.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                }
            }
            4 -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                    var message: UiText = UiText.DynamicString("")
                    var toastMessage: UiText = UiText.DynamicString("")
                    gameRoom.players.forEach {
                        if (it.uid == localPlayer.value.uid) {
                            it.protected = TheDoctor.toggleProtection(it)
                            message =
                                UiText.StringResource(R.string.card_doctor_message, it.nickName)
                            toastMessage =
                                UiText.StringResource(
                                    R.string.card_doctor_toast_message,
                                    it.nickName
                                )
                        }
                    }
                    val logMessage =
                        LogMessage.createLogMessage(
                            chatMessage = null,
                            gameMessage = GameMessage(
                                gameMessageType = GameMessageType.TheDoctor.messageType,
                                players = null,
                                player1 = gameRoom.players.first { it.uid == localPlayer.value.uid },
                                player2 = null
                            ),
                            type = "gameLog",
                            uid = null
                        )
                    useCases.setGameInDB(
                        GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
                    )
                }

            }
            5 -> {
                selectPlayerAlert.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                }
            }
            6 -> {
                selectPlayerAlert.value = true
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                }
            }
            7 -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = gameRoom
                        )
                    )
                    val logMessage =
                        LogMessage.createLogMessage(
                            chatMessage = null,
                            gameMessage = GameMessage(
                                gameMessageType = GameMessageType.Courtesan.messageType,
                                players = null,
                                player1 = gameRoom.players.first { it.uid == localPlayer.value.uid },
                                player2 = null
                            ),
                            type = "gameLog",
                            uid = null
                        )
                    useCases.setGameInDB(
                        GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)

                    )
                }

//                GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
            }
            8 -> {

                viewModelScope.launch(Dispatchers.IO) {
                    val updatedGameRoom = GameRules.eliminatePlayer(
                        gameRoom = gameRoom,
                        player = player
                    )
                    useCases.setGameInDB(
                        GameRules.handlePlayedCard(
                            card = playedCard.value,
                            player = player,
                            gameRoom = updatedGameRoom
                        )
                    )
                    val result = Darling.eliminatePlayer(gameRoom, player)
                    val logMessage =
                        LogMessage.createLogMessage(
                            chatMessage = null,
                            gameMessage = GameMessage(
                                gameMessageType = GameMessageType.Darling.messageType,
                                players = null,
                                player1 = player,
                                player2 = null
                            ),
                            type = "gameLog",
                            uid = null
                        )
                    result.game?.let {
                        useCases.setGameInDB(
                            GameRules.onEnd(
                                gameRoom = it,
                                logMessage
                            )
                        )
                    }

                }

            }
        }
    }

    fun onSelectPlayer(selectedPlayer: Player, gameRoom: GameRoom, context: Context) {
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
                    viewModelScope.launch(Dispatchers.IO) {
                        revealCardAlert.value = true
                        emptyCard.value = selectedPlayer.hand.first()
                        val logMessage =
                            LogMessage.createLogMessage(
                                chatMessage = null,
                                gameMessage = GameMessage(
                                    gameMessageType = GameMessageType.PrivateEye.messageType,
                                    players = null,
                                    player1 = localPlayer.value,
                                    player2 = selectedPlayer
                                ),
                                type = "gameLog",
                                uid = null
                            )

                        useCases.setGameInDB(
                            GameRules.onEnd(
                                gameRoom = gameRoom,
                                logMessage
                            )
                        )
                    }


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
                            result.message = GameMessageType.MoneyLenderP1Win
                        }
                        is Moneylender.Player2Wins -> {
                            result.message = GameMessageType.MoneyLenderP2Win


                            UiText.StringResource(
                                R.string.card_moneylender_message_lose,
                                localPlayer.value.nickName,
                                selectedPlayer.nickName
                            )
                        }
                        is Moneylender.Draw -> {
                            result.message = GameMessageType.MoneyLenderDraw
                        }
                    }
                    result.players?.let {
                        gameRoom.players = result.players
                    }
                    val logMessage =
                        LogMessage.createLogMessage(
                            chatMessage = null,
                            gameMessage = GameMessage(
                                gameMessageType = result.message!!.messageType,
                                players = null,
                                player1 = localPlayer.value,
                                player2 = selectedPlayer
                            ),
                            type = "gameLog",
                            uid = null
                        )



                    viewModelScope.launch(Dispatchers.IO) {
                        result.game?.let {
                            useCases.setGameInDB(
                                GameRules.onEnd(gameRoom = it, logMessage = logMessage)

                            )
                        }
                    }
                }
                5 -> {
                    val result = Wiseguy.discardAndDraw(
                        player1 = localPlayer.value,
                        player2 = selectedPlayer,
                        gameRoom = gameRoom,
                    )

                    when (result.cardResult) {
                        is Wiseguy.ForcedToDiscard -> {
                            result.message = GameMessageType.WiseGuyForcedToDiscard

                        }
                        is Wiseguy.ForcedToDiscardDarling -> {
                            result.message = GameMessageType.WiseGuyForcedToDiscardDarling

                        }
                        is Wiseguy.ForcedToDiscardAndEmptyDeck -> {
                            result.message = GameMessageType.WiseGuyForcedToDiscardAndEmptyDeck

                        }
                    }
                    val logMessage =
                        LogMessage.createLogMessage(
                            null,
                            gameMessage = GameMessage(
                                gameMessageType = result.message!!.messageType,
                                players = null,
                                player1 = localPlayer.value,
                                player2 = selectedPlayer,
                                usedCard = selectedPlayer.hand.first()
                            ),
                            type = "gameLog",
                            uid = null
                        )

                    val updatedGame = result.game

                    if (updatedGame != null) {
                        viewModelScope.launch(Dispatchers.IO) {
                            useCases.setGameInDB(
                                GameRules.onEnd(gameRoom = updatedGame, logMessage = logMessage)

                            )
                        }
                    }

                }
                6 -> {

                    val result = TheDon.swapCards(
                        player1 = localPlayer.value,
                        player2 = selectedPlayer,
                        gameRoom = gameRoom
                    )

                    val logMessage =
                        LogMessage.createLogMessage(
                            chatMessage = null,
                            gameMessage = GameMessage(
                                gameMessageType = result.message!!.messageType,
                                players = null,
                                player1 = localPlayer.value,
                                player2 = selectedPlayer
                            ),
                            type = "gameLog",
                            uid = null
                        )


                    gameRoom.players = result.players!!

                    viewModelScope.launch(Dispatchers.IO) {
                        useCases.setGameInDB(
                            GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)

                        )
                    }

                }
            }
        } else {
            /*If a player is protected using the doctor, this code is activated.*/
            val cardName = UiText.StringResource(card.cardName)
            val logMessage = LogMessage.createLogMessage(
                chatMessage = null,
                gameMessage = GameMessage(
                    gameMessageType = GameMessageType.TheDoctorProtected.messageType,
                    players = null,
                    player1 = localPlayer.value,
                    player2 = selectedPlayer,
                    usedCard = playedCard.value
                ),
                type = "gameLog",
                uid = null
            )
            viewModelScope.launch(Dispatchers.IO) {
                useCases.setGameInDB(
                    GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
                )
            }
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
        val updatedGameRoom = gameRoom
        updatedGameRoom.players.forEach {
            if (result.player2?.uid == it.uid) {
                it.isAlive = result.player2.isAlive
            }
        }
        val cardName = UiText.StringResource(CardAvatar.setCardAvatar(card).cardName)
        when (result.cardResult) {
            is Policeman.CorrectGuess -> {
                result.message = GameMessageType.PoliceCorrect
            }
            is Policeman.WrongGuess -> {
                result.message = GameMessageType.PoliceWrong

            }
        }

        val logMessage = LogMessage.createLogMessage(
            chatMessage = null,
            gameMessage = GameMessage(
                gameMessageType = GameMessageType.TheDoctorProtected.messageType,
                players = null,
                player1 = localPlayer.value,
                player2 = selectedPlayer.value,
                usedCard = card
            ),
            type = "gameLog",
            uid = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            useCases.setGameInDB(
                GameRules.onEnd(gameRoom = gameRoom, logMessage = logMessage)
            )
        }

    }

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ObserveRoom -> {
                viewModelScope.launch {
                    Log.d(TAG, "roomCode in viewModel: ${roomCode.value}")
                    useCases.subscribeToRealtimeUpdates(roomCode.value).map { gameRoom ->
                        GameState.Loaded(
                            gameRoom = gameRoom
                        )
                    }.collect {
                        loadingState.emit(it)
                    }
                }
            }
            is UiEvent.InitialStart -> {

                val logMessage = LogMessage.createLogMessage(
                    chatMessage = null,
                    gameMessage = GameMessage(
                        gameMessageType = GameMessageType.GameStart.messageType,
                        players = null,
                        player1 = localPlayer.value,
                        player2 = selectedPlayer.value
                    ),
                    type = "serverMessage",
                    uid = null
                )
                val newGame = useCases.startGame(gameRoom = event.gameRoom, logMessage = logMessage)
                viewModelScope.launch(Dispatchers.IO) {

                    useCases.setGameInDB(newGame)
                }
                event.onNavigate()
            }
            is UiEvent.CreateUserPlayer -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (currentUserUid != null) {
                        useCases.createUserPlayer(currentUserUid)
                    }
                }
            }
            is UiEvent.DeleteRoom -> {
                viewModelScope.launch(Dispatchers.IO) {
                    event.gameRoom.deleteRoom = true
                    useCases.setGameInDB(event.gameRoom)
                    useCases.deleteRoomFromFirestore
                    useCases.deleteUserGameRoomForAll(
                        event.gameRoom.roomCode,
                        event.gameRoom.roomNickname,
                        event.gameRoom.players
                    )
                }
            }
            is UiEvent.ExitGame -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteUserGameRoomForLocal(
                        roomCode.value,
                        roomNickname = event.gameRoom.roomNickname,
                        localPlayer.value
                    )
                    useCases.removePlayerFromGame(roomCode.value, localPlayer.value)
                    useCases.deleteUserGameRoomForLocal(
                        roomCode.value,
                        event.gameRoom.roomNickname,
                        player = localPlayer.value
                    )
                }
            }
            is UiEvent.EndRound -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        endRound(
                            event.alivePlayers,
                            event.game,
                            event.playerIsPlaying
                        )
                    )
                }
            }
            is UiEvent.StartRound -> {

                viewModelScope.launch(Dispatchers.IO) {
                    useCases.setGameInDB(
                        useCases.startNewRound(gameRoom = event.gameRoom)
                    )
                }
                endRoundAlert.value = false
                resultAlert.value = false
            }
            is UiEvent.StartNewGame -> {
                //this happens when a player's wins == win limit.
                useCases.startNewGame(event.gameRoom)
                endRoundAlert.value = false
                resultAlert.value = false
            }
            is UiEvent.UpdateUnreadStatus -> {
                viewModelScope.launch(Dispatchers.IO) {
                    currentUserUid?.let {
                        useCases.updateUnreadStatusForLocal(
                            gameRoom = event.gameRoom,
                            currentUserUid
                        )
                    }

                }
            }
            is UiEvent.SendMessage -> {
                viewModelScope.launch(Dispatchers.IO) {

                    useCases.sendMessage(gameRoom = event.gameRoom, logMessage = event.logMessage)
                    currentUserUid?.let {
                        useCases.updateUnreadStatusForAll(
                            gameRoom = event.gameRoom,
                            currentUserUid
                        )
                    }
                }
            }

        }
    }


    /* fun observeRoom() {
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
     }*/

    fun removeCurrentPlayer(list: List<Player>): List<Player> {
        //This is used for the table, it removes the player so it won't display on the table.
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
        if (localPlayer.value.uid == "" && currentUserUid != null) {
            localPlayer.value = Tools.getPlayer(game.players, currentUserUid)
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

    private fun endRound(
        alivePlayers: List<Player>,
        game: GameRoom,
        playerIsPlaying: Boolean
    ): GameRoom {
        if (alivePlayers.size == 1 && !game.roundOver && !playerIsPlaying) {
//            Log.d(TAG, "(if) ending game")
            val endGame = GameRules.endRound(gameRoom = game)
            val logMessage = LogMessage.createLogMessage(
                chatMessage = null,
                gameMessage = null,
                type = "winnerMessage",
                uid = null
            )

            endGame.gameRoom.players.forEach {
                if (it.isWinner) {
                    winner.value = it
                }
            }
            endRoundAlert.value = true
            logMessage.gameMessage = updateLogMessageFromEndGame(endGame, logMessage)
            endGame.gameRoom.gameLog.add(logMessage)
            return endGame.gameRoom
//            GameServer.updateGame(endGame.gameRoom)
        } else if (game.deck.deck.isEmpty() && !playerIsPlaying && !game.roundOver && game.deckClear) {
            Log.d(TAG, "(else-if) ending game")
            val endGame = GameRules.endRound(gameRoom = game)

            val logMessage = LogMessage.createLogMessage(
                chatMessage = null,
                gameMessage = null,
                type = "winnerMessage",
                uid = null
            )
            /*var winner = Player()
            game.players.forEach {
                if (it.isWinner) {
                    winner = it
                }
            }*/
            endRoundAlert.value = true
            logMessage.gameMessage = updateLogMessageFromEndGame(endGame, logMessage)

            endGame.gameRoom.gameLog.add(logMessage)
            return endGame.gameRoom

//            GameServer.updateGame(endGame.gameRoom)
        }
        return game
    }

    private fun updateLogMessageFromEndGame(
        endGame: EndRoundResult.FinalResult,
        logMessage: LogMessage
    ): GameMessage {
        when (endGame.type) {
            is EndRoundResult.RoundIsOverWinner -> {
                logMessage.gameMessage = GameMessage(
                    gameMessageType = GameMessageType.RoundOverWinner.messageType,
                    players = null,
                    player1 = winner.value,
                    player2 = null
                )
                return logMessage.gameMessage!!

            }
            is EndRoundResult.RoundIsOverTie -> {
                var names = listOf<Player>()
                for (player in endGame.remainingPlayers) {
                    names = names.plus(player)
                }
                logMessage.gameMessage = when (endGame.remainingPlayers.size) {
                    2 -> {
                        GameMessage(
                            gameMessageType = GameMessageType.RoundOverTie2.messageType,
                            players = names,
                            player1 = null,
                            player2 = null
                        )
                    }
                    3 -> {

                        GameMessage(
                            gameMessageType = GameMessageType.RoundOverTie3.messageType,
                            players = names,
                            player1 = null,
                            player2 = null
                        )
                    }
                    4 -> {

                        GameMessage(
                            gameMessageType = GameMessageType.RoundOverTie4.messageType,
                            players = names,
                            player1 = null,
                            player2 = null
                        )
                    }
                    else -> {
                        GameMessage(
                            gameMessageType = GameMessageType.RoundOverTie2.messageType,
                            players = names,
                            player1 = null,
                            player2 = null
                        )
                    }
                }
                return logMessage.gameMessage!!
            }
            is EndRoundResult.GameIsOver -> {
                logMessage.gameMessage = GameMessage(
                    gameMessageType = GameMessageType.GameOver.messageType,
                    players = null,
                    player1 = null,
                    player2 = null
                )
                return logMessage.gameMessage!!

            }
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