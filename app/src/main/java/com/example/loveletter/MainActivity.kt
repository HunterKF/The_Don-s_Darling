package com.example.loveletter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.loveletter.ui.theme.LoveLetterTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val auth = Firebase.auth
            val currentUser = auth.currentUser
            if (currentUser == null) {
                auth.signInAnonymously()
                println("Something happened...")
            }
            LoveLetterTheme {
                val navController = rememberNavController()

                Navigation(
                    navController = navController
                )
            }
        }
    }
}
