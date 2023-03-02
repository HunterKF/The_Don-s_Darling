package com.example.thedonsdarling.presentation.mygames

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.FirestoreUser
import com.example.thedonsdarling.items
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.ui.theme.WarmRed

@Composable
fun MyGames(
    myGamesViewModel: MyGamesViewModel = hiltViewModel(),
    navHostController: NavHostController,
    gameViewModel: GameViewModel,
) {
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
                firestoreUser = loaded.firestoreUser,
                gameViewModel = gameViewModel
            )
        }
    }
}

@Composable
private fun MyGamesContent(
    navHostController: NavHostController,
    gameViewModel: GameViewModel,
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
                var tabIndex by remember {
                    mutableStateOf(0)
                }
                TabRow(
                    selectedTabIndex = tabIndex,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary
                ) {
                    items.forEachIndexed { i, item ->
                        Tab(
                            selected = tabIndex == i,
                            onClick = { tabIndex = i },
                            modifier = Modifier.heightIn(48.dp)
                        ) {
                            item.vector?.let {
                                Column() {
                                    Icon(
                                        painter = painterResource(id = item.vector),
                                        contentDescription = item.label,
                                        tint = if (i == tabIndex) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            MaterialTheme.colors.onSurface.copy(alpha = 0.44f)
                                        }
                                    )

                                    Text(
                                        text = item.label,
                                        color = if (currentDestination?.route == item.route) WarmRed else Color.White

                                    )
                                }
                            }

                        }
                    }
                }

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
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {


        LazyColumn(Modifier
            .fillMaxSize()
            .padding(bottom = it.calculateBottomPadding(), start = 16.dp, end = 16.dp, top = 8.dp),
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
                        Text(text = stringResource(R.string.my_games_empty_game_message),
                            color = Color.White)
                    }
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