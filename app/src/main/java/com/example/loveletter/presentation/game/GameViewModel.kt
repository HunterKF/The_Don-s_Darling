package com.example.loveletter.presentation.game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loveletter.TAG
import com.example.loveletter.domain.Player
import com.example.loveletter.util.game.GameServer
import com.example.loveletter.util.user.HandleUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class GameViewModel : ViewModel() {

    private val loadingState = MutableStateFlow<GameState>(GameState.Loading)

    val state = loadingState.asStateFlow()


    val roomCode = mutableStateOf("1234")

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
}