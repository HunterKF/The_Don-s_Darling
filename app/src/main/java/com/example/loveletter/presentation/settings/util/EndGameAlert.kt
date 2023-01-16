package com.example.loveletter.presentation.settings.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.game.util.PlayingCard
import com.example.loveletter.presentation.util.SteelButton
import com.example.loveletter.ui.theme.Steel

@Composable
fun EndGameAlert(onCancel: () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Steel, RoundedCornerShape(15.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "End game?",
                style = MaterialTheme.typography.h5
            )
            Divider()
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "You cannot undo this.",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SteelButton(
                    icon = Icons.Rounded.Close
                ) {
                    onCancel()
                }
                SteelButton(
                    icon = Icons.Rounded.CheckCircle
                ) {
                    onClick()
                }
            }
        }
    }
}