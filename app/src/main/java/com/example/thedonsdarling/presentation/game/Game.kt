package com.example.thedonsdarling.presentation.game

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.TAG
import com.example.thedonsdarling.domain.*
import com.example.thedonsdarling.presentation.game.util.*
import com.example.thedonsdarling.presentation.messenger.Messenger
import com.example.thedonsdarling.presentation.settings.Settings
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.ui.theme.OffWhite
import com.example.thedonsdarling.util.Tools
import com.example.thedonsdarling.util.user.HandleUser
import kotlinx.coroutines.launch

@Composable
fun Game(
    navController: NavController,
    gameViewModel: GameViewModel
) {

    val state by gameViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit, block = {
        Log.d(TAG, "Launched effect in game is being called.")
        gameViewModel.observeRoom()
    })

    when (state) {
        GameState.Loading -> {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.primary
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        color = MaterialTheme.colors.onPrimary,
                        strokeWidth = 8.dp
                    )

                }

            }
            Log.d(TAG, "Loading state is being called.")

        }
        is GameState.Loaded -> {
            val loaded = state as GameState.Loaded
            Log.d(TAG, "GameState is now set to Loaded.")
            Log.d(TAG, "Refreshing game state composable.")
            LaunchedEffect(key1 = Unit, block = {
                gameViewModel.chatOpen.value = false
                gameViewModel.listOfPlayers.value = loaded.gameRoom.players
            })
            if (gameViewModel.chatOpen.value) {
                Messenger(gameRoom = loaded.gameRoom, gameViewModel = gameViewModel)
            } else if (gameViewModel.settingsOpen.value) {
                Settings(game = loaded.gameRoom, gameViewModel = gameViewModel) {
                    navController.navigate(Screen.Home.route)
                }
            } else {
                GameContent(loaded.gameRoom, gameViewModel, navController)
            }
        }
    }

}


@Composable
fun GameContent(game: GameRoom, gameViewModel: GameViewModel, navController: NavController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    /*This constantly updates the player so you always have the player's current hand*/
    var localPlayer by remember {
        mutableStateOf(HandleUser.getCurrentUser(game.players,
            currentUser = HandleUser.returnUser()!!.uid))
    }


    LaunchedEffect(key1 = game.gameLog) {
        if (game.gameLog.last().type == "gameLog") {
            Toast.makeText(context, game.gameLog.last().toastMessage, Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(key1 = game) {

        gameViewModel.declareIsHost(game)
        gameViewModel.localizeCurrentPlayer(game)
        Log.d("LaunchedEffect", "LaunchedEffect has been called. ")
        /*This constantly updates the player so you always have the player's current hand*/
        localPlayer = gameViewModel.getPlayerFromGameList(game)

        gameViewModel.handleDeletedRoom(game, navController)
        val playerIsPlaying = Tools.checkCards(game.players)
        val alivePlayers = game.players.filter {
            it.isAlive
        }

        gameViewModel.endRound(alivePlayers = alivePlayers, game = game, playerIsPlaying = playerIsPlaying)

        /*GameRules.launchOnTurn(
            game = game,
            localPlayer = localPlayer,
            isHost = gameViewModel.isHost.value
        )*/
    }

    Scaffold {
        ModalDrawer(
            drawerState = drawerState,
            scrimColor = MaterialTheme.colors.primary.copy(0.5f),
            drawerBackgroundColor = Black,
            drawerContent = {
                Column(Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    val modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        cardDefaults.forEach {
                            MenuItem(CardAvatar.setCardAvatar(it.number), modifier)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp,
                                    MaterialTheme.colors.onPrimary,
                                    RoundedCornerShape(10.dp))
                                .background(Black),
                            onClick = {
                                navController.navigate(Screen.Home.route)
                            }) {
                            Icon(
                                Icons.Outlined.Home,
                                null,
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp,
                                    MaterialTheme.colors.onPrimary,
                                    RoundedCornerShape(10.dp))
                                .background(Black),
                            onClick = {
//                                GameRules.startNewGame(gameRoom = game)
                                gameViewModel.settingsOpen.value = true
                            }) {
                            Icon(
                                Icons.Rounded.Settings,
                                null,
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }

                    }
                }
            }) {

            Column(Modifier
                .fillMaxSize()
                .background(Black)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 25.dp,
                            bottomEnd = 25.dp))
                ) {
                    PlayingTable(game = game, gameViewModel, navController,
                        modifier = Modifier
                            .background(OffWhite)
                            .fillMaxSize())
                }
                if (gameViewModel.selectPlayerAlert.value) {
                    Popup(popupPositionProvider = WindowCenterOffsetPositionProvider()) {
                        SelectPlayer(gameRoom = game,
                            gameViewModel = gameViewModel)
                    }
                }
                if (gameViewModel.revealCardAlert.value) {
                    Popup(popupPositionProvider = WindowCenterOffsetPositionProvider(),
                        onDismissRequest = { gameViewModel.revealCardAlert.value = false }) {
                        RevealCard(
                            gameViewModel = gameViewModel)
                    }
                }
                if (gameViewModel.guessCardAlert.value) {
                    Popup(popupPositionProvider = WindowCenterOffsetPositionProvider()) {

                        GuessCard(gameRoom = game,
                            gameViewModel = gameViewModel)
                    }

                }
                if (gameViewModel.endRoundAlert.value) {
                    Popup(
                        popupPositionProvider = WindowCenterOffsetPositionProvider()
                    ) {
                        RoundEndedAlert(
                            gameRoom = game,
                            gameViewModel = gameViewModel
                        )
                    }

                }


                BottomBar(
                    modifier = Modifier.height(100.dp),
                    player = localPlayer,
                    game = game,
                    gameViewModel = gameViewModel
                ) { scope.launch { drawerState.open() } }
            }
        }
    }

}






