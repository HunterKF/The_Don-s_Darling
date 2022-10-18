package com.example.loveletter

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.loveletter.presentation.CreateRoom
import com.example.loveletter.presentation.HomeScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.CreateRoom.route) {
            CreateRoom(navController)
        }
    }
}