package com.example.thedonsdarling.presentation.joingame

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.sharp.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.presentation.createplayer.AvatarImage
import com.example.thedonsdarling.presentation.util.CustomTextButton
import com.example.thedonsdarling.ui.theme.Black
import com.example.thedonsdarling.ui.theme.WarmRed
import com.example.thedonsdarling.data.gameserver.ConnectionRules
import com.example.thedonsdarling.domain.util.user.HandleUser

@Composable
fun JoinGameScreen(navController: NavHostController, gameLobbyViewModel: GameLobbyViewModel) {

    val roomFound = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        gameLobbyViewModel.clearData()
    }
    Scaffold(
        backgroundColor = Black
    ) {


        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(18.dp)
                .border(5.dp, MaterialTheme.colors.onPrimary, RectangleShape)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = {
                        gameLobbyViewModel.clearData()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.JoinGame.route) {
                                inclusive = true
                            }
                        }
                    }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        null,
                        tint = MaterialTheme.colors.onPrimary
                    )

                }
                Text(
                    text = stringResource(id = R.string.join_game),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.room_code),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(Modifier.height(4.dp))

                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                    value = gameLobbyViewModel.roomCode.value,
                    onValueChange = { newValue ->
                        gameLobbyViewModel.roomCode.value = newValue
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(focusDirection = FocusDirection.Down)
                        }
                    ),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    placeholder = {
                        Text(
                            stringResource(id = R.string.enter_room_code),
                            fontSize = 16.sp
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (gameLobbyViewModel.roomCode.value == "") {
                                Toast.makeText(context, R.string.enter_room_code, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                roomFound.value =
                                    ConnectionRules.checkGame(roomCode = gameLobbyViewModel.roomCode.value,
                                        roomFound = roomFound,
                                        context = context
                                    )
                            }
                        }) {
                            Icon(
                                Icons.Sharp.Search,
                                null
                            )
                        }
                    }
                )
            }

            Spacer(Modifier.height(4.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.enter_nickname),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(Modifier.height(4.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    value = gameLobbyViewModel.playerNickname.value,
                    onValueChange = { newValue ->
                        gameLobbyViewModel.playerNickname.value = newValue
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done

                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    singleLine = true,

                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.onPrimary,
                        focusedLabelColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    placeholder = {
                        Text(
                            stringResource(id = R.string.enter_player_nickname),
                            fontSize = 16.sp
                        )
                    },
                )
            }
            Spacer(Modifier.height(8.dp))




            val topIcons = listOf(
                1,
                2,
                3,
            )
            val bottomIcons = listOf(
                4,
                5,
                6,
            )

            var selectedIndex by remember {
                mutableStateOf(-1)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                topIcons.forEach { icon ->
                    val avatar = Avatar.setAvatar(icon)

                    AvatarImage(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedIndex == icon,
                                onClick = {
                                    selectedIndex = icon
                                    gameLobbyViewModel.playerChar.value =
                                        gameLobbyViewModel.assignCharNumber(icon)
                                    focusManager.clearFocus()
                                }
                            ),
                        icon = avatar.avatar,
                        background = if (selectedIndex == icon) WarmRed else Color.Transparent
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                bottomIcons.forEach { icon ->
                    val avatar = Avatar.setAvatar(icon)

                    AvatarImage(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedIndex == icon,
                                onClick = {
                                    selectedIndex = icon
                                    gameLobbyViewModel.playerChar.value =
                                        gameLobbyViewModel.assignCharNumber(icon)
                                    focusManager.clearFocus()
                                }
                            ),
                        icon = avatar.avatar,
                        background = if (selectedIndex == icon) WarmRed else Color.Transparent
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            CustomTextButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = gameLobbyViewModel.playerNickname.value != "" && gameLobbyViewModel.playerChar.value != 0 && gameLobbyViewModel.roomCode.value != "",
                text = stringResource(id = R.string.join_game),
                onClick = {
                    ConnectionRules.joinGame(
                        roomCode = gameLobbyViewModel.roomCode.value,
                        player = HandleUser.createGamePlayer(avatar = gameLobbyViewModel.playerChar.value,
                            nickname = gameLobbyViewModel.playerNickname.value, isHost = false),
                        context = context
                    ) {
                        navController.navigate(Screen.GameLobby.route)
                    }
                }
            )
        }
    }
}

