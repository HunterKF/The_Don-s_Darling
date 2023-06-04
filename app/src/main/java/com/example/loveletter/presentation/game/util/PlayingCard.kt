package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.loveletter.R
import com.example.loveletter.domain.CardAvatar

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
        .size(50.dp)
        .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .padding(6.dp)
//                .border(2.dp, Color.Red, CircleShape)
                .align(Alignment.TopStart)
//                .clip(CircleShape)
//                .size(30.dp)
//                .background(Color.White)
                .zIndex(1f)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center).offset(y= 1.dp, x = 1.dp),
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
            contentDescription = cardAvatar.cardName,
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
        /*Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.BottomCenter)
                .background(Color.Blue)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = cardAvatar.cardName,
                textAlign = TextAlign.Center
            )
        }*/

        if (infoWindow) {
            AlertDialog(
                onDismissRequest = {
                    infoWindow = false
                },
                title = {
                    Text(
                        text = "${cardAvatar.cardName}'s Rule"
                    )
                },
                text = {
                    Text(
                        text = stringResource(cardAvatar.ruleDescription)
                    )
                },
                buttons = {
                    IconButton(onClick = { infoWindow = false }) {
                        Icon(
                            Icons.Rounded.Close,
                            null
                        )
                    }
                }
            )
        }
        /*IconButton(onClick = { infoWindow = !infoWindow },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 10.dp, y = (10).dp)
        ) {
            Icon(
                Icons.Rounded.Info,
                "Check ${cardAvatar.cardName}'s rule.",
                tint = Color.White,
                modifier = Modifier
                    .scale(0.5f)
                    .align(Alignment.BottomEnd)
            )
        }*/
    }
}

@Preview
@Composable
fun Preview() {
    val card = CardAvatar.setCardAvatar(1)
//    PlayingCard(card, Modifier)
}