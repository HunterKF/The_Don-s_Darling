package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.CardAvatar
import com.example.loveletter.presentation.game.GameViewModel

@Composable
fun RevealCard(gameViewModel: GameViewModel) {
    val card = CardAvatar.setCardAvatar(gameViewModel.selectedPlayer.value.hand.first())
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
                text = "Result",
                style = MaterialTheme.typography.h5
            )
            Divider()
            Spacer(modifier = Modifier.height(22.dp))
            Text(text = "You are looking at ${gameViewModel.selectedPlayer.value.nickName}'s card.", style = MaterialTheme.typography.h6, textAlign = TextAlign.Center)
            PlayingCard(cardAvatar = card)
            IconButton(onClick = { gameViewModel.revealCardAlert.value = false }) {
                Icon(
                    Icons.Rounded.Check,
                    null
                )
            }
        }
    }
}