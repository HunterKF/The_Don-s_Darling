package com.example.thedonsdarling.presentation.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.domain.models.GameRoom

@Composable
fun Scoreboard(
    gameRoom: GameRoom,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        gameRoom.players.sortedByDescending { it.wins }.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    color = color,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
                Text(
                    text = it.wins.toString(),
                    style = MaterialTheme.typography.h6,
                    color = color,

                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(18.dp))

        }
    }
}