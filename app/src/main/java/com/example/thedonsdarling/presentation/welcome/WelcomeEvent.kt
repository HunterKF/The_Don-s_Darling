package com.example.thedonsdarling.presentation.welcome

sealed class WelcomeEvent {
    data class OnCompletion(val onNavigate: () -> Unit) : WelcomeEvent()
}