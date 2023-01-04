package com.example.loveletter.presentation.game.util

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.loveletter.TAG
import com.example.loveletter.domain.CardAvatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.game.GameViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuessCard(gameRoom: GameRoom, guessCard: MutableState<Boolean>, gameViewModel: GameViewModel) {

    var guessedCard by remember {
        mutableStateOf(0)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.75f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Blue, RoundedCornerShape(15.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Guess a card",
                style = MaterialTheme.typography.h4
            )
            Spacer(Modifier.height(24.dp))
            val cards = listOf(
                2,
                3,
                4,
                5,
                6,
                7,
                8
            )
            val state = rememberLazyListState()
            val context = LocalContext.current

            var selectedIndex by remember {
                mutableStateOf(-1)
            }
            BoxWithConstraints {
                LazyRow(
                    state = state
                ) {
                    if (!state.isScrollInProgress) {

                    }
                    itemsIndexed(cards) { index, item ->
                        println("first visible item: ${state.firstVisibleItemIndex}")
                        println("visible items info ${state.layoutInfo.visibleItemsInfo}")
                        println("total items count: ${state.layoutInfo.totalItemsCount}")
                        val scale by animateFloatAsState(targetValue = if (selectedIndex == index) 1.2f else 1f)


                        val color by animateColorAsState(targetValue = if (selectedIndex == index) Color.Red else Color.Gray)
                        Layout(
                            content = {
                                // Here's the content of each list item.
                                Box(
                                    Modifier
                                        .padding(8.dp)
                                        .background(Color.Gray)
                                ) {
                                    val card = CardAvatar.setCardAvatar(item)
                                    PlayingCard(modifier = Modifier
                                        .selectable(
                                            selected = selectedIndex == index,
                                            onClick = {
                                                selectedIndex = index
                                                Log.d(TAG, "selectedIndex: $selectedIndex")
                                                guessedCard = item
                                                Log.d(TAG,
                                                    "Player guessed: ${gameViewModel.guessCardAlert.value}")
                                            }
                                        )

                                        .scale(scale)
                                        .border(2.dp, color),
                                        cardAvatar = card)
                                }
                            },
                            measurePolicy = { measurables, constraints ->
                                val placeable = measurables.first().measure(constraints)
                                // maxWidth is from the BoxWithConstraints
                                val maxWidthInPx = maxWidth.roundToPx()
                                // Box width
                                val itemWidth = placeable.width
                                // Calculating the space for the first and last item
                                val startSpace =
                                    if (index == 0) (maxWidthInPx - itemWidth) / 2 else 0
                                val endSpace =
                                    if (index == cards.lastIndex) (maxWidthInPx - itemWidth) / 2 else 0
                                // The width of the box + extra space
                                val width = startSpace + placeable.width + endSpace
                                layout(width, placeable.height) {
                                    // Placing the Box in the right X position
                                    val x = if (index == 0) startSpace else 0
                                    placeable.place(x, 0)
                                }
                            }
                        )
                    }
                }
            }



            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    enabled = guessedCard != 0,
                    onClick = {
                        gameViewModel.onGuess(guessedCard, gameRoom = gameRoom)
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 16.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(15.dp))) {
                    Icon(
                        Icons.Rounded.Check,
                        "Confirm"
                    )
                }
            }
        }
    }
}
