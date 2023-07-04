package com.example.thedonsdarling.presentation.game

import app.cash.turbine.test
import com.example.thedonsdarling.data.preferences.FakeDefaultPreferences
import com.example.thedonsdarling.data.repository.FakeFireStoreRepositoryImpl
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.preferences.Preferences
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.example.thedonsdarling.domain.use_cases.*
import com.example.thedonsdarling.util.UiEvent
import com.example.thedonsdarling.util.game.gamerules.testPlayer1
import com.example.thedonsdarling.util.game.gamerules.testPlayer2
import com.example.thedonsdarling.util.game.gamerules.testPlayer3
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import kotlin.test.assertEquals

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

        val card1 = 1
        val card2 = 5

        val gameRoom = GameRoom().copy(
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

        val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
        player1.hand = arrayListOf(card1, card2)
        val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }
        player2.hand = arrayListOf(5)
        val viewModel = GameViewModel(
            useCases,
            defaultPreferences
        )

        println("BEFORE viewModel.state.value")
        println(viewModel.state.value)
        viewModel.onUiEvent(
            UiEvent.OnPlay(
                card = card1,
                player1,
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
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .doesNotContain(card1)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .contains(card2)
            }
        }
        println(viewModel.state.value)
    }

    @Test
    fun `Test play of Card 2`() = runBlocking {

        val card1 = 2
        val card2 = 5

        val initialRoom = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    turn = true,
                    turnOrder = 1,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = true
                ),
                testPlayer2.copy(
                    turn = false,
                    turnOrder = 2,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = false
                )
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

        val player1 = initialRoom.players.first { it.uid == testPlayer1.uid }
        player1.hand = arrayListOf(card1, card2)
        val player2 = initialRoom.players.first { it.uid == testPlayer2.uid }
        player2.hand = arrayListOf(5)
        val viewModel = GameViewModel(
            useCases,
            defaultPreferences
        )
        launch(Dispatchers.Main) {
            viewModel.onUiEvent(UiEvent.ObserveRoom)
        }

        println("BEFORE viewModel.state.value")
        println(viewModel.state.value)
        viewModel.onUiEvent(
            UiEvent.OnPlay(
                card = card1,
                player1,
                gameRoom = initialRoom
            )
        )
        viewModel.state.test {
            assertEquals(
                GameState.Loaded(
                    initialRoom
                ), awaitItem()
            )
        }
        launch(Dispatchers.Main) {
            viewModel.onUiEvent(UiEvent.ObserveRoom)
        }
        viewModel.state.test {
            assertEquals(
                GameState.Loaded(
                    initialRoom
                ), awaitItem()
            )
        }
        println("AFTER viewModel.state.value")
        viewModel.state.test {
            assertEquals(
                GameState.Loaded(
                    initialRoom
                ), awaitItem()
            )
        }
        when (viewModel.state.value) {
            is GameState.Loaded -> {
                val game = (viewModel.state.value as GameState.Loaded).gameRoom
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .doesNotContain(card1)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .contains(card2)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand.size)
                    .isEqualTo(1)
            }
        }
        viewModel.onUiEvent(
            UiEvent.OnSelectPlayer(
                selectedPlayer = player2,
                gameRoom = initialRoom
            )
        )
        Truth.assertThat(viewModel.revealCardAlert.value).isTrue()
        launch(Dispatchers.Main) {
            viewModel.onUiEvent(UiEvent.ObserveRoom)
        }
