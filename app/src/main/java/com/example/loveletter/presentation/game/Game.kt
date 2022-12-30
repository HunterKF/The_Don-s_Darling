package com.example.loveletter.presentation.game

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
import androidx.navigation.NavController
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.*
import com.example.loveletter.presentation.game.util.*
import com.example.loveletter.presentation.messenger.Messenger
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.OffWhite
import com.example.loveletter.ui.theme.Steel
import com.example.loveletter.util.Tools
import com.example.loveletter.util.game.gamerules.GameRules
import com.example.loveletter.util.user.HandleUser
import kotlinx.coroutines.launch

@Composable
fun Game(navController: NavController, gameViewModel: GameViewModel) {

    val state by gameViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit, block = {
        Log.d(TAG, "Launched effect in game is being called.")
        gameViewModel.observeRoom()
    })

    when (state) {
        GameState.Loading -> {
            CircularProgressIndicator()
            Log.d(TAG, "Loading state is being called.")

        }
        is GameState.Loaded -> {
            val loaded = state as GameState.Loaded
            Text("Hey")
            Log.d(TAG, "GameState is now set to Loaded.")
            Log.d(TAG, "Refreshing game state composable.")
            LaunchedEffect(key1 = Unit, block = {
                gameViewModel.chatOpen.value = false
                gameViewModel.listOfPlayers.value = loaded.gameRoom.players
            })
            if (gameViewModel.chatOpen.value) {
                Messenger(gameRoom = loaded.gameRoom, gameViewModel = gameViewModel)
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

    var currentPlayer by remember {
        mutableStateOf(HandleUser.getCurrentUser(game.players,
            currentUser = HandleUser.returnUser()!!.uid))
    }


    LaunchedEffect(key1 = game) {
        val isHost = Tools.getHost(game.players, gameViewModel.currentUser)

        if (gameViewModel.currentPlayer.value.uid == "") {
            gameViewModel.currentPlayer.value =
                Tools.getPlayer(game.players, gameViewModel.currentUser)
        }
        Log.d("LaunchedEffect", "LaunchedEffect has been called. ")
        game.players.forEach {
            if (it.uid == currentPlayer.uid) {
                gameViewModel.currentPlayer.value = it
                currentPlayer = it
            }
        }

        val playerIsPlaying = Tools.checkCards(game.players)
        val alivePlayers = game.players.filter {
            it.isAlive
        }
        if (alivePlayers.size == 1 && !game.roundOver) {
            Log.d(TAG, "(if) ending game")
            GameRules.endRound(gameRoom = game)
            var winner = Player()
            game.players.forEach {
                if (it.isWinner) {
                    winner = it
                }
            }
            Toast.makeText(context,
                "Game finished! Winner is: ${winner.nickName}",
                Toast.LENGTH_SHORT).show()
        } else if (game.deck.deck.isEmpty() && !playerIsPlaying && !game.roundOver) {
            Log.d(TAG, "(else-if) ending game")

            GameRules.endRound(gameRoom = game)
            var winner = Player()
            game.players.forEach {
                if (it.isWinner) {
                    winner = it
                }
            }
            Toast.makeText(context,
                "Game finished! Winner is: ${winner.nickName}",
                Toast.LENGTH_SHORT).show()
        }
        if (game.roundOver && isHost) {
            Log.d(TAG, "A new round is starting.")
            GameRules.startNewGame(gameRoom = game)
        }
        if (!currentPlayer.isAlive && currentPlayer.hand.isNotEmpty()) {
            gameViewModel.eliminate(gameRoom = game, player = currentPlayer)

        }
        game.players.forEach { player ->
            Log.d("LaunchedEffect", "Going through players ")
            Log.d("LaunchedEffect", "Current player: ${player.nickName} ")
            Log.d("LaunchedEffect", "Current player turn: ${player.turn} ")
            Log.d("LaunchedEffect", "Current player hand: ${player.hand} ")
            if (player.turn && player.hand.size < 2 && player.isAlive && !player.turnInProgress) {
                Log.d("LaunchedEffect", "Matching player has been found: ${player.nickName}")
                GameRules.onTurn(
                    gameRoom = game,
                    player = player
                )
            }
            Log.d("LaunchedEffect", "LaunchedEffect is finished.")
        }


    }
    BackHandler(false) {
    }

    Scaffold {
        ModalDrawer(
            drawerState = drawerState,
            scrimColor = Steel.copy(0.5f),
            drawerBackgroundColor = DarkNavy,
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
                        MenuItem(CardAvatar.setCardAvatar(1), modifier)
                        MenuItem(CardAvatar.setCardAvatar(2), modifier)
                        MenuItem(CardAvatar.setCardAvatar(3), modifier)
                        MenuItem(CardAvatar.setCardAvatar(4), modifier)
                        MenuItem(CardAvatar.setCardAvatar(5), modifier)
                        MenuItem(CardAvatar.setCardAvatar(6), modifier)
                        MenuItem(CardAvatar.setCardAvatar(7), modifier)
                        MenuItem(CardAvatar.setCardAvatar(8), modifier)
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
                                .border(1.dp, Steel, RoundedCornerShape(10.dp))
                                .background(DarkNavy),
                            onClick = {
                                navController.navigate(Screen.Home.route)
                            }) {
                            Icon(
                                Icons.Outlined.Home,
                                null,
                                tint = Steel
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Steel, RoundedCornerShape(10.dp))
                                .background(DarkNavy),
                            onClick = { GameRules.startNewGame(gameRoom = game) }) {
                            Icon(
                                Icons.Rounded.Refresh,
                                null,
                                tint = Steel

                            )
                        }

                    }
                }
            }) {

            Column(Modifier
                .fillMaxSize()
                .background(Navy)
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
                if (gameViewModel.resultAlert.value) {
                    Popup(popupPositionProvider = WindowCenterOffsetPositionProvider(),
                        onDismissRequest = { gameViewModel.selectPlayerAlert.value = false }) {
                        ResultMessage(message = gameViewModel.resultMessage.value,
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
                            guessCard = gameViewModel.guessCardAlert,
                            gameViewModel = gameViewModel)
                    }

                }


                BottomBar(modifier = Modifier.height(100.dp),
                    player = currentPlayer,
                    game = game,
                    gameViewModel = gameViewModel,
                    hand = currentPlayer.hand) { scope.launch { drawerState.open() } }
            }
        }
    }

}


