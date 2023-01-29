package com.example.thedonsdarling.presentation.settings.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.thedonsdarling.R
import com.example.thedonsdarling.presentation.util.OutlinedButton
import com.example.thedonsdarling.ui.theme.Steel

@Composable
fun EndGameAlert(onCancel: () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier
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
                text = stringResource(R.string.end_game_question),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h5
            )
            Divider()
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.end_game_warning),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                    onClick()
                }
            }
        }
    }
}