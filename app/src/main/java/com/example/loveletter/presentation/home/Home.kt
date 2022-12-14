package com.example.loveletter.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loveletter.R
import com.example.loveletter.Screen
import com.example.loveletter.TAG
import com.example.loveletter.util.user.HandleUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        if (currentUser != null) {
            HandleUser.createUserPlayer()
        }
    })
    val array = arrayListOf(1, 1, 1, 2, 2, 4, 5)

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
            OutlinedButton(onClick = { navHostController.navigate(Screen.MyGames.route) }) {
                Text(stringResource(R.string.my_games))
            }
            OutlinedButton(onClick = {
                Toast.makeText(context,
                    "Current user: ${currentUser.uid}",
                    Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(R.string.test_name))
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                array.forEach {
                    Text(
                        it.toString()
                    )
                }
            }
            fun removeTarget(array: ArrayList<Int>, target: Int): ArrayList<Int> {
                val index = array.indexOf(target)
                array.removeAt(index)
                return array
            }
            Button(onClick = {
                val newArray = removeTarget(array = array, target = 2)
                newArray.forEach {
                    Log.d(TAG, "Remaining item: $it")
                }
            }) {
                Text(
                    "Remove"
                )
            }
        }
    }
}