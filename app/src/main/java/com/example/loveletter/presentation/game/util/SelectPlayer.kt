package com.example.loveletter.presentation.game.util

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loveletter.TAG
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SelectPlayer(
    gameRoom: GameRoom,
    selectPlayer: MutableState<Boolean>,
    gameViewModel: GameViewModel,
) {
    val context = LocalContext.current
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
                var selectedIndex by remember {
                    mutableStateOf(-1)
                }
                var index = mutableStateOf(1)
                var selectedPlayer = mutableStateOf(Player())

                gameRoom.players.forEach {
                    val color =
                        if (it == selectedPlayer.value) Color.Blue.copy(1f)
                        else Color.Red
                    if (it.uid != gameViewModel.currentUser!!.uid) {
                        val avatar = Avatar.setAvatar(it.avatar)
                        val currentIndex = index.value

                        Card(
                            modifier = Modifier
                                .height(100.dp)
                                .aspectRatio(1f)
                                .weight(1f)
                                .selectable(
                                    selected = selectedIndex == index.value,
                                    onClick = {
                                        selectedIndex = index.value
                                        selectedPlayer.value = it
                                        Toast
                                            .makeText(context,
                                                "It has been selected. Player: ${selectedPlayer.value}",
                                                Toast.LENGTH_SHORT)
                                            .show()
                                        Log.d(TAG, "currentIndex: $currentIndex")
                                        Log.d(TAG, "index: ${index.value}")
                                        Log.d(TAG, "selectedIndex: $selectedIndex")
                                        Log.d(TAG, "color2: $color")
                                    }
                                ),
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
                                        .border(6.dp, color, CircleShape)
                                )
                                Spacer(Modifier.padding(12.dp))
                                Text(text = it.nickName)
                            }
                        }
                        index.value += 1
                    }
                }

            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                /*IconButton(onClick = { selectPlayer.value = false },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 16.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(15.dp))) {
                    Icon(
                        Icons.Rounded.Close,
                        "Cancel"
                    )
                }*/
                IconButton(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 16.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(15.dp))) {
                    Icon(
                        Icons.Rounded.Check,
                        "Select player"
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
    val gameViewModel = GameViewModel()
    Surface {
        SelectPlayer(gameRoom, selectPlayer, gameViewModel)
    }
}