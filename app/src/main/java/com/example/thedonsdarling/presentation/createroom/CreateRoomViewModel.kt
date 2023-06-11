package com.example.thedonsdarling.presentation.createroom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.domain.util.user.HandleUser
import com.example.thedonsdarling.domain.use_cases.UseCases
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
class CreateRoomViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
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
        viewModelScope.launch(Dispatchers.IO) {
            val currentUserUid = Firebase.auth.currentUser?.uid
            if (currentUserUid != null) {
                val newRoom = useCases.createBlankRoom(
                    roomNickname = roomNickname.value,
                    playLimit = playLimit.value,
                    players = listOf(
                        HandleUser.createGamePlayer(
                            avatar = playerChar.value,
                            nickname = playerNickname.value,
                            isHost = true,
                            uid = currentUserUid
                        )
                    ),
                    roomCode = roomCode.value,
                    uid = currentUserUid
                )
                useCases.setGameInDB(newRoom)
            }
            useCases.subscribeToRealtimeUpdates(roomCode.value).map { gameRoom ->
                CreateRoomState.Loaded(
                    gameRoom = gameRoom
                )
            }.collect {
                loadingState.emit(it)
                println("GAMEROOM${it.gameRoom}")

            }
        }
    }

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.DeleteRoom -> {
                viewModelScope.launch(Dispatchers.IO) {
                    event.gameRoom.deleteRoom = true
                    useCases.setGameInDB(gameRoom = event.gameRoom)
                }

                /*GameServer.deleteRoom(game = event.gameRoom)
                HandleUser.deleteUserGameRoomForAll(
                    gameRoom.roomCode,
                    gameRoom.roomNickname,
                    gameRoom.players
                )*/
            }
        }
    }
}