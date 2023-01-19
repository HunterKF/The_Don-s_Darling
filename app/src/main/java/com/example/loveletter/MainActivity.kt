package com.example.loveletter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.loveletter.presentation.SplashViewModel
import com.example.loveletter.presentation.createroom.CreateRoomViewModel
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.joingame.GameLobbyViewModel
import com.example.loveletter.presentation.mygames.MyGamesViewModel
import com.example.loveletter.ui.theme.LoveLetterTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }
        val gameViewModel by viewModels<GameViewModel>()
        val createRoomViewModel by viewModels<CreateRoomViewModel>()
        val gameLobbyViewModel by viewModels<GameLobbyViewModel>()
        val myGamesViewModel by viewModels<MyGamesViewModel>()

//        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val auth = Firebase.auth
            val currentUser = auth.currentUser
            if (currentUser == null) {
                auth.signInAnonymously()
                println("Something happened...")
            }
            LoveLetterTheme {
                val navController = rememberNavController()
                val screen by splashViewModel.startDestination

                Navigation(
                    navController = navController,
                    gameViewModel = gameViewModel,
                    createRoomViewModel = createRoomViewModel,
                    gameLobbyViewModel = gameLobbyViewModel,
                    myGamesViewModel = myGamesViewModel,
                    startDestination = screen
                )
            }
        }
    }
}
