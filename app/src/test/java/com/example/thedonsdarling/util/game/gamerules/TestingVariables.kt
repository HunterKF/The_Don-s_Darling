package com.example.thedonsdarling.util.game.gamerules

import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.Player

val testPlayer1 = Player().copy(
    avatar = 1,
    nickName = "Apple",
    uid = "apple_uid",
    ready = true,
    protected = false,
    turn = true,
    turnInProgress = true,
    turnOrder = 1,
    hand = arrayListOf(),
    isHost = true,
    isAlive = true,
    isWinner = false,
    wins = 0,
    unread = false,
    guide = true
)
val testPlayer2 = Player().copy(
    avatar = 1,
    nickName = "Bear",
    uid = "bear_uid",
    ready = true,
    protected = false,
    turn = false,
    turnInProgress = false,
    turnOrder = 2,
    hand = arrayListOf(),
    isHost = false,
    isAlive = true,
    isWinner = false,
    wins = 0,
    unread = false,
    guide = true
)
val testPlayer3 = Player().copy(
    avatar = 1,
    nickName = "Car",
    uid = "car_uid",
    ready = true,
    protected = false,
    turn = false,
    turnInProgress = false,
    turnOrder = 3,
    hand = arrayListOf(),
    isHost = false,
    isAlive = true,
    isWinner = false,
    wins = 0,
    unread = false,
    guide = true
)
val testPlayer4 = Player().copy(
    avatar = 1,
    nickName = "Deer",
    uid = "deer_uid",
    ready = true,
    protected = false,
    turn = false,
    turnInProgress = false,
    turnOrder = 4,
    hand = arrayListOf(),
    isHost = false,
    isAlive = true,
    isWinner = false,
    wins = 0,
    unread = false,
    guide = true
)

val testGameRoom = GameRoom().copy(
    turn = 1,
    roomCode = "ABCD",
    roomNickname = "QRST",
    playLimit = 5,
    players = listOf(
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