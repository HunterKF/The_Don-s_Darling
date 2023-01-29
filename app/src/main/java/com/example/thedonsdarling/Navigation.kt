package com.example.thedonsdarling

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thedonsdarling.presentation.createroom.CreateRoom
import com.example.thedonsdarling.presentation.HomeScreen
import com.example.thedonsdarling.presentation.createplayer.HostPlayer
import com.example.thedonsdarling.presentation.createroom.CreateRoomViewModel
import com.example.thedonsdarling.presentation.createroom.GameLobby
import com.example.thedonsdarling.presentation.game.Game
import com.example.thedonsdarling.presentation.game.GameViewModel
import com.example.thedonsdarling.presentation.joingame.GameLobbyViewModel
import com.example.thedonsdarling.presentation.joingame.JoinGameScreen
import com.example.thedonsdarling.presentation.mygames.MyGames
import com.example.thedonsdarling.presentation.mygames.MyGamesViewModel
import com.example.thedonsdarling.presentation.rules.RulesScreen
import com.example.thedonsdarling.presentation.welcome.WelcomeScreen

@Composable
fun Navigation(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    createRoomViewModel: CreateRoomViewModel,
    gameLobbyViewModel: GameLobbyViewModel,
    myGamesViewModel: MyGamesViewModel,
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, gameViewModel)
        }
        composable(Screen.CreateRoom.route) {
            CreateRoom(navController, createRoomViewModel, gameViewModel)
        }
        composable(Screen.HostPlayer.route) {
            HostPlayer(navController, createRoomViewModel)
        }
        composable(Screen.JoinGame.route) {
            JoinGameScreen(navController, gameLobbyViewModel)
        }
        composable(Screen.GameLobby.route) {
            GameLobby(navController, gameLobbyViewModel, gameViewModel)
        }
        composable(Screen.Game.route) {
            Game(navController, gameViewModel)
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screen.MyGames.route) {
            MyGames(myGamesViewModel = myGamesViewModel, navHostController = navController, gameViewModel = gameViewModel)
        }
        composable(Screen.Rules.route) {
            RulesScreen(navController)
        }
    }
}