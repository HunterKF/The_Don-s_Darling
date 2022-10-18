package com.example.loveletter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val vector: ImageVector) {
    object Home : Screen("home_screen", "Home", Icons.Rounded.PlayArrow)
    object CreateRoom : Screen("create_room", "Create Room", Icons.Rounded.Create)
}
