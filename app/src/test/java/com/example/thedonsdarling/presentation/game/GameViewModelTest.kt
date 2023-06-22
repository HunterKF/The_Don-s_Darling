package com.example.thedonsdarling.presentation.game

import com.example.thedonsdarling.data.preferences.FakeDefaultPreferences
import com.example.thedonsdarling.data.repository.FakeFireStoreRepositoryImpl
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.preferences.Preferences
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.example.thedonsdarling.domain.use_cases.*
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock

class GameViewModelTest {

    private lateinit var repository: FireStoreRepository
    private lateinit var useCases: UseCases
    private lateinit var defaultPreferences: Preferences
    private lateinit var viewModel: GameViewModel
    private lateinit var userMock: FirebaseUser
    private lateinit var firebaseMock: FirebaseAuth

    @Before
    fun setUp() {
        repository = FakeFireStoreRepositoryImpl()
        defaultPreferences = FakeDefaultPreferences()
        useCases = UseCases(
            addGameToMultiplePlayers = AddGameToMultiplePlayers(repository),
            addGameToPlayer = AddGameToPlayer(repository),
            createBlankRoom = CreateBlankRoom(repository),
            createUserPlayer = CreateUserPlayer(repository),
            deleteRoomFromFirestore = DeleteRoomFromFirestore(repository),
            deleteUserGameRoomForAll = DeleteUserGameRoomForAll(repository),
            deleteUserGameRoomForLocal = DeleteUserGameRoomForLocal(repository),
            observeMyGames = ObserveMyGames(repository),
            removePlayerFromGame = RemovePlayerFromGame(repository),
            removeSingleGameFromAllPlayersJoinedGames = RemoveSingleGameFromAllPlayersJoinedGames(
                repository
            ),
            removeSingleGameFromPlayerJoinedList = RemoveSingleGameFromPlayerJoinedList(repository),
            setGameInDB = SetGameInDB(repository),
            startGame = StartGame(),
            subscribeToRealtimeUpdates = SubscribeToRealtimeUpdates(repository),
            checkGame = CheckGame(repository),
            joinGame = JoinGame(repository),
            startNewRound = StartNewRound(),
            startNewGame = StartNewGame(),
            updateUnreadStatusForLocal = UpdateUnreadStatusForLocal(repository),
            updateUnreadStatusForAll = UpdateUnreadStatusForAll(repository),
            sendMessage = SendMessage(repository),
            getUid = GetUid(repository = repository)
        )

        viewModel = GameViewModel(
            useCases,
            defaultPreferences
        )
        userMock = mock(FirebaseUser::class.java)
        firebaseMock = mock(FirebaseAuth::class.java)
        doReturn(userMock).`when`(firebaseMock).currentUser
        doReturn("123").`when`(userMock).uid
    }


    @Test
    fun `12`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(),
                testPlayer2.copy()
            ),
            start = true,
            host = "apple_uid",
            roundOver = false,
            gameOver = false,
            showLogs = true,
            deleteRoom = false,
            deckClear = false,
            gameLog = arrayListOf()
        )
        /*viewModel.onPlay(
            card = 1,
            gameRoom = gameRoom2Players,
            player = gameRoom2Players.players[0],

        )*/
    }
}