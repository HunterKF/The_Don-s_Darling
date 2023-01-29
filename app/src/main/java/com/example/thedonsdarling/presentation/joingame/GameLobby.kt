package com.example.thedonsdarling.presentation.createroom

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.joingame.GameLobbyState
import com.example.thedonsdarling.presentation.joingame.GameLobbyViewModel
import com.example.thedonsdarling.presentation.util.RoomPlayerList
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.util.game.gamerules.gameserver.ConnectionRules
import com.example.thedonsdarling.util.user.HandleUser

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
                GameLobbyContent(
                    navController = navController,
                    gameLobbyViewModel = gameLobbyViewModel,
                    gameRoom = loaded.gameRoom)
                BackHandler() {
                    ConnectionRules.leaveGame(
                        gameLobbyViewModel.roomCode.value,
                        HandleUser.createGamePlayer(
                            gameLobbyViewModel.playerChar.value,
                            gameLobbyViewModel.playerNickname.value, isHost = false),
                    )
                    HandleUser.deleteUserGameRoomForAll(
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
    val context = LocalContext.current
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message, gameRoom.roomCode))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    Surface(
        color = Black
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
                            ConnectionRules.leaveGame(gameRoom.roomCode,
                                HandleUser.createGamePlayer(avatar = 0,
                                    nickname = "",
                                    isHost = false))
                            HandleUser.deleteUserGameRoomForAll(
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
                    .background(MaterialTheme.colors.onPrimary)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed( gameRoom.players) {  index, player ->
                            RoomPlayerList(
                                player = player,
                                index = index,
                                gameRoom = gameRoom
                            )
                        }
                    }
                }

            }
        }
    }
}

