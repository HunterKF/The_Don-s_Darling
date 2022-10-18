package com.example.loveletter.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.util.startgame.StartGame

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateRoom(navController: NavHostController) {
    val players = listOf(
        "Rick",
        "Bob",
        "Alice",
        "Tom"
    )
    val icons = listOf(
        R.drawable.bluechar,
        R.drawable.greenchar,
        R.drawable.goldchar,
        R.drawable.pinkchar,
        R.drawable.purplechar,
        R.drawable.redchar
    )

    var ready by remember {
        mutableStateOf(false)
    }
    var roomCode by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        roomCode = StartGame.getRandomString()
        StartGame.createCodedRoom(roomCode = roomCode)
    }
    Surface(Modifier.fillMaxSize()) {
        Box() {
            IconButton(modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp),
                onClick = {
                    navController.popBackStack()
                    StartGame.deleteRoom(roomCode)
                }) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    stringResource(R.string.go_back)
                )
            }
            Column(Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(roomCode, style = MaterialTheme.typography.h2)
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Rounded.Share,
                            null
                        )
                    }
                }
                Column(Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .alpha(0.5f)) {

                    players.forEach {
                        Row(Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {
                            Text(it, style = MaterialTheme.typography.h6)
                        }
                    }
                }
                LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 100.dp)) {
                    items(icons) { icon ->
                        Image(painterResource(id = icon), null)
                    }
                }
                if (!ready) {
                    OutlinedButton(onClick = { ready = true }) {
                        Text("Ready?")
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { StartGame.createRoom(
                            roomNickname = "suck it",
                            5,
                            players = players,
                            roomCode = roomCode
                        ) }) {
                            Icon(Icons.Rounded.Check, stringResource(R.string.confirm_ready))
                        }
                        Button(onClick = { ready = false }) {
                            Icon(Icons.Rounded.Close, stringResource(R.string.cancel_ready))
                        }
                    }
                }
            }
            IconButton(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp),
                onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Rounded.Email,
                    stringResource(R.string.open_chat)
                )
            }
        }

    }
    BackHandler() {
        StartGame.deleteRoom(roomCode)
        navController.popBackStack()
    }
}