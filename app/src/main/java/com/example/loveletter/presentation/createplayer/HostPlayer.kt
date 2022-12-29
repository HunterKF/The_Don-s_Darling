package com.example.loveletter.presentation.createplayer

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
import com.example.loveletter.presentation.createroom.CreateRoomViewModel
import com.example.loveletter.ui.theme.DarkNavy
import com.example.loveletter.ui.theme.Navy
import com.example.loveletter.ui.theme.Steel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HostPlayer(navController: NavHostController, createRoomViewModel: CreateRoomViewModel) {
    Surface(
        color = DarkNavy
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceEvenly) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50.dp)),
                value = createRoomViewModel.roomNickname.value,
                onValueChange = { newValue ->
                    createRoomViewModel.roomNickname.value = newValue
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
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
                        "Enter room nickname here...",

                        fontSize = 16.sp,
                        color = Steel
                    )
                },
                singleLine = true,
            )
            TextField(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(50.dp)),
                value = createRoomViewModel.playerNickname.value,
                onValueChange = { newValue ->
                    createRoomViewModel.playerNickname.value = newValue
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
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
                singleLine = true,
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
                    println(icon)
                    AvatarImage(
                        modifier = Modifier
                            .size(itemSize)
                            .selectable(
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                    println("selectedIndex: $selectedIndex")
                                    createRoomViewModel.playerChar.value = createRoomViewModel.assignCharNumber(index)
                                    Log.d(TAG,
                                        "playerChar: ${createRoomViewModel.playerChar.value}")
                                }
                            ),
                        icon = icon,
                        background = if (selectedIndex == index) Color.Red else Color.Transparent
                    )
                }
            }
            Button(
                enabled = createRoomViewModel.playerNickname.value != "" && createRoomViewModel.playerChar.value != 0 && createRoomViewModel.roomNickname.value != "",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Navy,
                    contentColor = Color.White,
                    disabledBackgroundColor = Color.LightGray,
                    disabledContentColor = Color.Black
                ),
                onClick = { navController.navigate(Screen.CreateRoom.route) }) {
                Text(stringResource(R.string.create_room))
            }
        }
    }
}
