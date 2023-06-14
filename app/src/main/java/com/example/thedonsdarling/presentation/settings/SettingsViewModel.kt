package com.example.thedonsdarling.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.thedonsdarling.data.gameserver.preferences.DefaultPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: DefaultPreferences
) : ViewModel() {
    val reportPlayerAlert =
        mutableStateOf(false)

    fun onSettingsEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleGuide -> {
                preferences.toggleGuideEnabled(event.enable)
            }

        }

    }
}