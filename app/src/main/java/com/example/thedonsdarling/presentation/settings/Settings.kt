package com.example.thedonsdarling.presentation.settings

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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.game.util.WindowCenterOffsetPositionProvider
import com.example.thedonsdarling.presentation.settings.util.EndGameAlert
import com.example.thedonsdarling.presentation.settings.util.ReportPlayer
import com.example.thedonsdarling.presentation.util.OutlinedButton
import com.example.thedonsdarling.ui.theme.*
import com.example.thedonsdarling.util.game.GameServer
import com.example.thedonsdarling.data.gameserver.ConnectionRules
import com.example.thedonsdarling.domain.util.user.HandleUser
import com.example.thedonsdarling.util.UiEvent

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
    val reportPlayer = remember {
        mutableStateOf(Player())
    }
    LaunchedEffect(key1 = game) {
        if (game.deleteRoom) {
            onExit()
        }
    }

    Scaffold(
        backgroundColor = Black
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .border(5.dp, Navy, RectangleShape)
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Center
            ) {
                IconButton(
                    modifier = Modifier.align(CenterStart),
                    onClick = { gameViewModel.settingsOpen.value = false }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        null,
                    )
                }

                Text(
                    stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Center)
                )
            }
            Spacer(modifier = Modifier.height(26.dp))

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
                    OutlinedButton(icon = Icons.Rounded.Refresh) {
                        GameServer.startNewRound(gameRoom = game)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
                    OutlinedButton(drawable = R.drawable.icon_end_game) {
                        endGameAlert = true
                    }
                }


            }
            Spacer(modifier = Modifier.height(26.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    stringResource(id = R.string.report_player_title),
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
                            contentDescription = stringResource(id = avatar.description),
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
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Card Guide",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )
                val checkedState =
                    remember { mutableStateOf(gameViewModel.localPlayer.value.guide) }
                Switch(
                    checked = checkedState.value,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = WarmRed,
                        checkedTrackColor = WarmRed.copy(alpha = 0.5f),
                        uncheckedThumbColor = MaterialTheme.colors.onPrimary,
                        uncheckedTrackColor = MaterialTheme.colors.onPrimary.copy(0.5f)
                    ),
                    onCheckedChange = {
                        checkedState.value = it
                        HandleUser.toggleCardGuideSetting(
                            player = gameViewModel.localPlayer.value,
                            gameRoom = game
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

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
                    OutlinedButton(drawable = R.drawable.exit) {
                        gameViewModel.onUiEvent(UiEvent.ExitGame)

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
                        HandleUser.deleteUserGameRoomForAll(
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
                ReportPlayer(
                    player = reportPlayer.value) {
                    reportPlayerAlert = false
                }
            }
        }
    }
    BackHandler {
        gameViewModel.settingsOpen.value = false
    }
}



