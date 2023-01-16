package com.example.loveletter.presentation.settings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.loveletter.R
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.game.util.WindowCenterOffsetPositionProvider
import com.example.loveletter.presentation.settings.util.EndGameAlert
import com.example.loveletter.presentation.settings.util.ReportPlayer
import com.example.loveletter.presentation.util.SteelButton
import com.example.loveletter.ui.theme.*
import com.example.loveletter.util.game.gamerules.gameserver.ConnectionRules
import com.example.loveletter.util.game.gamerules.gameserver.GameServer
import com.example.loveletter.util.user.HandleUser

@Composable
fun Settings(game: GameRoom, gameViewModel: GameViewModel, onExit: () -> Unit) {
    var endGameAlert by remember {
        mutableStateOf(false)
    }
    var reportPlayerAlert by remember {
        mutableStateOf(false)
    }
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    var reportPlayer = remember {
        mutableStateOf(Player())
    }
    LaunchedEffect(key1 = game) {
        if (game.deleteRoom) {
            onExit()
        }
    }

    Scaffold(
        backgroundColor = DarkNavy
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .border(5.dp, Navy, RectangleShape)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = CenterVertically
            ) {
                IconButton(onClick = { gameViewModel.settingsOpen.value = false }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        null,
                        tint = Steel
                    )
                }

                Text(
                    "Settings",
                    style = MaterialTheme.typography.h5
                )
                Spacer(Modifier.weight(1f))
            }
            if (gameViewModel.isHost.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = CenterVertically

                ) {
                    Text(
                        "Restart",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                    SteelButton(icon = Icons.Rounded.Refresh) {
                        GameServer.startNewGame(gameRoom = game)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        "End Game",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                    SteelButton(drawable = R.drawable.exit) {
                        endGameAlert = true
                    }
                }


            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    "Report Player",
                    style = MaterialTheme.typography.h6,
                )
                game.players.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(8.dp)

                            .clickable {
                                selectedIndex = if (selectedIndex == -1) {
                                    it.turnOrder
                                } else {
                                    -1
                                }
                                if (reportPlayer.value != it) {
                                    reportPlayer.value = it
                                } else {
                                    reportPlayer.value = Player()
                                }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        val avatar = Avatar.setAvatar(it.avatar)
                        Image(
                            painter = painterResource(id = avatar.avatar),
                            contentDescription = avatar.description,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = it.nickName,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp)
                                .weight(1f)
                        )
                        val animateOffset by animateDpAsState(
                            targetValue = if (selectedIndex == it.turnOrder) 0.dp else 10.dp,
                            animationSpec = tween(
                                delayMillis = 0
                            )
                        )
                        if (selectedIndex == it.turnOrder) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .offset(x = animateOffset)

                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(WarmRed)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp),
                                        verticalAlignment = CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Text(
                                            text = "Report?",
                                            color = Color.White
                                        )
                                        IconButton(onClick = { reportPlayerAlert = true }) {
                                            Icon(
                                                Icons.Rounded.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }

                }

            }

            if (!gameViewModel.isHost.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        "Leave Game",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                    SteelButton(drawable = R.drawable.exit) {
                        ConnectionRules.leaveGame(game.roomCode, gameViewModel.localPlayer.value)
                        onExit()
                    }
                }
            }
        }


        if (endGameAlert) {
            Popup(popupPositionProvider = WindowCenterOffsetPositionProvider(),
                onDismissRequest = { endGameAlert = false }) {
                EndGameAlert(
                    onCancel = { endGameAlert = false },
                    onClick = {
                        HandleUser.deleteUserGameRoom(
                            roomCode = game.roomCode,
                            roomNickname = game.roomNickname,
                            players = game.players
                        )
                        GameServer.deleteRoom(game)
                    }
                )
            }
        }
        if (reportPlayerAlert) {
            Popup(popupPositionProvider = WindowCenterOffsetPositionProvider(),
                onDismissRequest = { reportPlayerAlert = false }) {
                ReportPlayer(gameViewModel = gameViewModel,
                    gameRoom = game,
                    player = reportPlayer.value) {
                    reportPlayerAlert = false
                }
            }
        }
    }
    BackHandler() {
        gameViewModel.settingsOpen.value = false
    }
}


