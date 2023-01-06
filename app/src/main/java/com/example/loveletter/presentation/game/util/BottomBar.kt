package com.example.loveletter.presentation.game.util

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.loveletter.R
import com.example.loveletter.TAG
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.CardAvatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.ui.theme.*
import com.example.loveletter.util.game.gamerules.CardRules.Countess
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    player: Player,
    hand: List<Int>,
    color: Color = Navy,
    game: GameRoom,
    gameViewModel: GameViewModel,
    onOpen: () -> Job,
) {

    var currentPlayer = Player()
    game.players.forEach {
        if (it.uid == gameViewModel.localPlayer.value.uid) {
            currentPlayer = it
        }
    }
    Log.d(TAG, "Refreshing bottom bar composable")
    val context = LocalContext.current
    var currentCard by remember {
        mutableStateOf(0)
    }
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    LaunchedEffect(key1 = game) {
        selectedIndex = -1
        currentCard = 0
    }
    val coroutineScope = rememberCoroutineScope()
    val borderStroke =
        if (player.turn) BorderStroke(1.dp, Color.Red) else BorderStroke(0.dp, Color.Transparent)

    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .background(color)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, Steel, RoundedCornerShape(10.dp))
                        .background(DarkNavy), onClick = { onOpen() }) {
                    Icon(
                        Icons.Rounded.Menu,
                        null,
                        tint = Steel
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Steel, RoundedCornerShape(10.dp))
                            .background(DarkNavy)
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = { gameViewModel.chatOpen.value = true }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.comment),
                                null,
                                tint = Steel
                            )
                        }

                    }
                    if (player.unread) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(4.dp, DarkNavy, CircleShape)
                                .zIndex(3f)
                                .background(WarmRed)
                                .padding(2.dp)
                                .size(18.dp)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(25.dp)
                    .height(50.dp)
                    .zIndex(1f),
                contentAlignment = Alignment.BottomCenter
            ) {

            }
            Button(
                modifier = Modifier.padding(horizontal = 12.dp),
                enabled = player.turn && currentCard != 0,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = Color.Gray,
                    disabledContentColor = OffWhite,
                    backgroundColor = PineGreen,
                    contentColor = SoftYellow
                ),
                onClick = {
                    gameViewModel.onPlay(
                        card = currentCard,
                        player = player,
                        gameRoom = game
                    )
                }) {
                Text(
                    text = if (player.turn) "Play" else "Wait"
                )

            }

        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f)
                .offset(y = (-30).dp)
        ) {
            val avatar = Avatar.setAvatar(player.avatar)
            if (!player.isAlive) {
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                        .border(borderStroke, CircleShape)
                        .align(Alignment.Center)
                        .size(65.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray.copy(0.8f))
                ) {
                    Icon(
                        painterResource(id = R.drawable.dead),
                        null,
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(55.dp),
                    )
                }
            }
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = avatar.description,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(65.dp)
                    .zIndex(1f)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .border(borderStroke, CircleShape)
                    .align(Alignment.Center)
                    .size(85.dp)
                    .clip(CircleShape)
                    .background(Navy)
            )
        }
        val shakeKeyframes: AnimationSpec<Float> = keyframes {
            durationMillis = 800
            val easing = FastOutLinearInEasing

            // generate 8 keyframes
            for (i in 1..8) {
                val x = when (i % 3) {
                    0 -> 4f
                    1 -> -4f
                    else -> 0f
                }
                x at durationMillis / 10 * i with easing
            }
        }
        val offsetX = remember { Animatable(0f) }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.Center)
                .offset(y = (-100).dp),
            horizontalArrangement = Arrangement.Center
        ) {
            itemsIndexed(currentPlayer.hand) { index, cardNumber ->
                if (currentPlayer.hand.size == 2) {

                    var hasCountess by remember {
                        mutableStateOf(false)
                    }
                    var notPlayable = false
                    hasCountess = currentPlayer.hand.contains(7)
                    Log.d(TAG, "(before) hasCountess = $hasCountess")

                    LaunchedEffect(key1 = game, block = {
                        Log.d(TAG, "LaunchedEffect is starting. ")
                        Log.d(TAG, "(before) notPlay = $notPlayable")
                        notPlayable = Countess.checkCard(cardNumber)
                        Log.d(TAG, "(after) notPlay = $notPlayable")

                    })

                    if (hasCountess) {
                        notPlayable = Countess.checkCard(cardNumber)
                    }
                    val scale by animateDpAsState(targetValue = if (selectedIndex == index) 60.dp else 50.dp)
                    val offset by animateDpAsState(targetValue = if (selectedIndex == index) (-30).dp else 0.dp)

                    Box(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center) {
                        PlayingCard(cardAvatar = CardAvatar.setCardAvatar(
                            cardNumber),
                            notPlayable = notPlayable,
                            modifier = Modifier
                                .size(if (notPlayable) 50.dp else scale)
                                .offset(
                                    x = if (notPlayable) offsetX.value.dp else 0.dp,
                                    y = if (!notPlayable) offset else 0.dp
                                )
                                .selectable(
                                    selected = selectedIndex == index,
                                    onClick = {
                                        selectedIndex =
                                            if (selectedIndex == index) {
                                                -1
                                            } else {
                                                index
                                            }
                                        currentCard = if (selectedIndex == index && !notPlayable) {
                                            cardNumber
                                        } else {
                                            0
                                        }
                                        if (notPlayable) {
                                            coroutineScope.launch {
                                                offsetX.animateTo(
                                                    targetValue = 0f,
                                                    animationSpec = shakeKeyframes,
                                                )
                                            }
                                        }
                                        if ((cardNumber == 5 || cardNumber == 6) && hasCountess) {
                                            Toast
                                                .makeText(context,
                                                    "Please play the Countess.",
                                                    Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                    }
                                )
                        )
                    }

                } else {
                    var selected by remember {
                        mutableStateOf(false)
                    }
                    val scale by animateFloatAsState(targetValue = if (selected) 0.8f else 0.6f)
                    Box(contentAlignment = Alignment.Center) {
                        PlayingCard(
                            modifier = Modifier
                                .scale(scale)
                                .clickable {
                                    selected = !selected
                                },
                            cardAvatar = CardAvatar.setCardAvatar(
                                cardNumber)
                        )
                    }
                }
            }
        }
    }
}

