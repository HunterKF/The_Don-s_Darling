package com.example.loveletter.presentation.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.Deck
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player

@Composable
fun Game(navController: NavController, gameViewModel: GameViewModel) {

    val state by gameViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit, block = {
        gameViewModel.observeRoom()
    })

    when (state) {
        GameState.Loading -> {
            CircularProgressIndicator()
            gameViewModel.observeRoom()
        }
        is GameState.Loaded -> {
            val loaded = state as GameState.Loaded
            Text("Hey")
            GameContent(loaded.gameRoom)
        }
    }
}

@Composable
fun GameContent(game: GameRoom) {

    Box(Modifier
        .fillMaxSize()
    ) {
        Row(Modifier
            .fillMaxWidth()
            .background(Color.Blue.copy(0.5f))
            .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            game.players.forEach {
                val isTurn by remember {
                    mutableStateOf(it.isTurn)
                }
                val color by animateColorAsState(targetValue = if (isTurn) {
                    Color.Red
                } else {
                    Color.Blue
                })
                val avatar = Avatar.setAvatar(it.avatar)
                Box() {
                    Image(
                        painter = painterResource(id = avatar.avatar),
                        contentDescription = avatar.description,
                        modifier = Modifier
                            .scale(0.7f)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .border(2.dp, color, CircleShape)
                            .align(Alignment.Center)
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = it.nickName,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )

                }

            }
        }
        Text(game.deck.deck.size.toString())
        Box(Modifier
            .padding(start = 8.dp)
            .align(Alignment.CenterStart)
            .height(150.dp)
            .width(60.dp)
            .background(Color.Black)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                game.deck.deck.forEach { int ->
                    Divider(
                        color = Color.Red,
                        modifier = Modifier
                            .zIndex(1f)
                            .fillMaxHeight()
                            .width(1.dp)
                            .padding(horizontal = 0.5.dp)
                    )
                }
            }

        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun GameRoomPreview() {
    val gameRoom = GameRoom()
    GameContent(game = gameRoom)
}