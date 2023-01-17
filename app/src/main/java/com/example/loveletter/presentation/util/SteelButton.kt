package com.example.loveletter.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.loveletter.ui.theme.Black
import com.example.loveletter.ui.theme.Steel

@Composable
fun OutlinedButton(
    icon: ImageVector? = null,
    drawable: Int? = null,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colors.onPrimary, RoundedCornerShape(10.dp))
            .background(Black),
        onClick = { onClick() }) {
        if (icon != null) {
            Icon(
                icon,
                null,
                tint = MaterialTheme.colors.onPrimary
            )
        } else if (drawable != null) {
            Icon(
                painter = painterResource(id = drawable),
                null,
                tint = MaterialTheme.colors.onPrimary
            )
        }

    }
}