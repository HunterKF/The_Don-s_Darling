package com.example.thedonsdarling.presentation.game.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.thedonsdarling.domain.CardAvatar

@Composable
fun PlayingCard(
    modifier: Modifier = Modifier,
    cardAvatar: CardAvatar,
    color: Color = Color.White,
    notPlayable: Boolean = false,
) {
    var infoWindow by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier
        .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .padding(6.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 1.dp, x = 1.dp),
                text = cardAvatar.number.toString(),
                style = MaterialTheme.typography.h1,
                color = Color.Red,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = cardAvatar.number.toString(),
                style = MaterialTheme.typography.h1,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Image(
            modifier = Modifier.size(90.dp),
            painter = painterResource(id = cardAvatar.avatar),
            contentDescription = stringResource(id = cardAvatar.cardName),
            contentScale = ContentScale.FillBounds,

        )
        if (notPlayable) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(0.4f))
                    .zIndex(1f)
            )
        }
    }
}