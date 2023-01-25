package com.example.loveletter.presentation.rules

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.domain.rules

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
        }
    }
}
