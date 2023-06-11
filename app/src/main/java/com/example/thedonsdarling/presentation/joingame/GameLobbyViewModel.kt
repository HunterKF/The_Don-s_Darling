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
                                context.getText(R.string.check_game_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is CheckGameResult.GameNotFound -> {
                            println("Game not found")
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
                var message = context.getString(R.string.unknown_error)
                viewModelScope.launch {
                    currentUser?.let {
                        val result = useCases.joinGame(
                            roomCode.value, player = HandleUser.createGamePlayer(
                                avatar = playerChar.value,
                                nickname = playerNickname.value, isHost = false,
                                currentUser.uid
                            )
                        )
                        when (result) {
                            is JoinGameResult.GameFull -> {
                                message = context.getString(R.string.check_game_full)

                            }
                            is JoinGameResult.CodeNotFound -> {
                                message = context.getString(R.string.check_game_not_found)

                            }
                            is JoinGameResult.UnknownError -> {
                                message = context.getString(R.string.unknown_error)
                            }
                            is JoinGameResult.Success -> {
                                message = context.getString(R.string.successfully_joined_game)
                                event.navigate()
                            }
                        }
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } ?: Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()

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