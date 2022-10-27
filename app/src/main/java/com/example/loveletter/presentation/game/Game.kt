package com.example.loveletter.presentation.game

import androidx.compose.foundation.layout.Row
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.loveletter.domain.GameRoom

@Composable
fun Game(navController: NavController, gameViewModel: GameViewModel) {

    val state = gameViewModel.state.collectAsState()

    when (state.value) {
        GameState.Loading -> {
            CircularProgressIndicator()
        }
        is GameState.Loaded -> {
            val loaded = state as GameState.Loaded
            GameContent(loaded.gameRoom)
        }
    }
}

@Composable
fun GameContent(game: GameRoom) {
    Text(game.roomNickname)
    Row() {
        game.players.forEach {
            Text(it.nickName)
        }
    }
}