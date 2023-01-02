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
import androidx.compose.ui.zIndex
import com.example.loveletter.R
import com.example.loveletter.TAG
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.OffWhite
import com.example.loveletter.ui.theme.Steel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SelectPlayer(
    gameRoom: GameRoom,
    gameViewModel: GameViewModel,
) {
    val context = LocalContext.current
    var selectedPlayer = remember { mutableStateOf(Player()) }

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

                    if (it.uid != gameViewModel.currentUser!!.uid) {
                        val avatar = Avatar.setAvatar(it.avatar)
                        val color by animateColorAsState(targetValue = if (it == selectedPlayer.value) Color.Blue.copy(
                            1f)
                        else Color.Red)

                        val dp by animateDpAsState(targetValue = if (it == selectedPlayer.value) 130.dp else 120.dp)

                        Card(
                            modifier = Modifier
                                .height(dp)
                                .weight(1f)
                                .selectable(
                                    selected = it.uid == selectedPlayer.value.uid,
                                    onClick = {
                                        selectedPlayer.value = it
                                        Toast
                                            .makeText(context,
                                                "It has been selected. Player: ${selectedPlayer.value}",
                                                Toast.LENGTH_SHORT)
                                            .show()
                                        Log.d(TAG, "It has been clicked.")
                                    }
                                ),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (it.protected) {
                                        Box(
                                            modifier = Modifier
                                                .zIndex(2f)
                                                .align(Alignment.Center)
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                                .background(Navy.copy(0.6f))
                                        ) {
                                            Icon(
                                                painterResource(id = R.drawable.shield),
                                                null,
                                                tint = Steel,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .size(55.dp),
                                            )
                                        }
                                    } else if (!it.isAlive) {
                                        Box(
                                            modifier = Modifier
                                                .zIndex(2f)
                                                .align(Alignment.Center)
                                                .fillMaxSize()
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
                                            .clip(CircleShape)
                                            .border(6.dp, color, CircleShape)
                                    )
                                    Spacer(Modifier.padding(12.dp))
                                }
                                Text(text = it.nickName)

                            }
                        }

                    }
                }

            }
            Spacer(Modifier.height(20.dp))

            Button(
                enabled = selectedPlayer.value.uid != "",
                modifier = Modifier
                    .weight(0.5f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = Color.Gray,
                    disabledContentColor = OffWhite,
                    backgroundColor = Navy,
                    contentColor = Color.White
                ),
                onClick = {
                    gameViewModel.onSelectPlayer(selectedPlayer = selectedPlayer.value,
                        gameRoom = gameRoom)
                },
            ) {
                Icon(
                    Icons.Rounded.Check,
                    "Select player",
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp),
                    )
            }
        }
    }
}



