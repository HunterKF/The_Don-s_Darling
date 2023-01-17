package com.example.loveletter.presentation.mygames

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.domain.FirestoreUser
import com.example.loveletter.domain.JoinedGame
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.ui.theme.Navy


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JoinedGameCard(game: JoinedGame, color: Color = Navy, modifier: Modifier = Modifier,onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = game.roomNickname,
                color = MaterialTheme.colors.primary
            )
        }
    }
}