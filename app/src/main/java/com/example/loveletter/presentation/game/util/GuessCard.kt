package com.example.loveletter.presentation.game.util

import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.loveletter.TAG
import com.example.loveletter.domain.CardAvatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.presentation.game.GameViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuessCard(gameRoom: GameRoom, guessCard: MutableState<Boolean>, gameViewModel: GameViewModel) {

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
            /*LazyRow(state = state) {

                itemsIndexed(cards) { index, number ->

                    val scale by animateFloatAsState(targetValue = if (index == state.firstVisibleItemIndex) 1f else 0.5f)
                    val offset by animateOffsetAsState(targetValue = when {
                        (state.firstVisibleItemIndex > index) -> Offset(20f, 20f)
                        (state.firstVisibleItemIndex < index) -> Offset(-50f, 0f)
                        (state.firstVisibleItemIndex == index) -> Offset(0f, 0f)
                        else -> {
                            Offset(0f, 0f)
                        }
                    })
                    val zIndex by animateFloatAsState(targetValue = if (index == state.firstVisibleItemIndex) 1f else 0f)
                    println("first Visible Item Index: ${state.firstVisibleItemIndex}")
                    println("first Visible Item Scroll Offset: ${state.firstVisibleItemScrollOffset}")
                    println("layout info: ${state.layoutInfo}")
                    val card = CardAvatar.setCardAvatar(number)
                    PlayingCard(cardAvatar = card, modifier = Modifier
                        .clickable(
                            onClick = { Toast
                                .makeText(context,
                                    "x = ${offset.x.dp}. y = ${offset.y.dp}.",
                                    Toast.LENGTH_SHORT)
                                .show()
                            }
                        )
                        .offset(offset.x.dp, offset.y.dp)
                        .scale(scale)
                        .zIndex(zIndex)
                    )
                }
            }*/

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
                                    PlayingCard(cardAvatar = card, modifier = Modifier
                                        .selectable(
                                            selected = selectedIndex == index,
                                            onClick = {
                                                selectedIndex = index
                                                Log.d(TAG, "selectedIndex: $selectedIndex")
                                                gameViewModel.guessCard.value = index
                                                Log.d(TAG,
                                                    "Player guessed: ${gameViewModel.guessCard.value}")
                                            }
                                        )

                                        .scale(scale)
                                        .border(2.dp, color))
                                }
                            },
                            measurePolicy = { measurables, constraints ->
                                // I'm assuming you'll declaring just one root
                                // composable in the content function above
                                // so it's measuring just the Box
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
                IconButton(onClick = { guessCard.value = false },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 16.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(15.dp))) {
                    Icon(
                        Icons.Rounded.Close,
                        "Cancel"
                    )
                }
                IconButton(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 16.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(15.dp))) {
                    Icon(
                        Icons.Rounded.Check,
                        "Cancel"
                    )
                }
            }
        }
    }
}
