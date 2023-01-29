package com.example.thedonsdarling.presentation.settings.util

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.domain.GameRoom
import com.example.thedonsdarling.domain.Player
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.util.OutlinedButton
import com.example.thedonsdarling.ui.theme.Steel

@Composable
fun ReportPlayer(
    player: Player,
    onCancel: () -> Unit
    ) {
    val context = LocalContext.current

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_EMAIL, arrayOf("hunter.krez@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.report_player_email_title, player.uid))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.report_player_email_message, player.nickName, player.uid))
        type = "vnd.android.cursor.item/email"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.75f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(15.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.report_player_title),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h5
            )
            Divider()
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.report_player_message),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
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
                    contentDescription = stringResource(id = avatar.description),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = player.nickName,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary,
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
                    icon = Icons.Rounded.Close,
                    colorBackground = MaterialTheme.colors.onPrimary,
                    colorContent = MaterialTheme.colors.primary
                ) {
                    onCancel()
                }
                OutlinedButton(
                    icon = Icons.Rounded.CheckCircle,
                    colorBackground = MaterialTheme.colors.onPrimary,
                    colorContent = MaterialTheme.colors.primary
                ) {
                    context.startActivity(shareIntent)
                }
            }
        }
    }
}