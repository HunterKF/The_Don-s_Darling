package com.example.loveletter.presentation.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Steel

@Composable
fun SteelButton(
    icon: ImageVector? = null,
    drawable: Int? = null,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Steel, RoundedCornerShape(10.dp))
            .background(DarkNavy),
        onClick = { onClick() }) {
        if (icon != null) {
            Icon(
                icon,
                null,
                tint = Steel
            )
        } else if (drawable != null) {
            Icon(
                painter = painterResource(id = drawable),
                null,
                tint = Steel
            )
        }

    }
}