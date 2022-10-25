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
import com.example.loveletter.presentation.joingame.GameLobbyViewModel
import com.example.loveletter.presentation.joingame.JoinGameScreen

@Composable
fun Navigation(navController: NavHostController) {
    val createRoomViewModel = CreateRoomViewModel()
    val gameLobbyViewModel = GameLobbyViewModel()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.CreateRoom.route) {
            CreateRoom(navController, createRoomViewModel)
        }
        composable(Screen.HostPlayer.route) {
            HostPlayer(navController, createRoomViewModel)
        }
        composable(Screen.JoinGame.route) {
            JoinGameScreen(navController, gameLobbyViewModel)
        }
        composable(Screen.GameLobby.route) {
            GameLobby(navController, gameLobbyViewModel)
        }
    }
}