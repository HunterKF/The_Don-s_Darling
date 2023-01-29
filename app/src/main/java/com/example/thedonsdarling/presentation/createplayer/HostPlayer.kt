package com.example.thedonsdarling.presentation.createplayer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.domain.Avatar
import com.example.thedonsdarling.presentation.createroom.CreateRoomViewModel
import com.example.thedonsdarling.presentation.util.CustomTextButton
import com.example.thedonsdarling.ui.theme.*

@Composable
fun HostPlayer(navController: NavHostController, createRoomViewModel: CreateRoomViewModel) {
    LaunchedEffect(key1 = Unit) {
        createRoomViewModel.clearRoomData()
    }
    val focusManager = LocalFocusManager.current
    Surface(
        color = Black
    ) {
        var playLimit by remember {
            mutableStateOf(-1)
        }
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(18.dp)
                .border(5.dp, MaterialTheme.colors.onPrimary, RectangleShape)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),

            verticalArrangement = Arrangement.SpaceEvenly) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpToId
                    }
                }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        null,
                    )
                }

                Text(
                    text = stringResource(R.string.host_game),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.room_name),
                    style = MaterialTheme.typography.h5,
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
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(focusDirection = FocusDirection.Down)
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                    ),
                    placeholder = {
                        Text(
                            stringResource(id = R.string.enter_room_code),

                            fontSize = 16.sp,
                        )
                    },
                    singleLine = true,
                )
            }
            Spacer(Modifier.height(4.dp))

            Column {
                Text(
                    text = stringResource(id = R.string.enter_nickname),
                    style = MaterialTheme.typography.h5,
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
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                    ),
                    placeholder = {
                        Text(
                            stringResource(id = R.string.enter_player_nickname),
                            fontSize = 16.sp,
                        )
                    },
                    singleLine = true,
                )
            }
            Spacer(Modifier.height(4.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.play_limit),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                val playLimits = listOf(
                    5,
                    7,
                    9
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    playLimits.forEach { number ->
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .width(60.dp)
                                .background(if (number == playLimit) WarmRed else Color.White)
                                .clickable {
                                    playLimit = number
                                    createRoomViewModel.playLimit.value = number
                                    focusManager.clearFocus()

                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$number",
                                style = MaterialTheme.typography.h6,
                                color = if (number == playLimit) Color.White else Black,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

            }

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
                    var avatar = Avatar.setAvatar(icon)

                    AvatarImage(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedIndex == icon,
                                onClick = {
                                    focusManager.clearFocus()
                                    selectedIndex = icon
                                    createRoomViewModel.playerChar.value =
                                        createRoomViewModel.assignCharNumber(icon)
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
                    var avatar = Avatar.setAvatar(icon)

                    AvatarImage(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedIndex == icon,
                                onClick = {
                                    focusManager.clearFocus()
                                    selectedIndex = icon
                                    createRoomViewModel.playerChar.value =
                                        createRoomViewModel.assignCharNumber(icon)
                                }
                            ),
                        icon = avatar.avatar,
                        background = if (selectedIndex == icon) WarmRed else Color.Transparent
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            CustomTextButton(
                enabled = createRoomViewModel.playerNickname.value != "" && createRoomViewModel.playerChar.value != 0 && createRoomViewModel.roomNickname.value != "" && playLimit != -1,
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.create_room),
                onClick = {
                    navController.navigate(Screen.CreateRoom.route)
                })
        }
    }
}
