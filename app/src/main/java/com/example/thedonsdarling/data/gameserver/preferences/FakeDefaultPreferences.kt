package com.example.thedonsdarling.data.gameserver.preferences

import com.example.thedonsdarling.domain.preferences.Preferences

class FakeDefaultPreferences : Preferences {
    override fun toggleGuideEnabled(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getGuideEnabled(): Boolean {
        return true
    }

    override fun saveShouldShowOnboarding(shouldShow: Boolean) {
        TODO("Not yet implemented")
    }

    override fun loadShouldShowOnboarding(): Boolean {
        TODO("Not yet implemented")
    }
}