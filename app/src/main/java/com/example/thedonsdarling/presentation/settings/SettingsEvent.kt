package com.example.thedonsdarling.presentation.settings

sealed class SettingsEvent {
    data class ToggleGuide(val enable: Boolean) : SettingsEvent()
}
