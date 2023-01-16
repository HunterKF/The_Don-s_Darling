package com.example.loveletter.presentation.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom

@Composable
fun Scoreboard(gameRoom: GameRoom) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scoreboard"
        )
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
                        .padding(start = 8.dp)
                        .weight(1f)
                )
                Text(
                    text = it.wins.toString(),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(18.dp))

        }
    }
}