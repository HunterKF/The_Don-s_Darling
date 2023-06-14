package com.example.thedonsdarling.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.data.gameserver.preferences.DefaultPreferences
import com.example.thedonsdarling.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val preferences: DefaultPreferences
) : ViewModel() {
    fun onWelcomeEvent(event: WelcomeEvent) {
        when (event) {
            is WelcomeEvent.OnCompletion -> {
                viewModelScope.launch {
                    preferences.saveShouldShowOnboarding(true)
                    event.onNavigate()
                }
            }
        }

    }
}