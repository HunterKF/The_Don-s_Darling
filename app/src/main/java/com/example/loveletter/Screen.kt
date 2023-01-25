package com.example.loveletter

sealed class Screen(val route: String, val label: String, val vector: Int?) {
    object Home : Screen("home_screen", "Home", R.drawable.icon_home)
    object Welcome : Screen("welcome_screen", "Welcome", null)
    object CreateRoom : Screen("create_room", "Create Room", null)
    object HostPlayer : Screen("host_player", "Host Player", null)
    object JoinGame : Screen("join_game", "Join Game", null)
    object GameLobby : Screen("game_lobby", "Game Lobby", null)
    object Game : Screen("game", "Game", null)
    object MyGames : Screen("my_games", "My Games", R.drawable.icon_my_games)
    object Rules : Screen("rules", "Rules", null)
}
val items = listOf(
    Screen.Home,
    Screen.MyGames
)