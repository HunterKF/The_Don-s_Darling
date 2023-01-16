package com.example.loveletter.presentation.createroom

import android.content.Intent
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
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.Steel
import com.example.loveletter.util.Tools
import com.example.loveletter.util.game.gamerules.gameserver.GameServer
import com.example.loveletter.util.game.gamerules.gameserver.StartGame
import com.example.loveletter.util.user.HandleUser

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateRoom(
    navController: NavHostController,
    createRoomViewModel: CreateRoomViewModel,
    gameViewModel: GameViewModel,
) {

    val state by createRoomViewModel.state.collectAsState()


    when (state) {
        CreateRoomState.Loading -> {
            createRoomViewModel.roomCode.value = Tools.getRandomString()
            LaunchedEffect(key1 = Unit) {
                createRoomViewModel.observeRoom()
            }
        }
        is CreateRoomState.Loaded -> {
            val loaded = state as CreateRoomState.Loaded
            LaunchedEffect(key1 = Unit, block = {
//                HandleUser.addGameToUser(loaded.gameRoom.roomCode, loaded.gameRoom.roomNickname)
            })
            CreateRoomContent(
                navController = navController,
                createRoomViewModel = createRoomViewModel,
                gameRoom = loaded.gameRoom,
                gameViewModel = gameViewModel
            )
            BackHandler() {
                GameServer.deleteRoom(loaded.gameRoom)
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
        Box(

        ) {

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
                            navController.popBackStack()
                            GameServer.deleteRoom(game = gameRoom)
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
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(gameRoom.roomCode,
                            style = MaterialTheme.typography.h6,
                            color = Color.White)
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



                if (!ready) {
                    OutlinedButton(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        enabled = gameRoom.players.size > 1,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Navy,
                            contentColor = Color.White,
                            disabledBackgroundColor = Color.LightGray,
                            disabledContentColor = Color.Black
                        ),
                        onClick = { ready = true }) {
                        Text("Ready?")
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            modifier = Modifier
                                .padding(16.dp)
                                .border(1.dp, Steel, RoundedCornerShape(5.dp)),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Navy,
                                contentColor = Color.White,
                                disabledBackgroundColor = Color.LightGray,
                                disabledContentColor = Color.Black
                            ),
                            onClick = {
                                gameViewModel.assignRoomCode(createRoomViewModel.roomCode.value)
                                StartGame.startGame(
                                    gameRoom = gameRoom,
                                    context = context
                                ) {
                                    navController.navigate(Screen.Game.route)
                                }
                            }) {
                            Icon(Icons.Rounded.Check,
                                stringResource(R.string.confirm_ready),
                                tint = Steel)
                        }
                        Button(
                            modifier = Modifier
                                .padding(16.dp)
                                .border(1.dp, Steel, RoundedCornerShape(5.dp)),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Navy,
                                contentColor = Color.White,
                                disabledBackgroundColor = Color.LightGray,
                                disabledContentColor = Color.Black
                            ),
                            onClick = { ready = false }) {
                            Icon(Icons.Rounded.Close,
                                stringResource(R.string.cancel_ready),
                                tint = Steel)
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

