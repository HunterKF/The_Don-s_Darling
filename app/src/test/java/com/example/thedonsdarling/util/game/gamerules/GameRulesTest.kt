package com.example.thedonsdarling.util.game.gamerules

import com.example.thedonsdarling.domain.models.Deck
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.util.game.gamerules.GameRules
import com.google.common.truth.Truth

import org.junit.Before
import org.junit.Test

class GameRulesTest {

    private lateinit var gameRoom2Players: GameRoom
    private lateinit var gameRoom3Players: GameRoom
    private lateinit var gameRoom4Players: GameRoom
    private lateinit var localPlayer: Player


    @Before
    fun setUp() {
        /*gameRoom2Players = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2,
            )
        )*/
        gameRoom3Players = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2,
                testPlayer3,
            )
        )
        gameRoom4Players = testGameRoom.copy(
            players = listOf(
                testPlayer1,
                testPlayer2,
                testPlayer3,
                testPlayer4
            )
        )
        localPlayer = testPlayer1
    }

    private fun getPlayer1(gameRoom: GameRoom): Player {
        return gameRoom.players.first { it.uid == testPlayer1.uid }
    }

    private fun getPlayer2(gameRoom: GameRoom): Player {
        return gameRoom.players.first { it.uid == testPlayer2.uid }
    }

    @Test
    fun `Launch on Turn, 2 players, Player 1 is not protected`() {

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
        var player1 = getPlayer1(gameRoom2Players)
        var player2 = getPlayer2(gameRoom2Players)
        player1.turn = true
        player1.turnInProgress = false
        player1.hand = arrayListOf(1)
        player2.hand = arrayListOf(2)
        val result = GameRules.launchOnTurn(gameRoom2Players)
        player1 = result.players.first { it.uid == testPlayer1.uid }

        Truth.assertThat(player1.hand).isNotEmpty()
        Truth.assertThat(player1.hand.size).isEqualTo(2)
        Truth.assertThat(player1.turnInProgress).isTrue()
        Truth.assertThat(player1.protected).isFalse()
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)
    }

    @Test
    fun `Launch on Turn, 2 players, Player 1 is protected, toggle protection off`() {
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


        var player1 = getPlayer1(gameRoom2Players)
        var player2 = getPlayer2(gameRoom2Players)
        player1.turn = true
        player1.turnInProgress = false
        player1.hand = arrayListOf(1)
        player1.protected = true
        player2.hand = arrayListOf(2)
        println(gameRoom2Players.deck.deck.size)

        val result = GameRules.launchOnTurn(gameRoom2Players)
        player1 = result.players.first { it.uid == testPlayer1.uid }

        println(result.deck.deck.size)

        Truth.assertThat(player1.hand).isNotEmpty()
        Truth.assertThat(player1.hand.size).isEqualTo(2)
        Truth.assertThat(player1.protected).isFalse()
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)
    }

    @Test
    fun `onEnd, 2 Players, P1 isAlive, P2 !isAlive, return no change`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true
                ),
                testPlayer2.copy(
                    hand = arrayListOf(8),
                    isAlive = false

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
        val logMessage = LogMessage.createLogMessage(
            message = "test1",
            toastMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.players).isEqualTo(gameRoom2Players.players)
        Truth.assertThat(result.deck.deck.size).isEqualTo(16)

        Truth.assertThat(result.turn).isEqualTo(gameRoom2Players.turn)
    }

    @Test
    fun `onEnd, 2 Players, P1 isAlive, P2 isAlive, return P2's turn`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                ),
                testPlayer2.copy(
                    hand = arrayListOf(8),
                    isAlive = true

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
        val logMessage = LogMessage.createLogMessage(
            message = "test1",
            toastMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(2)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)

        Truth.assertThat(result.players[1].turn).isTrue()
        Truth.assertThat(result.players[1].hand.size).isEqualTo(2)
    }

    @Test
    fun `onEnd, 3 Players, P1 isAlive, P2 isAlive, P3 !isAlive, return P1's turn`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 2,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(8),
                    isAlive = true,
                    turn = true

                ),
                testPlayer3.copy(
                    hand = arrayListOf(8),
                    isAlive = false,
                    turn = false
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
        val logMessage = LogMessage.createLogMessage(
            message = "test1",
            toastMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(1)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)

        Truth.assertThat(result.players[0].turn).isTrue()
        Truth.assertThat(result.players[1].turn).isFalse()
        Truth.assertThat(result.players[2].turn).isFalse()
        Truth.assertThat(result.players[0].hand.size).isEqualTo(2)
    }

    @Test
    fun `onEnd, 4 Players, P1 isAlive, P2 isAlive, P3 !isAlive, P4 isAlive return P4's turn`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 2,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = true

                ),
                testPlayer3.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = false
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
        val logMessage = LogMessage.createLogMessage(
            message = "test1",
            toastMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(4)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)

        Truth.assertThat(result.players[0].turn).isFalse()
        Truth.assertThat(result.players[1].turn).isFalse()
        Truth.assertThat(result.players[2].turn).isFalse()
        Truth.assertThat(result.players[3].turn).isTrue()
        Truth.assertThat(result.players[3].hand.size).isEqualTo(2)
    }

    @Test
    fun `onEnd, 4 Players, P1 !isAlive, P2 !isAlive, P3 isAlive, P4 isAlive return P3's turn`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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
        val logMessage = LogMessage.createLogMessage(
            message = "test1",
            toastMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(3)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)
        Truth.assertThat(result.players[0].turn).isFalse()
        Truth.assertThat(result.players[1].turn).isFalse()
        Truth.assertThat(result.players[2].turn).isTrue()
        Truth.assertThat(result.players[2].turnInProgress).isTrue()
        Truth.assertThat(result.players[3].turn).isFalse()
        Truth.assertThat(result.players[2].hand.size).isEqualTo(2)
    }

    @Test
    fun `onEnd, 4 Players, P1 isAlive, P2 isAlive, P3 isAlive, P4 isAlive, deck empty, return P3's turn`() {
        val testDeck = Deck()
        testDeck.deck.clear()
        val gameRoom2Players = GameRoom().copy(
            deck = testDeck,
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(1),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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
        val logMessage = LogMessage.createLogMessage(
            message = "test1",
            toastMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(1)
        Truth.assertThat(result.players[0].turn).isTrue()
        Truth.assertThat(result.players[1].turn).isFalse()
        Truth.assertThat(result.players[2].turn).isFalse()
        Truth.assertThat(result.players[3].turn).isFalse()
        Truth.assertThat(result.players[0].hand.size).isEqualTo(1)
    }

    @Test
    fun `handlePlayedCard, return updated deck`() {
        val card = 2
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card, 3),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(1),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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


        val result =
            GameRules.handlePlayedCard(card, player = gameRoom2Players.players[0], gameRoom2Players)

        Truth.assertThat(result.players[0].hand.size).isEqualTo(1)
        Truth.assertThat(result.deck.discardDeck.size).isEqualTo(1)
    }

    @Test
    fun `addToDiscardPile, return updated deck`() {
        val card = 2
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card, 3),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(1),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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


        val result =
            GameRules.addToDiscardPile(card, gameRoom2Players)

        Truth.assertThat(result.size).isEqualTo(1)
        Truth.assertThat(result).contains(card)
    }

    @Test
    fun `removeFromDeck, return updated deck`() {
        val card = 1
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card, 3),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(1),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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
        val expectedResult = Deck().deck
        expectedResult.removeAt(gameRoom2Players.deck.deck.indexOf(card))


        val result =
            GameRules.removeFromDeck(card, gameRoom2Players)

        Truth.assertThat(result.size).isEqualTo(15)
        Truth.assertThat(result).isEqualTo(expectedResult)

    }

    @Test
    fun `drawCard, return updated deck`() {
        val card = 4
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(1),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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


        val result =
            GameRules.drawCard(gameRoom2Players)

        Truth.assertThat(result).isGreaterThan(0)
        Truth.assertThat(result).isLessThan(9)
    }

    @Test
    fun `endRound, return updated deck`() {
        val card = 4
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(card),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(1),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(4),
                    isAlive = true,
                    turn = true
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


        val result =
            GameRules.drawCard(gameRoom2Players)

        Truth.assertThat(result).isGreaterThan(0)
        Truth.assertThat(result).isLessThan(9)
    }


    /*
    * TODO - eliminate player
    *  TODO - AssignTurns
    *   Todo - deal cards*/
}