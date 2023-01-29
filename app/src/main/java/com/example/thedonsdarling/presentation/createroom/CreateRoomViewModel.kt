package com.example.thedonsdarling.presentation.createroom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.util.user.HandleUser
import com.example.thedonsdarling.util.game.gamerules.gameserver.StartGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreateRoomViewModel : ViewModel() {
    var playLimit = mutableStateOf(5)
    var playerChar = mutableStateOf(0)
    private val loadingState = MutableStateFlow<CreateRoomState>(CreateRoomState.Loading)

    val state = loadingState.asStateFlow()

    val roomCode = mutableStateOf("1234")


    var roomNickname = mutableStateOf("")

    var playerNickname = mutableStateOf("")

    fun assignCharNumber(index: Int): Int {
        return index
    }
    fun clearRoomData() {
        roomCode.value = ""
        roomNickname.value = ""
        playerNickname.value = ""
        playerChar.value = 0
    }
    fun observeRoom() {
        viewModelScope.launch {
            StartGame.createRoom(roomNickname = roomNickname.value,
                playLimit.value,
                listOf(HandleUser.createGamePlayer(
                    avatar = playerChar.value,
                    nickname = playerNickname.value,
                    isHost = true)),
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