//        delay(1000)

        val game = (viewModel.state.value as GameState.Loaded).gameRoom
        println(game)
        Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.turn).isFalse()
        Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.turn).isTrue()
        Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.hand.size).isEqualTo(2)
    }

    @Test
    fun `Test play of Card 3, P1 Wins, P2 eliminated, P3's turn`() = runBlocking {

        val card1 = 3
        val card2 = 8

        val gameRoom = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card1, card2),
                    turn = true,
                    turnOrder = 1,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = true
                ),
                testPlayer2.copy(
                    turn = false,
                    hand = arrayListOf(5),
                    turnOrder = 2,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = false
                ),
                testPlayer3.copy(
                    hand = arrayListOf(4),
                    turn = false,
                    turnOrder = 3,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = false
                )
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

        val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
        player1.hand = arrayListOf(card1, card2)
        val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }
        player2.hand = arrayListOf(5)
        val player3 = gameRoom.players.first { it.uid == testPlayer2.uid }
        player3.hand = arrayListOf(4)

        val viewModel = GameViewModel(
            useCases,
            defaultPreferences
        )
        viewModel.localPlayer.value = player1

        val collectedStates = mutableListOf<GameState>()
        val collectJob = launch {
            viewModel.state.collect { state ->
                collectedStates.add(state)
            }
        }

        viewModel.onUiEvent(
            UiEvent.OnPlay(
                card = card1,
                player1,
                gameRoom = gameRoom
            )
        )

        val currentStateAfterEvent1 = collectedStates.last()
        Truth.assertThat(currentStateAfterEvent1).isInstanceOf(GameState.Loaded::class.java)
        Truth.assertThat(viewModel.selectPlayerAlert.value).isTrue()
        when (viewModel.state.value) {
            is GameState.Loaded -> {
                val game = (viewModel.state.value as GameState.Loaded).gameRoom
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .doesNotContain(card1)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .contains(card2)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand.size)
                    .isEqualTo(1)
            }
        }
        viewModel.onUiEvent(
            UiEvent.OnSelectPlayer(
                selectedPlayer = player2,
                gameRoom = gameRoom
            )
        )
        launch(Dispatchers.Main) {
            viewModel.onUiEvent(UiEvent.ObserveRoom)
        }
        when (viewModel.state.value) {
            is GameState.Loaded -> {
                val game = (viewModel.state.value as GameState.Loaded).gameRoom
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.turn).isFalse()
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand.size)
                    .isEqualTo(1)
                Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.turn).isFalse()
                Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.isAlive).isFalse()
                Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.hand.size)
                    .isEqualTo(0)
                Truth.assertThat(game.players.first { it.uid == testPlayer3.uid }.turn).isTrue()
                Truth.assertThat(game.players.first { it.uid == testPlayer3.uid }.hand.size)
                    .isEqualTo(2)

                Truth.assertThat(game.turn).isEqualTo(3)
                Truth.assertThat(game.deck.discardDeck).isEqualTo(1)
            }
        }

    }

    @Test
    fun `Test play of Card 3, P1 Plays, P2 Wins, P1 eliminated, P3's turn`() = runBlocking {

        val card1 = 3
        val card2 = 8

        val gameRoom = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card1, 2),
                    turn = true,
                    turnOrder = 1,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = true
                ),
                testPlayer2.copy(
                    turn = false,
                    hand = arrayListOf(card2),
                    turnOrder = 2,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = false
                ),
                testPlayer3.copy(
                    hand = arrayListOf(4),
                    turn = false,
                    turnOrder = 3,
                    ready = true,
                    protected = false,
                    isAlive = true,
                    isHost = false
                )
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

        val player1 = gameRoom.players.first { it.uid == testPlayer1.uid }
        val player2 = gameRoom.players.first { it.uid == testPlayer2.uid }
        val player3 = gameRoom.players.first { it.uid == testPlayer2.uid }
        val viewModel = GameViewModel(
            useCases,
            defaultPreferences
        )
        viewModel.localPlayer.value = player1
        println("BEFORE viewModel.state.value")
        println(viewModel.state.value)
        viewModel.onUiEvent(
            UiEvent.OnPlay(
                card = card1,
                player1,
                gameRoom = gameRoom
            )
        )
        Truth.assertThat(viewModel.selectPlayerAlert.value).isTrue()
        println("AFTER viewModel.state.value")
        when (viewModel.state.value) {
            is GameState.Loaded -> {
                val game = (viewModel.state.value as GameState.Loaded).gameRoom
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .doesNotContain(card1)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand)
                    .contains(card2)
                Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand.size)
                    .isEqualTo(1)
            }
        }
        viewModel.onUiEvent(
            UiEvent.OnSelectPlayer(
                selectedPlayer = player2,
                gameRoom = gameRoom
            )
        )
        launch(Dispatchers.Main) {
            viewModel.onUiEvent(UiEvent.ObserveRoom)
        }
        delay(100)
        val game = (viewModel.state.value as GameState.Loaded).gameRoom
        println(game)
        Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.isAlive).isFalse()
        Truth.assertThat(game.players.first { it.uid == testPlayer1.uid }.hand.size).isEqualTo(0)
        Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.turn).isTrue()
        Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.isAlive).isTrue()
        Truth.assertThat(game.players.first { it.uid == testPlayer2.uid }.hand.size).isEqualTo(2)
        Truth.assertThat(game.players.first { it.uid == testPlayer3.uid }.turn).isFalse()
        Truth.assertThat(game.players.first { it.uid == testPlayer3.uid }.hand.size).isEqualTo(1)
        Truth.assertThat(game.turn).isEqualTo(2)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}