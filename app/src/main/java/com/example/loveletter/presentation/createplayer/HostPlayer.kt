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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.presentation.createroom.CreateRoomViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HostPlayer(navController: NavHostController, createRoomViewModel: CreateRoomViewModel) {
    Surface() {
        Column(Modifier.fillMaxSize()) {

            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                value = createRoomViewModel.roomNickname.value,
                onValueChange = { newValue ->
                    createRoomViewModel.roomNickname.value = newValue
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                singleLine = true,
                label = {
                    Text(stringResource(R.string.enter_room_nickname))
                }
            )
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                value = createRoomViewModel.playerNickname.value,
                onValueChange = { newValue ->
                    createRoomViewModel.playerNickname.value = newValue
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                singleLine = true,
                label = {
                    Text(stringResource(R.string.enter_player_nickname))
                }
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
            LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 100.dp)) {
                itemsIndexed(icons) { index, icon ->
                    println(icon)
                    AvatarImage(
                        modifier = Modifier.selectable(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                println("selectedIndex: $selectedIndex")
                                createRoomViewModel.playerChar.value = index
                                Log.d(TAG, "playerChar: ${createRoomViewModel.playerChar.value}")
                            }
                        ),
                        icon = icon,
                        background = if (selectedIndex == index) Color.Red else Color.Transparent
                    )
                }
            }
            OutlinedButton(onClick = { navController.navigate(Screen.CreateRoom.route)}) {
                Text(stringResource(R.string.create_room))
            }
        }
    }
}
