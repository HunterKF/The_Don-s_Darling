package com.example.thedonsdarling.util.game.gamerules

import com.example.thedonsdarling.domain.models.*
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
                    isAlive = true,
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
            chatMessage = "",
            gameMessage = null,
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
            turn = 0,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    turn = true,
                    isAlive = true,
                    turnOrder = 0

                ),
                testPlayer2.copy(
                    hand = arrayListOf(8),
                    turn = false,
                    isAlive = true,
                    turnOrder = 1

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
            chatMessage = "",
            gameMessage = null,
            type = "game rule",
            uid = null
        )
        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(1)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)

        Truth.assertThat(result.players[1].turn).isTrue()
        Truth.assertThat(result.players[1].hand.size).isEqualTo(2)

        Truth.assertThat(result.players[0].turn).isFalse()
        Truth.assertThat(result.players[0].hand.size).isEqualTo(1)
    }

    @Test
    fun `onEnd, 3 Players, P1 isAlive, P2 isAlive, P3 !isAlive, return P1's turn`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 1,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(2),
                    isAlive = true,
                    turn = false,
                ),
                testPlayer2.copy(
                    hand = arrayListOf(8),
                    isAlive = true,
                    turn = true,

                ),
                testPlayer3.copy(
                    hand = arrayListOf(),
                    isAlive = false,
                    turn = false,
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
            chatMessage = "",
            gameMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(0)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)

        Truth.assertThat(result.players[0].turn).isTrue()
        Truth.assertThat(result.players[1].turn).isFalse()
        Truth.assertThat(result.players[2].turn).isFalse()
        Truth.assertThat(result.players[0].hand.size).isEqualTo(2)
    }

    @Test
    fun `onEnd, 4 Players, P1 isAlive, P2 isAlive, P3 !isAlive, P4 isAlive return P4's turn`() {
        val gameRoom2Players = GameRoom().copy(
            turn =1,
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
            chatMessage = "",
            gameMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(3)
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
            turn = 3,
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
            chatMessage = "",
            gameMessage = null,
            type = "game rule",
            uid = null
        )

        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(2)
        Truth.assertThat(result.deck.deck.size).isEqualTo(15)
        Truth.assertThat(result.players[0].turn).isFalse()
        Truth.assertThat(result.players[1].turn).isFalse()
        Truth.assertThat(result.players[2].turn).isTrue()
        Truth.assertThat(result.players[2].turnInProgress).isTrue()
        Truth.assertThat(result.players[3].turn).isFalse()
        Truth.assertThat(result.players[2].hand.size).isEqualTo(2)
    }

    @Test
    fun `onEnd, 4 Players, P1 isAlive, P2 isAlive, P3 isAlive, P4 isAlive, deck empty, return P1's turn`() {
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
            chatMessage = "",
            gameMessage = null,
            type = "game rule",
            uid = null
        )
        val result = GameRules.onEnd(gameRoom2Players, logMessage)

        Truth.assertThat(result.turn).isEqualTo(0)
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
    fun `eliminatePlayer, eliminate player 3, return gameRoom`() {
        val card = 4
        val gameRoom2Players = GameRoom().copy(
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
                    hand = arrayListOf(8),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(card),
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
            GameRules.eliminatePlayer(gameRoom2Players, gameRoom2Players.players[2])
        Truth.assertThat(result.players[2].isAlive).isFalse()
        Truth.assertThat(result.players[2].hand).isEmpty()
        Truth.assertThat(result.deck.discardDeck).contains(card)
    }

    @Test
    fun `assignTurns, return gameRoom`() {
        for (i in 1..100) {
            val card = 4
            val gameRoom2Players = GameRoom().copy(
                turn = 1,
                roomCode = "ABCD",
                roomNickname = "QRST",
                playLimit = 5,
                players = listOf(
                    testPlayer1.copy(
                        hand = arrayListOf(2),
                        isAlive = true,
                        turn = false,
                        turnOrder = 1

                    ),
                    testPlayer2.copy(
                        hand = arrayListOf(8),
                        isAlive = true,
                        turn = false,
                        turnOrder = 1

                    ),
                    testPlayer3.copy(
                        hand = arrayListOf(card),
                        isAlive = true,
                        turn = false,
                        turnOrder = 1
                    ),
                    testPlayer4.copy(
                        hand = arrayListOf(4),
                        isAlive = true,
                        turn = false,
                        turnOrder = 1
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
            val gameTurn = (0 until gameRoom2Players.players.size).shuffled().random()

            val result =
                GameRules.assignTurns(gameRoom2Players.players, gameTurn)
            println("Gameturn $gameTurn")
            Truth.assertThat(result.first { it.turn }).isNotNull()
            Truth.assertThat(result.first { it.turn }).isEqualTo(gameRoom2Players.players[gameTurn])
        }
    }


    @Test
    fun `dealCards, all players alive, P4's turn, return gameRoom`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(),
                    isAlive = true,
                    turn = false
                ),
                testPlayer4.copy(
                    hand = arrayListOf(),
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
            GameRules.dealCards(gameRoom2Players)
        println(result.players)
        for (i in 0 until 3) {
            if (!result.players[i].turn) {
                Truth.assertThat(result.players[i].hand.size).isEqualTo(1)

            } else {
                Truth.assertThat(result.players[i].hand.size).isEqualTo(2)
            }
        }
        Truth.assertThat(result.deck.deck.size).isEqualTo(11)
    }

    @Test
    fun `dealCards, 3 players, P3's turn, return gameRoom`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(),
                    isAlive = true,
                    turn = false

                ),
                testPlayer3.copy(
                    hand = arrayListOf(),
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
            GameRules.dealCards(gameRoom2Players)
        println(result.players)
        for (i in 0 until 3) {
            if (!result.players[i].turn) {
                Truth.assertThat(result.players[i].hand.size).isEqualTo(1)

            } else {
                Truth.assertThat(result.players[i].hand.size).isEqualTo(2)
            }
        }
        Truth.assertThat(result.deck.deck.size).isEqualTo(12)
    }

    @Test
    fun `dealCards, 2 players alive, P1's turn, return gameRoom`() {
        val gameRoom2Players = GameRoom().copy(
            turn = 4,
            roomCode = "ABCD",
            roomNickname = "QRST",
            playLimit = 5,
            players = listOf(
                testPlayer1.copy(
                    hand = arrayListOf(),
                    isAlive = true,
                    turn = false
                ),
                testPlayer2.copy(
                    hand = arrayListOf(),
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
            GameRules.dealCards(gameRoom2Players)
        println(result.players)
        for (i in 0 until 2) {
            if (!result.players[i].turn) {
                Truth.assertThat(result.players[i].hand.size).isEqualTo(1)

            } else {
                Truth.assertThat(result.players[i].hand.size).isEqualTo(2)
            }
        }
        Truth.assertThat(result.deck.deck.size).isEqualTo(13)
    }
}