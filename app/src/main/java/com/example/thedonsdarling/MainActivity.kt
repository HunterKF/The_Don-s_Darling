package com.example.thedonsdarling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.thedonsdarling.domain.preferences.Preferences
import com.example.thedonsdarling.presentation.SplashViewModel
import com.example.thedonsdarling.presentation.createroom.CreateRoomViewModel
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.joingame.GameLobbyViewModel
import com.example.thedonsdarling.presentation.mygames.MyGamesViewModel
import com.example.thedonsdarling.ui.theme.TheDonsDarlingTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
/*
    @Inject
    lateinit var splashViewModel: SplashViewModel*/

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }*/
        val gameViewModel by viewModels<GameViewModel>()
        val createRoomViewModel by viewModels<CreateRoomViewModel>()
        val gameLobbyViewModel by viewModels<GameLobbyViewModel>()
        val myGamesViewModel by viewModels<MyGamesViewModel>()

        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()


//        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            TheDonsDarlingTheme {
                val navController = rememberNavController()
                val screen = if (shouldShowOnboarding) Screen.Welcome.route else Screen.Home.route
                Log.d("MainActivity", "Value of shouldShow: $shouldShowOnboarding")

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
