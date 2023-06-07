package com.example.thedonsdarling.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.items
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.ui.theme.Navy
import com.example.thedonsdarling.ui.theme.WarmRed
import com.example.thedonsdarling.domain.util.user.HandleUser
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
                contentDescription = stringResource(id = R.string.host_game),
                tint = Black
            )
        }
    }
}