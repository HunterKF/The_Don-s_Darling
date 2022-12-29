package com.example.loveletter.presentation.createroom

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.joingame.GameLobbyState
import com.example.loveletter.presentation.joingame.GameLobbyViewModel
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.Steel
import com.example.loveletter.util.joingame.JoinGame
import com.example.loveletter.util.user.HandleUser

@Composable
fun GameLobby(
    navController: NavHostController,
    gameLobbyViewModel: GameLobbyViewModel,
    gameViewModel: GameViewModel,
) {

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
            if (loaded.gameRoom.start) {
                LaunchedEffect(key1 = Unit) {
                    gameViewModel.assignRoomCode(roomCode2 = loaded.gameRoom.roomCode)
                    navController.navigate(Screen.Game.route)
                }
            } else {
                Log.d(TAG, "Game lobby is refreshing...")
                LaunchedEffect(key1 = Unit, block = {
//                    HandleUser.addGameToUser(loaded.gameRoom.roomCode, loaded.gameRoom.roomNickname)
                })
                GameLobbyContent(
                    navController = navController,
                    gameLobbyViewModel = gameLobbyViewModel,
                    gameRoom = loaded.gameRoom)
                BackHandler() {
                    JoinGame.leaveGame(gameLobbyViewModel.roomCode.value,
                        HandleUser.createGamePlayer(gameLobbyViewModel.playerChar.value,
                            gameLobbyViewModel.playerNickname.value, isHost = false))
                    HandleUser.deleteUserGameRoom(
                        loaded.gameRoom.roomCode,
                        loaded.gameRoom.roomNickname,
                        loaded.gameRoom.players
                    )
                    navController.navigate(Screen.Home.route)
                }
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
    var ready by remember { mutableStateOf(false) }
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Let's play a game! Here's the code: ${gameRoom.roomCode}")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current
    Surface(
        color = DarkNavy
    ) {
        Box() {

            Column(Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                        onClick = {
                            JoinGame.leaveGame(gameRoom.roomCode,
                                HandleUser.createGamePlayer(avatar = 0,
                                    nickname = "",
                                    isHost = false))
                            HandleUser.deleteUserGameRoom(
                                gameRoom.roomCode,
                                gameRoom.roomNickname,
                                gameRoom.players
                            )
                            navController.navigate(Screen.Home.route)
                        }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            stringResource(R.string.go_back),
                            tint = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(gameRoom.roomCode, style = MaterialTheme.typography.h6, color = Color.White)
                        IconButton(onClick = {

                            Log.d(TAG,
                                "Value for room code: ${gameRoom.roomCode}")
                            context.startActivity(shareIntent)

                        }) {
                            Icon(
                                Icons.Rounded.Share,
                                null,
                                tint = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = gameRoom.roomNickname,
                    style = MaterialTheme.typography.h2,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(24.dp))

                Column(Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Steel)) {

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

