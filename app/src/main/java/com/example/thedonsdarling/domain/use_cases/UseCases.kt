package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.models.CheckGameResult
import com.example.thedonsdarling.util.UiEvent

data class UseCases(
    val addGameToMultiplePlayers: AddGameToMultiplePlayers,
    val addGameToPlayer: AddGameToPlayer,
    val createRoom: CreateRoom,
    val createUserPlayer: CreateUserPlayer,
    val deleteRoomFromFirestore: DeleteRoomFromFirestore,
    val deleteUserGameRoomForAll: DeleteUserGameRoomForAll,
    val deleteUserGameRoomForLocal: DeleteUserGameRoomForLocal,
    val observeMyGames: ObserveMyGames,
    val removePlayerFromGame: RemovePlayerFromGame,
    val removeSingleGameFromAllPlayersJoinedGames: RemoveSingleGameFromAllPlayersJoinedGames,
    val removeSingleGameFromPlayerJoinedList: RemoveSingleGameFromPlayerJoinedList,
    val setGameInDB: SetGameInDB,
    val startGame: StartGame,
    val subscribeToRealtimeUpdates: SubscribeToRealtimeUpdates,
    val checkGame: CheckGame,
    val joinGame: JoinGame,
    val startNewRound: StartNewRound,
    val startNewGame: StartNewGame,
    val updateUnreadStatusForLocal: UpdateUnreadStatusForLocal,
    val updateUnreadStatusForAll: UpdateUnreadStatusForAll,
    val sendMessage: SendMessage
)