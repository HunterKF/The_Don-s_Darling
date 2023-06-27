package com.example.thedonsdarling.presentation.messenger

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.models.GameMessage
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.models.UiText
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.messenger.util.GameMessage
import com.example.thedonsdarling.presentation.messenger.util.PlayerMessage
import com.example.thedonsdarling.presentation.messenger.util.UserMessage
import com.example.thedonsdarling.presentation.util.Scoreboard
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.ui.theme.Navy
import com.example.thedonsdarling.ui.theme.OffWhite
import com.example.thedonsdarling.ui.theme.Steel
import com.example.thedonsdarling.util.UiEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun Messenger(gameRoom: GameRoom, gameViewModel: GameViewModel) {
    val chatMessage = remember {
        mutableStateOf("")
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = gameRoom.gameLog, block = {
        if (gameRoom.gameLog.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(gameRoom.gameLog.size - 1)
            }
        }
        gameViewModel.currentUserUid?.let {
            gameViewModel.onUiEvent(UiEvent.UpdateUnreadStatus(gameRoom))
            /*GameServer.updateUnreadStatusForLocal(gameRoom, it.uid) */
        }
    })
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Black
    ) {
        Column {
            Card(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomEnd = 15.dp,
                            bottomStart = 15.dp
                        )
                    )
            ) {
                Scaffold(
                    topBar = {
                        var showScoreboard by remember {
                            mutableStateOf(false)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .background(MaterialTheme.colors.primary)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = {
                                        gameViewModel.chatOpen.value = false
                                        gameViewModel.onUiEvent(UiEvent.UpdateUnreadStatus(gameRoom))
                                        /*GameServer.updateUnreadStatusForLocal(gameRoom,
                                            gameViewModel.currentUser!!.uid)*/
                                    },
                                    modifier = Modifier
                                ) {
                                    Icon(
                                        Icons.Rounded.ArrowBack,
                                        null,
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = gameRoom.roomNickname,
                                        style = MaterialTheme.typography.h6,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }
                                IconButton(
                                    onClick = { showScoreboard = !showScoreboard },
                                    modifier = Modifier
                                ) {
                                    Icon(
                                        Icons.Rounded.Menu,
                                        null,
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }
                            }
                            if (showScoreboard) {
                                Scoreboard(gameRoom)
                            }
                        }
                    }
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column {

                            LazyColumn(
                                state = listState,
                                contentPadding = PaddingValues(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(OffWhite)
                            ) {
                                items(gameRoom.gameLog) {
                                    val sdf = SimpleDateFormat("HH:mm")
                                    val date = sdf.format(it.date)
                                    when (it.type) {
                                        "userMessage" -> {
                                            val player =
                                                gameViewModel.listOfPlayers.value.first { player ->
                                                    it.uid == player.uid
                                                }
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                if (player.uid == gameViewModel.localPlayer.value.uid) {
                                                    it.chatMessage?.let { message ->
                                                        UserMessage(
                                                            message = UiText.DynamicString(message)
                                                                .asString(),
                                                            time = date,
                                                        )
                                                    }
                                                } else {
                                                    it.chatMessage?.let { message ->
                                                        PlayerMessage(
                                                            message = UiText.DynamicString(message)
                                                                .asString(),
                                                            avatar = player.avatar,
                                                            nickName = player.nickName,
                                                            time = date
                                                        )
                                                    }

                                                }
                                            }

                                        }
                                        "winnerMessage" -> {
                                            it.gameMessage?.let { message ->
                                                GameMessage(
                                                    message = message.gameMessageType,
                                                    time = date
                                                )
                                            }
                                        }
                                        "gameLog" -> {
                                            it.gameMessage?.let { gameMessage ->
                                                GameMessage.fromMessageReturnMessageString(
                                                    gameMessage = gameMessage
                                                )
                                            }?.let { value ->
                                                GameMessage(
                                                    message = value,
                                                    time = date
                                                )
                                            }
                                        }
                                        "serverMessage" -> {
                                            it.gameMessage?.let { gameMessage ->
                                                GameMessage.fromMessageReturnMessageString(
                                                    gameMessage = gameMessage
                                                )
                                            }?.let { value ->
                                                GameMessage(
                                                    message = value,
                                                    time = date
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }


                    }
                }
            }
            TextField(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .fillMaxWidth(),
                value = chatMessage.value,
                onValueChange = { newValue ->
                    chatMessage.value = newValue
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Navy,
                    textColor = Steel,
                    cursorColor = Steel,
                    focusedLabelColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Steel
                ),
                placeholder = {
                    Text(
                        stringResource(R.string.messenger_type_here),
                        fontSize = 16.sp,
                        color = Steel
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        gameViewModel.onUiEvent(
                            UiEvent.SendMessage(
                                gameRoom, LogMessage.createLogMessage(
                                    chatMessage = chatMessage.value,
                                    uid = gameViewModel.localPlayer.value.uid,
                                    type = "userMessage",
                                    gameMessage = null
                                )
                            )
                        )
                        /*GameServer.sendMessage(
                            gameRoom = gameRoom,
                            LogMessage.createLogMessage(
                                message = message.value,
                                toastMessage = null,
                                uid = gameViewModel.localPlayer.value.uid,
                                type = "userMessage"
                            )
                        )*/
                        chatMessage.value = ""
                    }) {
                        Icon(
                            Icons.Outlined.Send,
                            null,
                            tint = Color.White
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        gameViewModel.onUiEvent(
                            UiEvent.SendMessage(
                                gameRoom, LogMessage.createLogMessage(
                                    chatMessage = chatMessage.value,
                                    uid = gameViewModel.localPlayer.value.uid,
                                    type = "userMessage",
                                    gameMessage = null
                                )
                            )
                        )
                        /*GameServer.sendMessage(
                            gameRoom = gameRoom,
                            LogMessage.createLogMessage(
                                message = message.value,
                                toastMessage = null,
                                uid = gameViewModel.localPlayer.value.uid,
                                type = "userMessage"
                            )
                        )*/
                        chatMessage.value = ""
                    }
                )
            )
        }


    }
    BackHandler {
        gameViewModel.chatOpen.value = false
        gameViewModel.onUiEvent(UiEvent.UpdateUnreadStatus(gameRoom))
//        GameServer.updateUnreadStatusForLocal(gameRoom, gameViewModel.currentUser!!.uid)
    }

}

