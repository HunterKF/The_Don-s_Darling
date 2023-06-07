package com.example.thedonsdarling.presentation.joingame

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.data.gameserver.StartGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GameLobbyViewModel : ViewModel() {
    private val loadingState = MutableStateFlow<GameLobbyState>(GameLobbyState.Loading)
    val state = loadingState.asStateFlow()

    val roomCode = mutableStateOf("")
    val playerNickname = mutableStateOf("")
    val playerChar = mutableStateOf(0)



    fun clearData() {
       playerChar.value = -1
       roomCode.value = ""
       playerNickname.value = ""
    }
    fun observeRoom() {
        viewModelScope.launch {
            StartGame.subscribeToRealtimeUpdates(roomCode.value).map { gameRoom ->
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
}