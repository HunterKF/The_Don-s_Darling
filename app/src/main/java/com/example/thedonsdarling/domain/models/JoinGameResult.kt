package com.example.thedonsdarling.domain.models

sealed class JoinGameResult {
    object CodeNotFound : JoinGameResult()
    object GameFull : JoinGameResult()
    object Success : JoinGameResult()
    object UnknownError : JoinGameResult()
}
