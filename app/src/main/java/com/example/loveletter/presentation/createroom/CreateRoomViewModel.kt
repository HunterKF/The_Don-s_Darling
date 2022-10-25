package com.example.loveletter.presentation.createroom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.util.user.HandleUser
import com.example.loveletter.util.startgame.StartGame
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreateRoomViewModel : ViewModel() {
    var playerChar = mutableStateOf(0)
    private val loadingState = MutableStateFlow<CreateRoomState>(CreateRoomState.Loading)

    val state = loadingState.asStateFlow()
    val currentUser = Firebase.auth.currentUser

    val roomCode = mutableStateOf("1234")


    var roomNickname = mutableStateOf("")

    var playerNickname = mutableStateOf("")

    fun observeRoom() {
        viewModelScope.launch {
            StartGame.createRoom(roomNickname = roomNickname.value,
                0,
                listOf(HandleUser.createPlayer(playerChar.value, playerNickname.value)),
                roomCode.value)
            StartGame.subscribeToRealtimeUpdates(roomCode.value).map { gameRoom ->

                CreateRoomState.Loaded(
                    gameRoom = gameRoom
                )
            }.collect {
                loadingState.emit(it)
            }
        }
    }
}