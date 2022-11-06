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
import com.example.loveletter.util.startgame.StartGame
import com.example.loveletter.util.user.HandleUser

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateRoom(
    navController: NavHostController,
    createRoomViewModel: CreateRoomViewModel,
    gameViewModel: GameViewModel,
) {

    val state by createRoomViewModel.state.collectAsState()

    createRoomViewModel.roomCode.value = StartGame.getRandomString()

    when (state) {
        CreateRoomState.Loading -> {
            createRoomViewModel.roomCode.value = StartGame.getRandomString()
            LaunchedEffect(key1 = Unit) {
                createRoomViewModel.observeRoom()
            }
        }
        is CreateRoomState.Loaded -> {
            val loaded = state as CreateRoomState.Loaded
            LaunchedEffect(key1 = Unit, block = {
                HandleUser.addGameToUser(loaded.gameRoom.roomCode, loaded.gameRoom.roomNickname)
            })
            CreateRoomContent(
                navController = navController,
                createRoomViewModel = createRoomViewModel,
                gameRoom = loaded.gameRoom,
                gameViewModel = gameViewModel
            )
            BackHandler() {
                StartGame.deleteRoom(loaded.gameRoom.roomCode)
                HandleUser.deleteUserGameRoom(
                    loaded.gameRoom.roomCode,
                    loaded.gameRoom.roomNickname,
                    loaded.gameRoom.players
                )
                navController.popBackStack()
            }
        }

    }


}

@Composable
private fun CreateRoomContent(
    navController: NavHostController,
    createRoomViewModel: CreateRoomViewModel,
    gameRoom: GameRoom,
    gameViewModel: GameViewModel,
) {
    var ready by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(Modifier.fillMaxSize()) {
        Box() {
            IconButton(modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp),
                onClick = {
                    navController.popBackStack()
                    StartGame.deleteRoom(createRoomViewModel.roomCode.value)
                    HandleUser.deleteUserGameRoom(
                        gameRoom.roomCode,
                        gameRoom.roomNickname,
                        gameRoom.players
                    )

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



                if (!ready) {
                    OutlinedButton(onClick = { ready = true }) {
                        Text("Ready?")
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            gameViewModel.roomCode.value = createRoomViewModel.roomCode.value
                            StartGame.startGame(
                                gameRoom = gameRoom,
                                context = context
                            ) {
                                navController.navigate(Screen.Game.route)
                            }
                        }) {
                            Icon(Icons.Rounded.Check, stringResource(R.string.confirm_ready))
                        }
                        Button(onClick = { ready = false }) {
                            Icon(Icons.Rounded.Close, stringResource(R.string.cancel_ready))
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

