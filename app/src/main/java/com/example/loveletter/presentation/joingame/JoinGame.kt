package com.example.loveletter.presentation.joingame

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.presentation.createplayer.AvatarImage
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.Steel
import com.example.loveletter.util.joingame.JoinGame
import com.example.loveletter.util.user.HandleUser

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JoinGameScreen(navController: NavHostController, gameLobbyViewModel: GameLobbyViewModel) {

    val roomFound = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current


    Surface(
        color = DarkNavy
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
        ) {

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(50.dp)),
                value = gameLobbyViewModel.roomCode.value,
                onValueChange = { newValue ->
                    gameLobbyViewModel.roomCode.value = newValue
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters
                ),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Navy,
                    textColor = Steel,
                    cursorColor = Steel,
                    focusedLabelColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Steel
                ),
                placeholder = {
                    Text(
                        "Enter room code here...",
                        fontSize = 16.sp,
                        color = Steel
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        roomFound.value =
                            JoinGame.checkGame(roomCode = gameLobbyViewModel.roomCode.value,
                                roomFound = roomFound,
                                context = context)
                    }) {
                        Icon(
                            Icons.Sharp.Search,
                            null,
                            tint = Steel
                        )
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50.dp)),
                value = gameLobbyViewModel.playerNickname.value,
                onValueChange = { newValue ->
                    gameLobbyViewModel.playerNickname.value = newValue
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                singleLine = true,

                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Navy,
                    textColor = Steel,
                    cursorColor = Steel,
                    focusedLabelColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Steel
                ),
                placeholder = {
                    Text(
                        "Enter your nickname here...",
                        fontSize = 16.sp,
                        color = Steel
                    )
                },
            )

            val icons = listOf(
                R.drawable.bluechar,
                R.drawable.greenchar,
                R.drawable.goldchar,
                R.drawable.pinkchar,
                R.drawable.purplechar,
                R.drawable.redchar
            )

            var selectedIndex by remember {
                mutableStateOf(-1)
            }
            val itemSize: Dp = (LocalConfiguration.current.screenWidthDp.dp / 3)

            LazyVerticalGrid(
                cells = GridCells.Fixed(3)
            ) {
                itemsIndexed(icons) { index, icon ->
                    AvatarImage(
                        modifier = Modifier
                            .size(itemSize)
                            .selectable(
                                selected = selectedIndex == index,
                                onClick = {
                                    Toast.makeText(context, "Index: $index.", Toast.LENGTH_SHORT).show()
                                    selectedIndex = index
                                    Log.d(TAG, "selectedIndex: $selectedIndex")
                                    gameLobbyViewModel.playerChar.value =
                                        gameLobbyViewModel.assignCharNumber(index)
                                    Log.d(TAG, "playerChar: ${gameLobbyViewModel.playerChar.value}")
                                }
                            ),
                        icon = icon,
                        background = if (selectedIndex == index) Color.Red else Color.Transparent
                    )

                }
            }
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = gameLobbyViewModel.playerNickname.value != "" && gameLobbyViewModel.playerChar.value != 0 && gameLobbyViewModel.roomCode.value != "",
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Navy,
                    contentColor = Color.White,
                    disabledBackgroundColor = Color.LightGray,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    JoinGame.joinGame(
                        roomCode = gameLobbyViewModel.roomCode.value,
                        player = HandleUser.createGamePlayer(avatar = gameLobbyViewModel.playerChar.value,
                            nickname = gameLobbyViewModel.playerNickname.value, isHost = false),
                        context = context
                    ) {
                        navController.navigate(Screen.GameLobby.route)
                    }
                }) {
                Text(stringResource(R.string.join_game))
            }
        }
    }
}
