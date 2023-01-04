package com.example.loveletter.presentation.createplayer

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.presentation.createroom.CreateRoomViewModel
import com.example.loveletter.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HostPlayer(navController: NavHostController, createRoomViewModel: CreateRoomViewModel) {
    Surface(
        color = DarkNavy
    ) {
        var playLimit by remember {
            mutableStateOf(-1)
        }
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .border(5.dp, Navy, RectangleShape)
                .padding(24.dp),

            verticalArrangement = Arrangement.SpaceEvenly) {

            Column {
                Text(
                    text = "Room Name",
                    style = MaterialTheme.typography.h5,
                    color = WarmRed,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp)),
                    value = createRoomViewModel.roomNickname.value,
                    onValueChange = { newValue ->
                        createRoomViewModel.roomNickname.value = newValue
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Navy,
                        textColor = SoftYellow,
                        cursorColor = SoftYellow,
                        focusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = SoftYellow
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
            }
            Column {
                Text(
                    text = "Your Name",
                    style = MaterialTheme.typography.h5,
                    color = WarmRed,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                TextField(
                    modifier = Modifier

                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    value = createRoomViewModel.playerNickname.value,
                    onValueChange = { newValue ->
                        createRoomViewModel.playerNickname.value = newValue
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Navy,
                        textColor = SoftYellow,
                        cursorColor = SoftYellow,
                        focusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = SoftYellow
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
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Play Limit",
                    style = MaterialTheme.typography.h5,
                    color = WarmRed,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(98) { number ->
                        val displayNumber = number + 1
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .width(60.dp)
                                .background(if (number == playLimit) SoftYellow else Navy)
                                .clickable {
                                    playLimit = number
                                    createRoomViewModel.playLimit.value = displayNumber
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text= "$displayNumber",
                                style = MaterialTheme.typography.h6,
                                color = if (number == playLimit) Navy else SoftYellow,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

            }

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
                                    selectedIndex = index
                                    createRoomViewModel.playerChar.value =
                                        createRoomViewModel.assignCharNumber(index)
                                }
                            ),
                        icon = icon,
                        background = if (selectedIndex == index) WarmRed else Color.Transparent
                    )
                }
            }

            Button(
                enabled = createRoomViewModel.playerNickname.value != "" && createRoomViewModel.playerChar.value != 0 && createRoomViewModel.roomNickname.value != "" && playLimit != -1,
                modifier = Modifier
                    .fillMaxWidth()
                ,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = WarmRed,
                    contentColor = SoftYellow,
                    disabledBackgroundColor = ColdRed,
                    disabledContentColor = WarmRed
                ),
                onClick = {
                    navController.navigate(Screen.CreateRoom.route)
                }) {
                Text(stringResource(R.string.create_room),
                modifier = Modifier.padding(vertical = 6.dp))
            }
        }
    }
}
