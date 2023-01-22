package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.util.CustomTextButton
import com.example.loveletter.presentation.util.Scoreboard
import com.example.loveletter.util.game.GameServer

@Composable
fun RoundEndedAlert(gameViewModel: GameViewModel, gameRoom: GameRoom) {
    val winnerMessage = gameRoom.gameLog.last { it.type == "winnerMessage" }
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

            Scoreboard(gameRoom = gameRoom, color = MaterialTheme.colors.primary)
            if (gameViewModel.isHost.value) {
                CustomTextButton(
                    enabled = true,
                    onClick = {
                        GameServer.startNewGame(gameRoom)
                        gameViewModel.endRoundAlert.value = false
                        gameViewModel.resultAlert.value = false
                    },
                    text = "Start Next Round",
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            } else {
                CustomTextButton(
                    enabled = true,
                    onClick = { gameViewModel.endRoundAlert.value = false },
                    text = "Okay",
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            }

        }
    }
}