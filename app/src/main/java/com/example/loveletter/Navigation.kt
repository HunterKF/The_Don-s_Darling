package com.example.loveletter

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.loveletter.presentation.createroom.CreateRoom
import com.example.loveletter.presentation.HomeScreen
import com.example.loveletter.presentation.createplayer.HostPlayer
import com.example.loveletter.presentation.createroom.CreateRoomViewModel
import com.example.loveletter.presentation.createroom.GameLobby
import com.example.loveletter.presentation.game.Game
import com.example.loveletter.presentation.game.GameViewModel
import com.example.loveletter.presentation.joingame.GameLobbyViewModel
import com.example.loveletter.presentation.joingame.JoinGameScreen
import com.example.loveletter.presentation.mygames.MyGames
import com.example.loveletter.presentation.mygames.MyGamesViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    createRoomViewModel: CreateRoomViewModel,
    gameLobbyViewModel: GameLobbyViewModel,
    myGamesViewModel: MyGamesViewModel
) {

    NavHost(navController = navController, startDestination = Screen.Game.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
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
        composable(Screen.MyGames.route) {
            MyGames(navController, myGamesViewModel, gameViewModel)
        }
        composable(Screen.Game.route) {
            Game(navController, gameViewModel)
        }
    }
}