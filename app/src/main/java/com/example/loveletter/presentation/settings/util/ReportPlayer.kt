package com.example.loveletter.presentation.settings.util

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.util.OutlinedButton
import com.example.loveletter.ui.theme.Steel

@Composable
fun ReportPlayer(
    gameRoom: GameRoom,
    gameViewModel: GameViewModel,
    player: Player,
    onCancel: () -> Unit
    ) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_EMAIL, arrayOf("hunter.krez@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "Reporting user: ${player.uid}")
        putExtra(Intent.EXTRA_TEXT, "User ${player.nickName} (${player.uid}) is being reported. (Please write down what happened.)")
        type = "vnd.android.cursor.item/email"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.75f)
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
                text = "Report player",
                style = MaterialTheme.typography.h5
            )
            Divider()
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "Please send an email to report a player. Afterwards, please exit the game through the settings.",
                style = MaterialTheme.typography.body1
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val avatar = Avatar.setAvatar(player.avatar)
                Image(
                    painter = painterResource(id = avatar.avatar),
                    contentDescription = avatar.description,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = player.nickName,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    icon = Icons.Rounded.Close
                ) {
                    onCancel()
                }
                OutlinedButton(
                    icon = Icons.Rounded.CheckCircle
                ) {
                    context.startActivity(shareIntent)
                }
            }
        }
    }
}