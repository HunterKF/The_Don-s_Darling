package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.loveletter.R
import com.example.loveletter.domain.*
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.ui.theme.*

@Composable
fun Table(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
            .border(15.dp, Steel, RoundedCornerShape(150.dp))
            .clip(RoundedCornerShape(350.dp))
            .background(DarkNavy)
    )

}

@Composable
fun PlayerIconLeft(
    player: Player,
    xDp: Dp = 20.dp,
    borderStroke: BorderStroke = BorderStroke(1.dp, Color.Red)
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
                Text(text = "Wins: ${player.wins}",
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
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = avatar.description,
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
                    .background(DarkNavy)
            )
        }
        Box(
            modifier = Modifier
                .offset(y = 50.dp, x = xDp)

                .align(Alignment.Center)
                .border(1.dp, Color.Green)
        ) {
            Row() {
                player.hand.forEach { _ ->
                    Image(
                        modifier = Modifier
                            .size(40.dp),
                        painter = painterResource(id = R.drawable.card_back),
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
    borderStroke: BorderStroke = BorderStroke(1.dp, Color.Red)
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
                Text(text = "Wins: ${player.wins}",
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
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = avatar.description,
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
                    .background(DarkNavy)
            )
        }
        Box(
            modifier = Modifier
                .offset(y = 50.dp, x = xDp)

                .align(Alignment.Center)
                .border(1.dp, Color.Green)
        ) {
            Row() {
                player.hand.forEach { _ ->
                    Image(
                        modifier = Modifier
                            .size(40.dp),
                        painter = painterResource(id = R.drawable.card_back),
                        contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun DeckPlace(deck: Deck, modifier: Modifier = Modifier) {
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
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                deck.deck.forEach { _ ->
                    Image(
                        modifier = Modifier
                            .border(1.dp, Color.Cyan)
                            .size(90.dp),
                        painter = painterResource(id = R.drawable.card_back),
                        contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier.size(90.dp),
                contentAlignment = Alignment.Center

            ) {
                if (deck.discardDeck.isEmpty()) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        null,
                        tint = Navy.copy(0.9f)
                    )
                } else {
                    deck.discardDeck.forEach {

                        PlayingCard(cardAvatar = CardAvatar.setCardAvatar(it),
                            modifier = Modifier.size(90.dp))

                    }
                }
            }
        }

    }
}

@Composable
fun PlayingTable(game: GameRoom, gameViewModel: GameViewModel, navController: NavController,
modifier: Modifier = Modifier) {

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
                if (players.size >= 1) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                    ) {
                        PlayerIconLeft(player = players[0],
                            borderStroke = BorderStroke(0.dp, Color.Transparent))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (players.size >= 2) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                    ) {
                        PlayerIconRight(player = players[1])
                    }
                }
            }
            DeckPlace(deck = game.deck, modifier = Modifier
                .zIndex(1f)
//                .border(1.dp, Color.Red)
                .weight(0.8f))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .offset(y = (-20).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (players.size >= 3) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                    ) {
                        PlayerIconLeft(player = players[2],
                            borderStroke = BorderStroke(0.dp, Color.Transparent))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                ) {
                    PlayerIconRight(player = gameViewModel.currentPlayer.value, borderStroke = BorderStroke(0.dp, Color.Red))
                }
            }


        }


    }
}

