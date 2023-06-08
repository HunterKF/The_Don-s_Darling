package com.example.thedonsdarling.util

sealed class UiEvent {
    object Success: UiEvent()
    object HandleUser: UiEvent()
    object DeleteRoom: UiEvent()
    object ExitGame : UiEvent()

    data class ShowSnackbar(val message: UiText): UiEvent()
}