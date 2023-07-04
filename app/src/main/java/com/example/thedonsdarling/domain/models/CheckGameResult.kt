package com.example.thedonsdarling.domain.models

sealed class CheckGameResult {
    object GameFound : CheckGameResult()
    object GameNotFound : CheckGameResult()
}
