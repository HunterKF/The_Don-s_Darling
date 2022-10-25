package com.example.loveletter.presentation.createroom

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.joingame.GameLobbyState
import com.example.loveletter.presentation.joingame.GameLobbyViewModel
import com.example.loveletter.util.joingame.JoinGame
import com.example.loveletter.util.startgame.StartGame
import com.example.loveletter.util.user.HandleUser

@Composable
fun GameLobby(navController: NavHostController, gameLobbyViewModel: GameLobbyViewModel) {

    val state by gameLobbyViewModel.state.collectAsState()


    when (state) {
        GameLobbyState.Loading -> {
            CircularProgressIndicator()
            LaunchedEffect(key1 = Unit) {
                gameLobbyViewModel.observeRoom()
            }
        }
        is GameLobbyState.Loaded -> {
            val loaded = state as GameLobbyState.Loaded

            GameLobbyContent(
                navController = navController,
                gameLobbyViewModel = gameLobbyViewModel,
                gameRoom = loaded.gameRoom)
            BackHandler() {
                JoinGame.leaveGame(gameLobbyViewModel.roomCode.value,
                    HandleUser.createPlayer(gameLobbyViewModel.playerChar.value,
                        gameLobbyViewModel.playerNickname.value))
                navController.navigate(Screen.Home.route)
            }
        }

    }


}

@Composable
private fun GameLobbyContent(
    navController: NavHostController,
    gameLobbyViewModel: GameLobbyViewModel,
    gameRoom: GameRoom,
) {

    Surface(Modifier.fillMaxSize()) {
        Box() {
            IconButton(modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp),
                onClick = {
                    JoinGame.leaveGame(gameRoom.roomCode, HandleUser.createPlayer(0, ""))
                    navController.navigate(Screen.Home.route)
                }) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    stringResource(R.string.go_back)
                )
            }
            Column(Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(gameRoom.roomCode, style = MaterialTheme.typography.h6)
                    IconButton(onClick = {
                        Log.d(TAG,
                            "Value for roomcode: ${gameRoom.roomCode}")
                    }) {
                        Icon(
                            Icons.Rounded.Share,
                            null
                        )
                    }
                }

                Text(
                    text = gameRoom.roomNickname,
                    style = MaterialTheme.typography.h2
                )

                Column(Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .alpha(0.5f)) {

                    gameRoom.players.forEach { player ->

                        val avatar = Avatar.setAvatar(player.avatar)
                        Row(Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically) {

                            Image(modifier = Modifier
                                .scale(0.7f)
                                .padding(4.dp)
                                .clip(CircleShape),
                                painter = painterResource(id = avatar.avatar),
                                contentDescription = avatar.description)

                            Text(player.nickName, style = MaterialTheme.typography.h6)
                        }
                    }
                }

            }
            IconButton(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp),
                onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Rounded.Email,
                    stringResource(R.string.open_chat)
                )
            }
        }
    }
}

