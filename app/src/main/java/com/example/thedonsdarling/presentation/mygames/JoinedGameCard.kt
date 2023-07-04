package com.example.thedonsdarling.presentation.mygames

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.thedonsdarling.domain.models.JoinedGame
import com.example.thedonsdarling.ui.theme.Navy


@Composable
fun JoinedGameCard(game: JoinedGame, color: Color = Navy, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = game.roomNickname,
                color = MaterialTheme.colors.primary
            )
        }
    }
}