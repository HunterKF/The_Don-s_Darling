package com.example.loveletter.presentation.mygames

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.FirestoreUser
import com.example.loveletter.items
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.ui.theme.Black
import com.example.loveletter.ui.theme.WarmRed
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun MyGames(
    myGamesViewModel: MyGamesViewModel,
    navHostController: NavHostController,
    gameViewModel: GameViewModel,
) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val context = LocalContext.current
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

            MyGamesContent(
                navHostController = navHostController,
                currentUser = currentUser,
                context = context,
                firestoreUser = loaded.firestoreUser,
                gameViewModel = gameViewModel
            )
        }
    }
}

@Composable
private fun MyGamesContent(
    navHostController: NavHostController,
    currentUser: FirebaseUser,
    gameViewModel: GameViewModel,
    context: Context,
    firestoreUser: FirestoreUser,
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Black,
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(0.dp)),
                backgroundColor = Color.White
            ) {
                items.forEach { screen ->
                    var vector = R.drawable.icon_end_game
                    screen.vector?.let {
                        vector = it
                    }
                    BottomNavigationItem(
                        modifier = Modifier.background(MaterialTheme.colors.primary),
                        icon = {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(id = vector),
                                contentDescription = null,
                                tint = if (currentDestination?.route == screen.route) WarmRed else Color.White)
                        },

                        label = {
                            Text(
                                text = screen.label,
                                color = if (currentDestination?.route == screen.route) WarmRed else Color.White

                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navHostController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        LazyColumn(Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            item {
                Image(painter = painterResource(id = R.drawable._12_512_b),
                    contentDescription = null)
            }
            item {
                Text(
                    text = stringResource(R.string.my_games),
                    style = MaterialTheme.typography.h4,
                    color = Color.White
                )
            }
            items(firestoreUser.joinedGames) { game ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (firestoreUser.joinedGames.isEmpty()) {
                        Text(text = "Start playing today!", color = Color.White)
                    }
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

            }
        }

    }

}