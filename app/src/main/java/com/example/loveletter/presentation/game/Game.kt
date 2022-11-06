package com.example.loveletter.presentation.game

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.loveletter.Screen
import com.example.loveletter.domain.*
import com.example.loveletter.presentation.game.util.Card
import com.example.loveletter.presentation.game.util.MenuItem
import com.example.loveletter.util.user.HandleUser
import kotlinx.coroutines.launch

@Composable
fun Game(navController: NavController, gameViewModel: GameViewModel) {

    val state by gameViewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit, block = {
        gameViewModel.observeRoom()
    })

    when (state) {
        GameState.Loading -> {
            CircularProgressIndicator()
            gameViewModel.observeRoom()
        }
        is GameState.Loaded -> {
            val loaded = state as GameState.Loaded
            Text("Hey")
            GameContent(loaded.gameRoom, gameViewModel, navController)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameContent(game: GameRoom, gameViewModel: GameViewModel, navController: NavController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val currentPlayer by remember {
        mutableStateOf(HandleUser.getCurrentUser(game.players, currentUser = HandleUser.returnUser()!!.uid))
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
                        println("${it.nickName}'s turn is: ${it.turn}")
                        if (it.uid != HandleUser.returnUser()?.uid) {
                            val color by animateColorAsState(targetValue = if (it.turn) {
                                Color.Red
                            } else {
                                Color.Blue
                            })
                            val avatar = Avatar.setAvatar(it.avatar)
                            Box() {
                                Image(
                                    painter = painterResource(id = avatar.avatar),
                                    contentDescription = avatar.description,
                                    modifier = Modifier
                                        .scale(0.7f)
                                        .padding(8.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, color, CircleShape)
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


                //Center card
                Box(Modifier
                    .fillMaxWidth()
                    .weight(0.6f)) {

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

                        if (game.deck.discardDeck!!.isEmpty()) {
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
                                    Card(cardAvatar = CardAvatar.setCardAvatar(it))
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
                            player.hand.let {
                                LazyRow(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    itemsIndexed(it) { index, cardNumber ->
                                        if (it.size == 2) {

                                            val scale by animateFloatAsState(targetValue = if (selectedIndex == index) 0.8f else 0.6f)
                                            val offset by animateDpAsState(targetValue = if (selectedIndex == index) (-50).dp else 0.dp)
                                            Box(contentAlignment = Alignment.Center) {
                                                Card(cardAvatar = CardAvatar.setCardAvatar(cardNumber),
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
                                                            }
                                                        )
                                                )
                                                if (selectedIndex == index) {
                                                    OutlinedButton(modifier = Modifier.align(Alignment.BottomCenter),
                                                        onClick = { /*TODO*/ }) {
                                                        Text("Play")
                                                    }
                                                }
                                            }

                                        } else {
                                            var selected by remember {
                                                mutableStateOf(false)
                                            }
                                            val scale by animateFloatAsState(targetValue = if (selected) 0.8f else 0.6f)
                                            Box(contentAlignment = Alignment.Center) {
                                                Card(cardAvatar = CardAvatar.setCardAvatar(cardNumber),
                                                    modifier = Modifier
                                                        .scale(scale)
                                                        .clickable {
                                                            selected = !selected
                                                        }
                                                )
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
                        Image(
                            painter = painterResource(id = avatar.avatar),
                            contentDescription = avatar.description,
                            modifier = Modifier
                                .scale(0.7f)
                                .padding(8.dp)
                                .clip(CircleShape)
                                .border(2.dp, color, CircleShape)
                        )
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

