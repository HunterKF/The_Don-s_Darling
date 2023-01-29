package com.example.thedonsdarling.presentation.rules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.CardAvatar
import com.example.thedonsdarling.domain.cardDefaults
import com.example.thedonsdarling.domain.rules

@Composable
fun RulesScreen(navController: NavHostController) {
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),

                    ) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            null
                        )
                    }
                    Text(
                        style = MaterialTheme.typography.h2,
                        text = stringResource(R.string.rules_rule_title),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            items(rules) { rule ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(
                        text = stringResource(id = rule.title),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    rule.description.forEach {
                        Text(
                            text = stringResource(id = it),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary

                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            itemsIndexed(cardDefaults) { index, card ->
                Column(
                    modifier =  Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CardRule(card = card, modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f))
                    if (index != cardDefaults.size) {
                        Divider(modifier = Modifier.fillMaxWidth(1f),
                            color = MaterialTheme.colors.onPrimary,
                            thickness = 4.dp)
                    }
                }

            }
        }
    }
}

@Composable
private fun CardRule(
    card: CardAvatar,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(
            text = stringResource(id = card.cardName),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center

        )
        Image(
            modifier = Modifier.fillMaxWidth(0.5f),
            painter = painterResource(id = card.avatar),
            contentDescription = stringResource(id = card.cardName),
            contentScale = ContentScale.FillWidth
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = card.ruleDescription),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Left
        )
    }
}
