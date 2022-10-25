package com.example.loveletter.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val context = LocalContext.current
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(vertical = 48.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(onClick = { navHostController.navigate(Screen.HostPlayer.route) }) {
                Text(stringResource(R.string.new_game))
            }
            OutlinedButton(onClick = { navHostController.navigate(Screen.JoinGame.route) }) {
                Text(stringResource(R.string.join_game))
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.my_games))
            }
            OutlinedButton(onClick = {
                Toast.makeText(context,
                    "Current user: ${currentUser.uid}",
                    Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(R.string.test_name))
            }
            OutlinedButton(onClick = {
                currentUser.delete()
                auth.signOut()
                Toast.makeText(context,
                    "Signed out!",
                    Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(R.string.sign_out_delete))
            }
        }
    }
}