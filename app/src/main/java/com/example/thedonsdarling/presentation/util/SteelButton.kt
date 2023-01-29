package com.example.thedonsdarling.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedButton(
    icon: ImageVector? = null,
    drawable: Int? = null,
    colorBackground: Color = MaterialTheme.colors.primary,
    colorContent: Color = MaterialTheme.colors.onPrimary,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .border(1.dp, colorContent, RoundedCornerShape(10.dp))
            .background(colorBackground),
        onClick = { onClick() }) {
        if (icon != null) {
            Icon(
                icon,
                null,
                tint = colorContent
            )
        } else if (drawable != null) {
            Icon(
                painter = painterResource(id = drawable),
                null,
                tint = colorContent
            )
        }

    }
}