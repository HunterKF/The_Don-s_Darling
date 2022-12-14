package com.example.loveletter.presentation.joingame

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loveletter.domain.Player
import com.example.loveletter.util.startgame.StartGame
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
}