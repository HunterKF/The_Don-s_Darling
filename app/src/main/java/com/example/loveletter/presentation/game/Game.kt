package com.example.loveletter.presentation.game

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.*
import com.example.loveletter.presentation.game.util.*
import com.example.loveletter.util.Tools
import com.example.loveletter.util.game.gamerules.CardRules.Countess
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
            GameContent(loaded.gameRoom, gameViewModel, navController)
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameContent(game: GameRoom, gameViewModel: GameViewModel, navController: NavController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val currentPlayer by remember {
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





    BackHandler(true) {
        Log.d(TAG, "HEYYYY YOU GUYYYYYSSSSS")
    }

    Scaffold(

    ) {
        ModalDrawer(drawerState = drawerState, drawerContent = {
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
                OutlinedButton(onClick = {
                    navController.navigate(Screen.Home.route)
                }) {
                    Icon(
                        Icons.Rounded.Home,
                        null
                    )
                    Text("Back to home")
                }
            }
        }) {

            Column(Modifier
                .fillMaxSize()
            ) {
                //Players in the room
                Row(Modifier
                    .fillMaxWidth()
                    .background(Color.Blue.copy(0.5f))
                    .padding(bottom = 8.dp)
                    .weight(0.15f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    val playerList = gameViewModel.removeCurrentPlayer(game.players)

                    playerList.forEach {
//                        Log.d(TAG, "${it.nickName}'s turn is: ${it.turn}")
                        if (it.uid != HandleUser.returnUser()?.uid) {
                            val color by animateColorAsState(targetValue = if (it.turn) {
                                Color.Green
                            } else {
                                Color.Yellow
                            })
                            val avatar = Avatar.setAvatar(it.avatar)
                            Box() {
                                if (!it.isAlive) {
                                    Icon(
                                        Icons.Rounded.Close,
                                        null,
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .zIndex(1f)
                                    )
                                }
                                Image(
                                    painter = painterResource(id = avatar.avatar),
                                    contentDescription = avatar.description,
                                    modifier = Modifier
                                        .scale(0.7f)
                                        .padding(8.dp)
                                        .clip(CircleShape)
                                        .border(4.dp, color, CircleShape)
                                        .align(Alignment.Center)
                                )
                                Spacer(Modifier.size(4.dp))
                                Text(
                                    text = it.nickName,
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                )

                            }
                        }
                    }
                }
                Button(onClick = { GameRules.startNewGame(gameRoom = game) }) {
                    Text("Restart game")
                }
                Text(
                    text = "Current turn: ${game.turn}"
                )
                Text(modifier = Modifier,
                    text = game.deck.deck.toString()
                )
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
                //Center card
                Box(Modifier
                    .fillMaxWidth()
                    .weight(0.6f)) {
                    Text(modifier = Modifier.align(Alignment.CenterEnd),
                        text = game.deck.deck.size.toString()
                    )
                    //Deck
                    Box(Modifier
                        .padding(start = 8.dp)
                        .height(150.dp)
                        .width(60.dp)
                        .background(Color.Black)
                        .align(Alignment.CenterStart)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            game.deck.deck.forEach { int ->
                                Divider(
                                    color = Color.Red,
                                    modifier = Modifier
                                        .zIndex(1f)
                                        .fillMaxHeight()
                                        .width(1.dp)
                                        .padding(horizontal = 0.5.dp)
                                )
                            }
                        }

                    }
                    Box(modifier = Modifier.align(Alignment.Center)) {

                        if (game.deck.discardDeck.isEmpty()) {
                            Box(Modifier
                                .width(160.dp)
                                .height(230.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .border(2.dp, Color.LightGray, RoundedCornerShape(5.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.ShoppingCart,
                                    null
                                )
                            }
                        } else {
                            game.deck.discardDeck.forEach {
                                Box(
                                    modifier = Modifier.rotate(gameViewModel.randomFloat())
                                ) {
                                    PlayingCard(cardAvatar = CardAvatar.setCardAvatar(it))
                                }
                            }

                        }
                    }
                }


                var selectedIndex by remember {
                    mutableStateOf(-1)
                }

                //Player hand
                Box(
                    Modifier.weight(0.4f),
                ) {
                    game.players.forEach { player ->
                        if (player.uid == HandleUser.returnUser()!!.uid) {
                            if (player.hand.isEmpty()) {
                                Column() {
                                    Text(
                                        text = "You were eliminated",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = "Please wait till a new round begins.",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            } else {

                                player.hand.let { hand ->
                                    var hasCountess by remember {
                                        mutableStateOf(false)
                                    }
                                    hasCountess = Countess.checkHand(hand)

                                    LazyRow(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        itemsIndexed(hand) { index, cardNumber ->
                                            if (hand.size == 2) {

                                                var notPlayable by remember {
                                                    mutableStateOf(false)
                                                }
                                                if (hasCountess) {
                                                    notPlayable = Countess.checkCard(cardNumber)
                                                }


                                                val scale by animateFloatAsState(targetValue = if (selectedIndex == index) 0.8f else 0.6f)
                                                val offset by animateDpAsState(targetValue = if (selectedIndex == index) (-50).dp else 0.dp)

                                                Box(contentAlignment = Alignment.Center) {
                                                    PlayingCard(cardAvatar = CardAvatar.setCardAvatar(
                                                        cardNumber),
                                                        notPlayable = notPlayable,
                                                        modifier = Modifier
                                                            .scale(scale)
                                                            .offset(y = offset)
                                                            .selectable(
                                                                selected = selectedIndex == index,
                                                                onClick = {
                                                                    selectedIndex =
                                                                        if (selectedIndex == index) {
                                                                            -1
                                                                        } else {
                                                                            index
                                                                        }
                                                                    if ((cardNumber == 5 || cardNumber == 6) && hasCountess) {
                                                                        Toast
                                                                            .makeText(context,
                                                                                "Please play the Countess.",
                                                                                Toast.LENGTH_SHORT)
                                                                            .show()
                                                                    }

                                                                }
                                                            )
                                                    )

                                                    if (selectedIndex == index) {
                                                        if ((cardNumber == 5 || cardNumber == 6) && hasCountess) {

                                                        } else {
                                                            OutlinedButton(modifier = Modifier.align(
                                                                Alignment.BottomCenter),
                                                                onClick = {
                                                                    gameViewModel.onPlay(
                                                                        card = cardNumber,
                                                                        player = player,
                                                                        gameRoom = game
                                                                    )
                                                                }) {
                                                                Text("Play")
                                                            }
                                                        }

                                                    }
                                                }

                                            } else {
                                                var selected by remember {
                                                    mutableStateOf(false)
                                                }
                                                val scale by animateFloatAsState(targetValue = if (selected) 0.8f else 0.6f)
                                                Box(contentAlignment = Alignment.Center) {
                                                    PlayingCard(modifier = Modifier
                                                        .scale(scale)
                                                        .clickable {
                                                            selected = !selected
                                                        },
                                                        cardAvatar = CardAvatar.setCardAvatar(
                                                            cardNumber)
                                                    )
                                                }
                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }

                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Blue)
                        .weight(0.1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Opens modal drawer
                    IconButton(
                        modifier = Modifier,
                        onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            Icons.Rounded.Menu,
                            null,
                            tint = Color.Green
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatar = Avatar.setAvatar(currentPlayer.avatar!!)
                        val color by animateColorAsState(targetValue = if (currentPlayer.turn) {
                            Color.Red
                        } else {
                            Color.Blue
                        })
                        Box() {
                            Image(
                                painter = painterResource(id = avatar.avatar),
                                contentDescription = avatar.description,
                                modifier = Modifier
                                    .scale(0.7f)
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, color, CircleShape)
                            )
                            if (!currentPlayer.isAlive) {
                                Icon(
                                    Icons.Rounded.Close,
                                    null,
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .zIndex(1f)
                                )
                            }
                        }
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = currentPlayer.nickName,
                            style = MaterialTheme.typography.subtitle1,
                        )
                    }
                    IconButton(
                        modifier = Modifier,
                        onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Rounded.Email,
                            null,
                            tint = Color.Green
                        )
                    }
                }

            }
        }
    }

}

