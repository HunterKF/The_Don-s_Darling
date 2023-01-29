package com.example.thedonsdarling.presentation.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShadowDivider(modifier: Modifier = Modifier) {
    Divider(modifier = modifier
        .shadow(6.dp, RoundedCornerShape(1.dp)),
        color = Color.Transparent
    )
}