package com.example.thedonsdarling.presentation.game.util

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.thedonsdarling.R
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.CardAvatar
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.util.CustomTextButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuessCard(gameRoom: GameRoom, gameViewModel: GameViewModel) {

    var guessedCard by remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.75f)
            .padding(16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(4.dp, MaterialTheme.colors.primary, RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.guess_a_card),
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
            var selectedIndex by remember {
                mutableStateOf(-1)
            }


            val itemSize: Dp =
                ((LocalConfiguration.current.screenWidthDp.dp - 58.dp) / 4)
            LazyVerticalGrid(
                modifier = Modifier.padding(vertical = 16.dp).fillMaxHeight(0.8f),
                cells = GridCells.Adaptive(itemSize),
                horizontalArrangement = Arrangement.Center,
                content = {
                    itemsIndexed(cards) { index, item ->
                        val scale by animateFloatAsState(targetValue = if (selectedIndex == index) 1.2f else 1f)


                        val color by animateColorAsState(targetValue = if (selectedIndex == index) Color.Red else Color.Gray)
                        val card = CardAvatar.setCardAvatar(item)
                        PlayingCard(
                            modifier = Modifier
                                .selectable(
                                    selected = selectedIndex == index,
                                    onClick = {
                                        if (selectedIndex != index) {
                                            selectedIndex = index
                                            Log.d(TAG, "selectedIndex: $selectedIndex")
                                            guessedCard = item
                                            Log.d(TAG,
                                                "Player guessed: ${gameViewModel.guessCardAlert.value}")

                                        } else {
                                            selectedIndex = -1
                                            Log.d(TAG, "selectedIndex: $selectedIndex")
                                            guessedCard = -1
                                            Log.d(TAG,
                                                "Player guessed: ${gameViewModel.guessCardAlert.value}")

                                        }
                                    }
                                )
                                .scale(scale)
                                .aspectRatio(1f),
                            cardAvatar = card,
                            color = color
                        )
                    }
                })



            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                CustomTextButton(
                    enabled = guessedCard != 0 && guessedCard != -1,
                    onClick = {
                        gameViewModel.onGuess(guessedCard, gameRoom = gameRoom)
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 16.dp),

                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    text = stringResource(R.string.guess)
                )
            }
        }
    }
}
