package com.example.loveletter.presentation.createplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AvatarImage(
    icon: Int,
    modifier: Modifier = Modifier,
    background: Color,
) {
    Box(
        modifier = modifier
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(id = icon), null,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .border(4.dp, background, RoundedCornerShape(5.dp))
        )
    }
}