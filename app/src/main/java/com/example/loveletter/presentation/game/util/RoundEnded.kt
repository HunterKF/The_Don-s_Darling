package com.example.loveletter.presentation.game.util

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.loveletter.R
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.util.CustomTextButton
import com.example.loveletter.presentation.util.Scoreboard
import com.example.loveletter.util.game.GameServer

@Composable
fun RoundEndedAlert(gameViewModel: GameViewModel, gameRoom: GameRoom) {
    val winnerMessage = gameRoom.gameLog.last { it.type == "winnerMessage" }
    var remainingCards = arrayListOf<Player>()
    gameRoom.players.forEach {
        if (it.isAlive) {
            remainingCards.add(it)
        }
    }
    var showRemainingPlayers by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(4.dp, MaterialTheme.colors.primary, RoundedCornerShape(15.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Round Over!",
                style = MaterialTheme.typography.h5
            )
            Divider()
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = winnerMessage.message,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.animateContentSize(),
                        text = stringResource(if (showRemainingPlayers)R.string.view_remaining_cards else R.string.view_scoreboard)
                    )
                    IconButton(onClick = { showRemainingPlayers = !showRemainingPlayers }) {
                        Icon(
                            Icons.Rounded.Refresh,
                            null,
                        )
                    }

                }
                if (showRemainingPlayers) {
                    RemainingPlayers(remainingPlayers = remainingCards,
                        color = MaterialTheme.colors.primary)
                } else {
                    Scoreboard(gameRoom = gameRoom, color = MaterialTheme.colors.primary)

                }
            }
            if (gameViewModel.isHost.value) {
                Column {
                    if (!gameRoom.gameOver) {
                        CustomTextButton(
                            enabled = true,
                            onClick = {
                                GameServer.startNewRound(gameRoom)
                                gameViewModel.endRoundAlert.value = false
                                gameViewModel.resultAlert.value = false
                            },
                            text = stringResource(R.string.start_next_round),
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        CustomTextButton(
                            enabled = true,
                            onClick = {
                                GameServer.startNewGame(gameRoom)
                                gameViewModel.endRoundAlert.value = false
                                gameViewModel.resultAlert.value = false
                            },
                            text = stringResource(R.string.start_new_game),
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    }
                    if (gameRoom.gameOver) {
                        CustomTextButton(
                            enabled = true,
                            onClick = {
                                GameServer.deleteRoom(gameRoom)
                                gameViewModel.endRoundAlert.value = false
                                gameViewModel.resultAlert.value = false
                            },
                            text = stringResource(R.string.end_and_delete_game),
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    }
                }

            } else {
                CustomTextButton(
                    enabled = true,
                    onClick = { gameViewModel.endRoundAlert.value = false },
                    text = stringResource(R.string.okay),
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            }

        }
    }
}