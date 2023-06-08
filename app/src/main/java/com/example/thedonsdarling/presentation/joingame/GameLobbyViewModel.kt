package com.example.thedonsdarling.presentation.joingame

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.models.CheckGameResult
import com.example.thedonsdarling.domain.models.JoinGameResult
import com.example.thedonsdarling.domain.use_cases.UseCases
import com.example.thedonsdarling.domain.util.user.HandleUser
import com.example.thedonsdarling.util.UiEvent
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
class GameLobbyViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val loadingState = MutableStateFlow<GameLobbyState>(GameLobbyState.Loading)
    val state = loadingState.asStateFlow()

    val roomCode = mutableStateOf("")
    val playerNickname = mutableStateOf("")
    val playerChar = mutableStateOf(0)

    val currentUser = Firebase.auth.currentUser


    fun clearData() {
        playerChar.value = -1
        roomCode.value = ""
        playerNickname.value = ""
    }

    fun observeRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.subscribeToRealtimeUpdates(roomCode.value).map { gameRoom ->
                GameLobbyState.Loaded(
                    gameRoom = gameRoom
                )
            }.collect {
                loadingState.emit(it)
            }
        }
    }

    fun assignCharNumber(index: Int): Int {
        return index
    }

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.RemovePlayer -> {
                val player = HandleUser.createGamePlayer(
                    playerChar.value,
                    playerNickname.value, isHost = false,
                    currentUser.uid
                )
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.removePlayerFromGame(
                        roomCode = roomCode.value,
                        player = player
                    )
                }
            }
            is UiEvent.CheckRoom -> {
                viewModelScope.launch {
                    val result = useCases.checkGame(roomCode.value)
                    val context = event.context
                    when (result) {
                        is CheckGameResult.GameFound -> {
                            Toast.makeText(
                                context,
                                context.getText(R.string.check_game_not_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is CheckGameResult.GameNotFound -> {
                            Toast.makeText(
                                context,
                                context.getText(R.string.check_game_not_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

            }
            is UiEvent.JoinGame -> {
                val context = event.context
                viewModelScope.launch(Dispatchers.IO) {
                    val result = useCases.joinGame(
                        roomCode.value, player = HandleUser.createGamePlayer(
                            avatar = playerChar.value,
                            nickname = playerNickname.value, isHost = false,
                            currentUser.uid
                        )
                    )
                    when (result) {
                        is JoinGameResult.GameFull -> {
                            Toast.makeText(
                                context,
                                context.getText(R.string.check_game_full),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is JoinGameResult.CodeNotFound -> {
                            Toast.makeText(
                                context,
                                context.getText(R.string.check_game_not_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is JoinGameResult.UnknownError -> {
                            Toast.makeText(
                                context,
                                context.getText(R.string.unknown_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is JoinGameResult.Success -> {
                            Toast.makeText(
                                context,
                                context.getText(R.string.successfully_joined_game),
                                Toast.LENGTH_SHORT
                            ).show()
                            event.navigate()
                        }
                    }
                }

            }
            is UiEvent.LeaveGame -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val game = event.gameRoom
                    useCases.removePlayerFromGame(
                        roomCode.value,
                        HandleUser.createGamePlayer(
                            playerChar.value,
                            playerNickname.value,
                            isHost = false,
                            currentUser.uid
                        )
                    )
                    useCases.removeSingleGameFromPlayerJoinedList(
                        roomCode.value, HandleUser.createGamePlayer(
                            playerChar.value,
                            playerNickname.value,
                            isHost = false,
                            currentUser.uid
                        ), roomNickname = game.roomNickname
                    )
                }
            }
        }
    }
}