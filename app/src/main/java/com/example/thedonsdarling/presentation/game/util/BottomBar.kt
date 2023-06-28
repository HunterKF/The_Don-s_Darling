package com.example.thedonsdarling.presentation.game.util

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.thedonsdarling.R
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.domain.CardAvatar
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.ui.theme.*
import com.example.thedonsdarling.domain.util.game.gamerules.CardRules.Courtesan
import com.example.thedonsdarling.util.UiEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    player: Player,
    color: Color = MaterialTheme.colors.primary,
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
    val context = LocalContext.current
    var currentCard by remember {
        mutableStateOf(0)
    }
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    var cardTitle by remember { mutableStateOf("") }
    var cardDescription by remember { mutableStateOf("") }
    LaunchedEffect(key1 = game) {
        selectedIndex = -1
        currentCard = 0
        cardTitle = ""
        cardDescription = ""

    }

    val coroutineScope = rememberCoroutineScope()
    val borderStroke =
        if (player.turn) BorderStroke(2.dp, Color.Red) else BorderStroke(0.dp, Color.Transparent)

    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .background(color),
        contentAlignment = Alignment.Center
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
                        .border(1.dp, MaterialTheme.colors.onPrimary, RoundedCornerShape(10.dp))
                        .background(Black), onClick = { onOpen() }) {
                    Icon(
                        Icons.Rounded.Menu,
                        null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, MaterialTheme.colors.onPrimary, RoundedCornerShape(10.dp))
                            .background(Black)
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = { gameViewModel.chatOpen.value = true }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.comment),
                                null,
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }

                    }
                    if (player.unread) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(4.dp, Black, CircleShape)
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
                    .zIndex(1f)
            )
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

                    gameViewModel.onUiEvent(
                        UiEvent.OnPlay(
                            card = currentCard,
                            player = player,
                            gameRoom = game
                        )

                    )
                    cardTitle = ""
                    cardDescription = ""
                }) {
                Text(
                    text = if (player.turn) stringResource(id = R.string.play_button_play) else stringResource(id = R.string.play_button_wait)
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
                        tint = WarmRed,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(55.dp),
                    )
                }
            }
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = stringResource(id = avatar.description),
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
                    .background(color)
            )
        }
        val shakeKeyframes: AnimationSpec<Float> = keyframes {
            durationMillis = 800
            val easing = FastOutLinearInEasing
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

        if (cardTitle != "" && gameViewModel.showGuides.value) {
            Box(
                modifier = Modifier
                    .offset(y = (-200).dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colors.onPrimary)
            ) {
                Column(modifier = Modifier.padding(vertical = 6.dp,
                    horizontal = 8.dp),
                    horizontalAlignment = Alignment.Start) {
                    Text(
                        text = cardTitle,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = cardDescription,
                        color = MaterialTheme.colors.primary
                    )
                }

            }
        }
        Row(
            modifier = Modifier

                .fillMaxWidth(0.8f)
                .offset(y = (-100).dp),
            horizontalArrangement = Arrangement.Center
        ) {
            currentPlayer.hand.forEachIndexed() { index, cardNumber ->
                if (currentPlayer.hand.size == 2) {

                    var hasCountess by remember {
                        mutableStateOf(false)
                    }
                    var notPlayable = false
                    hasCountess = currentPlayer.hand.contains(7)


                    if (hasCountess) {
                        notPlayable = Courtesan.checkCard(cardNumber)
                    }
                    val scale by animateDpAsState(targetValue = if (selectedIndex == index) 60.dp else 50.dp)
                    val cardGuideX by animateDpAsState(targetValue = if (index == 0) (-0).dp else 0.dp)
                    val offset by animateDpAsState(targetValue = if (selectedIndex == index) (-30).dp else 0.dp)

                    var card = CardAvatar.setCardAvatar(cardNumber)

                    Box(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center) {
                        PlayingCard(
                            cardAvatar = card,
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
                                        if (selectedIndex == index && !notPlayable) {
                                            currentCard = cardNumber
                                            Log.d(TAG, "if $selectedIndex & $index & $currentCard")
                                            Log.d(TAG, "if $notPlayable")

                                        } else {
                                            currentCard = 0


                                        }
                                        if (selectedIndex == index && !notPlayable) {
                                            cardTitle = context.getString(card.cardName)
                                            cardDescription =
                                                context.getString(card.ruleShortDescription)

                                        } else {
                                            cardTitle = ""
                                            cardDescription = ""
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
                                                    context.getString(R.string.countess_in_hand_message),
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
                        val card = CardAvatar.setCardAvatar(cardNumber)
                        PlayingCard(
                            modifier = Modifier
                                .scale(scale)
                                .clickable {
                                    selected = !selected
                                    if (selected) {
                                        cardTitle = context.getString(card.cardName)
                                        cardDescription =
                                            context.getString(card.ruleShortDescription)

                                    } else {
                                        cardTitle = ""
                                        cardDescription = ""
                                    }
                                },
                            cardAvatar = card
                        )
                    }
                }
            }
        }
    }
}

