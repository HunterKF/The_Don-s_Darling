package com.example.loveletter.presentation.mygames

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.FirestoreUser
import com.example.loveletter.domain.JoinedGame
import com.example.loveletter.presentation.game.GameViewModel

@Composable
fun MyGames(
    navController: NavHostController,
    myGamesViewModel: MyGamesViewModel,
    gameViewModel: GameViewModel,
) {

    val state by myGamesViewModel.state.collectAsState()

    when (state) {
        MyGamesState.Loading -> {
            CircularProgressIndicator()
            LaunchedEffect(key1 = Unit) {
                myGamesViewModel.observeRoom()
            }
        }
        is MyGamesState.Loaded -> {
            val loaded = state as MyGamesState.Loaded

            MyGamesContent(loaded.firestoreUser, navController, gameViewModel)
        }
    }
}

@Composable
fun MyGamesContent(
    firestoreUser: FirestoreUser,
    navController: NavHostController,
    gameViewModel: GameViewModel,
) {
    Surface() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("My games",
                style = MaterialTheme.typography.h1)
            LazyColumn() {
                items(firestoreUser.joinedGames) { game ->
                    if (game.ready) {
                        JoinedGameCard(game) {
                            gameViewModel.roomCode.value = game.roomCode
                            Log.d(TAG, game.roomCode)
                            navController.navigate(Screen.Game.route) {
                                this.popUpToId
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JoinedGameCard(game: JoinedGame, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick()
        }
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(game.roomNickname)
            Text(game.roomCode)
        }
    }
}