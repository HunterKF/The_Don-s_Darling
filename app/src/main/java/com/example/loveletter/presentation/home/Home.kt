package com.example.loveletter.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.loveletter.presentation.mygames.JoinedGameCard
import com.example.loveletter.presentation.mygames.MyGamesState
import com.example.loveletter.presentation.mygames.MyGamesViewModel
import com.example.loveletter.ui.theme.Black
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.WarmRed
import com.example.loveletter.util.user.HandleUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    gameViewModel: GameViewModel,

    ) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        if (currentUser != null) {
            HandleUser.createUserPlayer()
        }
    })
    HomeContent(
        navHostController = navHostController,
        currentUser = currentUser,
        gameViewModel = gameViewModel,
        context = context,
    )
}

@Composable
private fun HomeContent(
    navHostController: NavHostController,
    currentUser: FirebaseUser,
    gameViewModel: GameViewModel,
    context: Context,
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
        Column(Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {

            Image(painter = painterResource(id = R.drawable._12_512_b), contentDescription = null)

            HomeButton(modifier = Modifier.aspectRatio(3f),
                text = stringResource(R.string.host),
                icon = R.drawable.plus,
                onClick = {
                    navHostController.navigate(Screen.HostPlayer.route)
                })

            HomeButton(modifier = Modifier.aspectRatio(3f),
                text = stringResource(R.string.join),
                icon = R.drawable.enter,
                onClick = {
                    navHostController.navigate(Screen.JoinGame.route)
                }
            )
            HomeButton(modifier = Modifier.aspectRatio(3f),
                text = stringResource(R.string.rules),
                icon = R.drawable.icon_rules,
                onClick = {
                    navHostController.navigate(Screen.Rules.route)
                }
            )


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
            .clip(RoundedCornerShape(25.dp))
            .background(Navy)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h6,
                color = Black
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Host game",
                tint = Black
            )
        }
    }
}