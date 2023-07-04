package com.example.thedonsdarling.presentation.game.util

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.*
import com.example.thedonsdarling.domain.models.Deck
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.ui.theme.*

@Composable
fun Table(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
            .border(15.dp, Steel, RoundedCornerShape(150.dp))
            .clip(RoundedCornerShape(350.dp))
            .background(Black)
    )

}

@Composable
fun PlayerIconLeft(
    player: Player,
    xDp: Dp = 20.dp,
    borderStroke: BorderStroke,
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .border(borderStroke, RoundedCornerShape(25.dp))
                .clip(RoundedCornerShape(25.dp))
                .widthIn(min = 120.dp, max = 170.dp)
                .height(40.dp)
                .background(Steel)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Spacer(Modifier.width(54.dp))

            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = player.nickName,
                    fontSize = 12.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Normal)
                Text(
                    text = stringResource(R.string.table_player_wins, player.wins),
                    fontSize = 12.sp,
                    color = Color.Yellow,
                    fontWeight = FontWeight.Light)
            }
            Spacer(Modifier.width(12.dp))

        }
        val avatar = Avatar.setAvatar(player.avatar)

        Box(
            modifier = Modifier.align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            if (!player.isAlive) {
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                        .border(borderStroke, CircleShape)
                        .align(Alignment.Center)
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray.copy(0.8f))
                ) {
                    Icon(
                        painterResource(id = R.drawable.dead),
                        null,
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(35.dp),
                    )
                }
            }
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = stringResource(id = avatar.description),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(45.dp)
                    .zIndex(1f)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .border(borderStroke, CircleShape)
                    .align(Alignment.Center)
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Black)
            )
        }
        Box(
            modifier = Modifier
                .offset(y = 50.dp, x = xDp)

                .align(Alignment.Center)
//                .border(1.dp, Color.Green)
        ) {
            Row() {
                player.hand.forEach { _ ->
                    Image(
                        modifier = Modifier
                            .border(1.dp, Color.White, RoundedCornerShape(10.dp))

                            .size(40.dp),
                        painter = painterResource(id = R.drawable._12_512_b),
                        contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun PlayerIconRight(
    player: Player,
    xDp: Dp = (-50).dp,
    borderStroke: BorderStroke,
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .border(borderStroke, RoundedCornerShape(25.dp))

                .clip(RoundedCornerShape(25.dp))
                .widthIn(min = 120.dp, max = 170.dp)
                .height(40.dp)
                .background(Steel)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = player.nickName,
                    fontSize = 12.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Normal)
                Text(
                    text = stringResource(R.string.table_player_wins, player.wins),
                    fontSize = 12.sp,
                    color = Color.Yellow,
                    fontWeight = FontWeight.Light)
            }
            Spacer(Modifier.width(54.dp))
        }
        val avatar = Avatar.setAvatar(player.avatar)

        Box(
            modifier = Modifier.align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            if (!player.isAlive) {
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                        .border(borderStroke, CircleShape)
                        .align(Alignment.Center)
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray.copy(0.8f))
                ) {
                    Icon(
                        painterResource(id = R.drawable.dead),
                        null,
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(35.dp),
                    )
                }
            }
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = stringResource(id = avatar.description),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(45.dp)
                    .zIndex(1f)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .border(borderStroke, CircleShape)
                    .align(Alignment.Center)
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Black)
            )
        }
        Box(
            modifier = Modifier
                .offset(y = 50.dp, x = xDp)

                .align(Alignment.Center)
//                .border(1.dp, Color.Green)
        ) {
            Row() {
                player.hand.forEach { _ ->
                    Image(
                        modifier = Modifier
                            .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                            .size(40.dp),
                        painter = painterResource(id = R.drawable._12_512_b),
                        contentDescription = null)
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
    }
}

@Composable
fun DeckPlace(deck: Deck, modifier: Modifier = Modifier, roundOver: Boolean) {
    Box(modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (!roundOver) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    deck.deck.forEach { _ ->
                        Image(
                            modifier = Modifier
                                .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                                .size(90.dp),
                            painter = painterResource(id = R.drawable._12_512_b),
                            contentDescription = null,
                        )
                    }
                    Text(
                        text = deck.deck.size.toString(),
                        color = MaterialTheme.colors.onPrimary.copy(0.6f),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier.size(90.dp),
                    contentAlignment = Alignment.Center

                ) {
                    if (deck.discardDeck.isNotEmpty()) {
                        deck.discardDeck.forEach {
                            PlayingCard(cardAvatar = CardAvatar.setCardAvatar(it),
                                modifier = Modifier.size(90.dp))

                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(
                        text = stringResource(R.string.wait_for_host),
                        color = MaterialTheme.colors.onPrimary.copy(0.8f),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }

    }
}

@Composable
fun PlayingTable(
    game: GameRoom, gameViewModel: GameViewModel, navController: NavController,
    modifier: Modifier = Modifier,
) {

    val players = gameViewModel.removeCurrentPlayer(game.players)


    Box(
        modifier = modifier
    ) {
        Table(
            modifier = Modifier.padding(30.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 60.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .offset(y = (-15).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {
                //player1

                if (players.size >= 1) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                    ) {
                        var stroke = remember {
                            mutableStateOf(BorderStroke(0.dp, Color.Transparent))
                        }
                        if (players[0].turn) {
                            Log.d(TAG,
                                "Player1: ${players[0].nickName}'s turn value: ${players[0].turn}")
                            stroke.value = BorderStroke(2.dp, Color.Red)
                        } else {
                            stroke.value = BorderStroke(0.dp, Color.Transparent)
                        }
                        PlayerIconLeft(player = players[0],
                            borderStroke = stroke.value)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                //player2

                if (players.size >= 2) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                    ) {
                        var stroke = remember {
                            mutableStateOf(BorderStroke(0.dp, Color.Transparent))
                        }
                        if (players[1].turn) {
                            Log.d(TAG,
                                "Player1: ${players[0].nickName}'s turn value: ${players[0].turn}")
                            stroke.value = BorderStroke(2.dp, Color.Red)
                        } else {
                            stroke.value = BorderStroke(0.dp, Color.Transparent)
                        }
                        PlayerIconRight(player = players[1],
                            borderStroke = stroke.value)
                    }
                }
            }
            DeckPlace(
                deck = game.deck,
                roundOver = game.roundOver,
                modifier = Modifier
                    .zIndex(1f)
                    .weight(0.8f))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .offset(y = (-20).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {
                //player3

                if (players.size >= 3) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                    ) {
                        var stroke = remember {
                            mutableStateOf(BorderStroke(0.dp, Color.Transparent))
                        }
                        if (players[2].turn) {
                            Log.d(TAG,
                                "Player1: ${players[2].nickName}'s turn value: ${players[0].turn}")
                            stroke.value = BorderStroke(2.dp, Color.Red)
                        } else {
                            stroke.value = BorderStroke(0.dp, Color.Transparent)
                        }
                        PlayerIconLeft(player = players[2],
                            borderStroke = stroke.value)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                //local player
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                ) {
                    var stroke = remember {
                        mutableStateOf(BorderStroke(0.dp, Color.Transparent))
                    }
                    if (gameViewModel.localPlayer.value.turn) {
                        Log.d(TAG,
                            "Player1: ${gameViewModel.localPlayer.value.nickName}'s turn value: ${gameViewModel.localPlayer.value.turn}")
                        stroke.value = BorderStroke(2.dp, Color.Red)
                    } else {
                        stroke.value = BorderStroke(0.dp, Color.Transparent)
                    }
                    PlayerIconRight(player = gameViewModel.localPlayer.value,
                        borderStroke = stroke.value)
                }
            }


        }


    }
}

