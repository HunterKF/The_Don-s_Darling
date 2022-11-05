package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.loveletter.domain.CardAvatar

@Composable
fun MenuItem(cardAvatar: CardAvatar, modifier: Modifier) {
    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${cardAvatar.number} - ${cardAvatar.cardName}",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold)
            Text("in game: x${cardAvatar.numberInGame}",
                style = MaterialTheme.typography.subtitle1)
        }
        Text(
            text = stringResource(id = cardAvatar.ruleShortDescription),
            style = MaterialTheme.typography.body1
        )

    }
}