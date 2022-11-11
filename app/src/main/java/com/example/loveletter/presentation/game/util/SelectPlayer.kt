package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom

@Composable
fun SelectPlayer(gameRoom: GameRoom, selectPlayer: MutableState<Boolean>) {

    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.5f)
            .padding(16.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(15.dp))
            .border(2.dp, Color.Blue, RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select a player",
                style = MaterialTheme.typography.h4
            )
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                gameRoom.players.forEach {
                    val avatar = Avatar.setAvatar(it.avatar)
                    Card(
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = avatar.avatar),
                                contentDescription = avatar.description,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Green, CircleShape)
                            )
                            Spacer(Modifier.padding(12.dp))
                            Text(text = it.nickName)
                        }
                    }
                }

            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { selectPlayer.value = false },
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


@Preview(showSystemUi = true)
@Composable
fun Preview1() {
    val gameRoom = GameRoom()
    val selectPlayer = remember { mutableStateOf(true) }
    Surface {
        SelectPlayer(gameRoom, selectPlayer)
    }
}