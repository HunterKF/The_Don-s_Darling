package com.example.thedonsdarling.presentation.game

import com.example.thedonsdarling.data.preferences.FakeDefaultPreferences
import com.example.thedonsdarling.data.repository.FakeFireStoreRepositoryImpl
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.preferences.Preferences
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.example.thedonsdarling.domain.use_cases.*
import com.example.thedonsdarling.util.UiEvent
import com.example.thedonsdarling.util.game.gamerules.testGameRoom
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer3
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock

class GameViewModelTest {

    private lateinit var repository: FireStoreRepository
    private lateinit var useCases: UseCases
    private lateinit var defaultPreferences: Preferences
    private lateinit var userMock: FirebaseUser
    private lateinit var firebaseMock: FirebaseAuth
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
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


        userMock = mock(FirebaseUser::class.java)
        firebaseMock = mock(FirebaseAuth::class.java)
        doReturn(userMock).`when`(firebaseMock).currentUser
        doReturn("123").`when`(userMock).uid
    }


    @Test
    fun `Test onPlay, card 1`() = runBlocking {
        val player = Player().copy(
            avatar = 1,
            nickName = "Apple",
            uid = "apple_uid",
            ready = true,
            protected = false,
            turn = true,
            turnInProgress = false,
            turnOrder = 1,
            hand = arrayListOf(4, 5),
            isHost = true,
            isAlive = true,
            isWinner = false,
            wins = 0,
            unread = false,
            guide = true
        )
        val viewModel = GameViewModel(
            useCases,
            defaultPreferences
        )
        val card1 = 1
        val card2 = 5
        val gameRoom = testGameRoom.copy(
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card1,card2)
                ),
                player,
                testPlayer3,
            )
        )
        println("BEFORE viewModel.state.value")
        println(viewModel.state.value)
        viewModel.onUiEvent(
            UiEvent.OnPlay(
                card = card1,
                player,
                gameRoom = gameRoom
            )
        )
        launch(Dispatchers.Main) {
            viewModel.onUiEvent(UiEvent.ObserveRoom)
        }
        Truth.assertThat(viewModel.selectPlayerAlert.value).isTrue()
        println("AFTER viewModel.state.value")
        when (viewModel.state.value) {
            is GameState.Loaded -> {
                val game = (viewModel.state.value as GameState.Loaded).gameRoom
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid}.hand).doesNotContain(card1)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid}.hand).contains(card2)
            }
        }
        println(viewModel.state.value)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}