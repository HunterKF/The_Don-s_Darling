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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.ui.theme.Black

@Composable
fun RoomPlayerList(
    player: Player,
    index: Int,
    gameRoom: GameRoom,
) {
    val avatar = Avatar.setAvatar(player.avatar)
    Row(Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {

        Image(modifier = Modifier
            .size(70.dp)
            .padding(4.dp)
            .clip(CircleShape),
            painter = painterResource(id = avatar.avatar),
            contentDescription = stringResource(id = avatar.description))
        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = player.nickName,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f),
            color = Black
        )
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (index != gameRoom.players.size) {
            ShadowDivider(
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}