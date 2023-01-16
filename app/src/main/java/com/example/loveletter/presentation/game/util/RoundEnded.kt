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
import com.example.loveletter.presentation.util.Scoreboard
import com.example.loveletter.util.game.gamerules.gameserver.GameServer

@Composable
fun RoundEndedAlert(gameViewModel: GameViewModel, gameRoom: GameRoom) {
    val winnerMessage = gameRoom.gameLog.last { it.type == "winnerMessage" }
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.75f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Blue, RoundedCornerShape(15.dp))
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

            Scoreboard(gameRoom = gameRoom)
            if (gameViewModel.isHost.value) {
                Button(
                    onClick = {
                        GameServer.startNewGame(gameRoom)
                        gameViewModel.endRoundAlert.value = false
                        gameViewModel.resultAlert.value = false
                    }) {
                    Text(
                        text = "Start Next Round"
                    )
                }
            } else {
                Button(onClick = { gameViewModel.endRoundAlert.value = false }) {
                    Text(
                        text = "Okay"
                    )
                }
            }

        }
    }
}