package com.example.thedonsdarling.presentation.createroom

import android.content.Intent
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
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.util.CustomTextButton
import com.example.thedonsdarling.presentation.util.RoomPlayerList
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.domain.util.Tools
import com.example.thedonsdarling.util.UiEvent

@Composable
fun CreateRoom(
    createRoomViewModel: CreateRoomViewModel,
    gameViewModel: GameViewModel,
    onNavigateBack: () -> Boolean,
    onNavigateToScreen: () -> Unit,
    ) {

    val state by createRoomViewModel.state.collectAsState()


    when (state) {
        CreateRoomState.Loading -> {
            LaunchedEffect(key1 = Unit) {
                createRoomViewModel.roomCode.value = Tools.getRandomString()
                createRoomViewModel.observeRoom()
            }
        }
        is CreateRoomState.Loaded -> {
            val loaded = state as CreateRoomState.Loaded
            CreateRoomContent(
                onNavigateToScreen = { onNavigateToScreen() },
                onNavigateBack = { onNavigateBack() },
                createRoomViewModel = createRoomViewModel,
                gameRoom = loaded.gameRoom,
                gameViewModel = gameViewModel,
            )
            BackHandler() {
                gameViewModel.onUiEvent(UiEvent.DeleteRoom(gameRoom = loaded.gameRoom))
                onNavigateBack()
            }
        }

    }


}

@Composable
private fun CreateRoomContent(
    createRoomViewModel: CreateRoomViewModel,
    gameRoom: GameRoom,
    gameViewModel: GameViewModel,
    onNavigateToScreen: () -> Unit,
    onNavigateBack: () -> Boolean
) {
    val context = LocalContext.current
    var ready by remember { mutableStateOf(false) }
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message, gameRoom.roomCode))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    BackHandler() {
        onNavigateBack()
    }
    Surface(
        color = Black
    ) {
        Box(

        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 48.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                        onClick = {
                            createRoomViewModel.onUiEvent(UiEvent.DeleteRoom(gameRoom))
                            onNavigateBack()


                        }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            stringResource(R.string.go_back)
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            gameRoom.roomCode,
                            style = MaterialTheme.typography.h6,
                            color = Color.White
                        )
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


                Column(
                    Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .weight(1f)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.onPrimary)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(gameRoom.players) { index, player ->
                            RoomPlayerList(
                                player = player,
                                index = index,
                                gameRoom = gameRoom
                            )
                        }
                    }
                }



                if (!ready) {
                    CustomTextButton(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        enabled = gameRoom.players.size > 1,
                        text = stringResource(R.string.ready_question),
                        onClick = { ready = true })
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            modifier = Modifier
                                .padding(16.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onPrimary,
                                disabledBackgroundColor = Color.LightGray,
                                disabledContentColor = Color.Black
                            ),
                            onClick = {
                                gameViewModel.assignRoomCode(createRoomViewModel.roomCode.value)
                                gameViewModel.onUiEvent(
                                    UiEvent.InitialStart(
                                        gameRoom,
                                        context = context
                                    ) {
                                        onNavigateToScreen()
                                    })
                            }) {
                            Icon(
                                Icons.Rounded.Check,
                                stringResource(R.string.confirm_ready),
                                tint = Color.Black
                            )
                        }
                        Button(
                            modifier = Modifier
                                .padding(16.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onPrimary,
                                disabledBackgroundColor = Color.LightGray,
                                disabledContentColor = Color.Black
                            ),
                            onClick = { ready = false }) {
                            Icon(
                                Icons.Rounded.Close,
                                stringResource(R.string.cancel_ready),
                                tint = Color.Black
                            )
                        }
                    }
                }
            }

        }
    }
}



