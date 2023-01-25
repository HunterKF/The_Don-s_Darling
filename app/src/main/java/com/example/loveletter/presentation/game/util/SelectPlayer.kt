package com.example.loveletter.presentation.game.util

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.loveletter.R
import com.example.loveletter.TAG
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.GameRoom
import com.example.loveletter.domain.Player
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.util.CustomTextButton
import com.example.loveletter.ui.theme.*

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
            .border(4.dp, MaterialTheme.colors.primary, RoundedCornerShape(15.dp)),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                gameRoom.players.forEach {

                    if (it.uid != gameViewModel.currentUser!!.uid) {
                        val avatar = Avatar.setAvatar(it.avatar)
                        val color by animateColorAsState(targetValue = if (it == selectedPlayer.value) WarmRed
                        else MaterialTheme.colors.primary)

                        val dp by animateDpAsState(targetValue = if (it == selectedPlayer.value) 130.dp else 120.dp)

                        Box(
                            modifier = Modifier
                                .height(dp)
                                .weight(1f)
                                .selectable(
                                    selected = it.uid == selectedPlayer.value.uid,
                                    onClick = {
                                        if (selectedPlayer.value != it) {
                                            selectedPlayer.value = it
                                        } else {
                                            selectedPlayer.value = Player()
                                        }
                                        Log.d(TAG, "It has been clicked.")
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(0.5f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (it.protected) {
                                        Box(
                                            modifier = Modifier
                                                .zIndex(2f)
                                                .align(Alignment.Center)
                                                .fillMaxSize()

                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(55.dp)
                                                    .zIndex(2f)
                                                    .align(Alignment.Center)
                                                    .clip(CircleShape)
                                                    .background(Black.copy(0.5f))
                                            )
                                            Icon(
                                                painterResource(id = R.drawable.shield),
                                                null,
                                                tint = MaterialTheme.colors.onPrimary,
                                                modifier = Modifier
                                                    .zIndex(3f)
                                                    .align(Alignment.Center)
                                                    .size(55.dp),
                                            )
                                        }
                                    } else if (!it.isAlive) {
                                        Box(
                                            modifier = Modifier
                                                .zIndex(2f)
                                                .align(Alignment.Center)
                                                .fillMaxHeight()
                                                .clip(CircleShape)
                                                .background(WarmRed.copy(0.8f))
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
                                            .border(4.dp, color, CircleShape)
                                    )
                                    Spacer(Modifier.padding(12.dp))
                                }
                                Text(text = it.nickName)

                            }
                        }

                    }
                }

            }
            Spacer(Modifier.height(10.dp))

            CustomTextButton(
                enabled = selectedPlayer.value.uid != "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(15.dp)),
                onClick = {
                    gameViewModel.onSelectPlayer(
                        selectedPlayer = selectedPlayer.value,
                        gameRoom = gameRoom
                    )
                },
                text = stringResource(R.string.select),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}



