package com.example.loveletter.presentation.messenger

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.LogMessage
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.messenger.util.GameMessage
import com.example.loveletter.presentation.messenger.util.PlayerMessage
import com.example.loveletter.presentation.messenger.util.UserMessage
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.OffWhite
import com.example.loveletter.ui.theme.Steel
import com.example.loveletter.util.game.GameServer
import java.text.SimpleDateFormat

@Composable
fun Messenger(gameRoom: GameRoom, gameViewModel: GameViewModel) {
    var message = remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkNavy
    ) {
        Column(
        ) {
            Card(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 15.dp, bottomStart = 15.dp))) {
                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth().height(80.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { gameViewModel.chatOpen.value = false },
                                modifier = Modifier) {
                                Icon(
                                    Icons.Rounded.ArrowBack,
                                    null
                                )
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gameRoom.roomNickname,
                                    style = MaterialTheme.typography.h6,
                                    textAlign = TextAlign.Center
                                )
                            }
                            IconButton(onClick = { /*TODO - Make a scoreboard*/ },
                                modifier = Modifier) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    null
                                )
                            }
                        }
                    }
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column() {

                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(OffWhite)
                            ) {
                                items(gameRoom.gameLog) {
                                    val sdf = SimpleDateFormat("HH:mm")
                                    val date = sdf.format(it.date)
                                    if (it.type == "userMessage") {
                                        val player =
                                            gameViewModel.listOfPlayers.value.filter { player ->
                                                it.uid == player.uid
                                            }.first()
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            if (player.uid == gameViewModel.currentPlayer.value.uid) {
                                                UserMessage(
                                                    message = it.message,
                                                    time = date,
                                                )
                                            } else {
                                                PlayerMessage(message = it.message,
                                                    avatar = player.avatar,
                                                    nickName = player.nickName,
                                                    time = date
                                                )
                                            }
                                        }

                                    } else if (it.type == "gameLog") {
                                        GameMessage(message = it.message, time = date)
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
                value = message.value,
                onValueChange = { newValue ->
                    message.value = newValue
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
                      "Type here...",
                      fontSize = 16.sp,
                      color = Steel
                  )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        GameServer.sendMessage(gameRoom = gameRoom,
                            LogMessage.createLogMessage(
                                message = message.value,
                                uid = gameViewModel.currentPlayer.value.uid,
                                type = "userMessage"
                            )
                        )
                        message.value = ""
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
                        GameServer.sendMessage(gameRoom = gameRoom,
                            LogMessage.createLogMessage(
                                message = message.value,
                                uid = gameViewModel.currentPlayer.value.uid,
                                type = "userMessage"
                            )
                        )
                        message.value = ""
                    }
                )
            )
        }


    }
    BackHandler() {
        gameViewModel.chatOpen.value = false
    }

}

@Preview
@Composable
fun MessengerPreview() {
    val gameRoom = GameRoom()
    val logMessage = LogMessage.createLogMessage("Hello", "", "userMessage")
//    gameRoom.gameLog.add(logMessage)
    val gameViewModel = GameViewModel()
    Messenger(gameRoom = gameRoom, gameViewModel = gameViewModel)
}