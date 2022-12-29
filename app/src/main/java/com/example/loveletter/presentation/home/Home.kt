package com.example.loveletter.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.FirestoreUser
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.mygames.JoinedGameCard
import com.example.loveletter.presentation.mygames.MyGamesState
import com.example.loveletter.presentation.mygames.MyGamesViewModel
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.OffWhite
import com.example.loveletter.ui.theme.Steel
import com.example.loveletter.util.user.HandleUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    myGamesViewModel: MyGamesViewModel,
    gameViewModel: GameViewModel

    ) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        if (currentUser != null) {
            HandleUser.createUserPlayer()
        }
    })
    val state by myGamesViewModel.state.collectAsState()
    when (state) {
        MyGamesState.Loading -> {
            CircularProgressIndicator()
            LaunchedEffect(key1 = Unit) {
                myGamesViewModel.observeRoom()
            }
        }
        is MyGamesState.Loaded -> {
            val loaded = state as MyGamesState.Loaded

            HomeContent(
                navHostController = navHostController,
                currentUser = currentUser,
                gameViewModel = gameViewModel,
                context = context,
                firestoreUser = loaded.firestoreUser)
        }
    }
}

@Composable
private fun HomeContent(
    navHostController: NavHostController,
    currentUser: FirebaseUser,
    gameViewModel: GameViewModel,
    context: Context,
    firestoreUser: FirestoreUser,
) {
    Surface(Modifier.fillMaxSize(),
        color = DarkNavy) {
        LazyColumn(Modifier.padding(vertical = 48.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeButton(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                        text = "Host",
                        icon = R.drawable.plus,
                        onClick = {
                            navHostController.navigate(Screen.HostPlayer.route)
                        })
                    HomeButton(modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                        text = "Join",
                        icon = R.drawable.enter,
                        onClick = {
                            navHostController.navigate(Screen.JoinGame.route)
                        }
                    )
                }
            }
            item {
                Text(
                    text = stringResource(R.string.my_games),
                    style = MaterialTheme.typography.h4,
                    color = Color.White
                )
            }
            items(firestoreUser.joinedGames) { game ->
                if (currentUser != null) {
                    JoinedGameCard(game,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)) {
                        gameViewModel.roomCode.value = game.roomCode
                        Log.d(TAG, game.roomCode)
                        navHostController.navigate(Screen.Game.route) {
                            this.popUpToId
                        }
                    }
                }
            }
            item {
                OutlinedButton(onClick = {
                    Toast.makeText(context,
                        "Current user: ${currentUser.uid}",
                        Toast.LENGTH_SHORT).show()
                }) {
                    Text(stringResource(R.string.test_name))
                }
            }
        }
    }
}

@Composable
private fun HomeButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    icon: Int,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .border(2.dp, Steel, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(Navy)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h6,
                color = OffWhite
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Host game",
                tint = OffWhite
            )
        }
    }
}