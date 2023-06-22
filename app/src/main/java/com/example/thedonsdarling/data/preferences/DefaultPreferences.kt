package com.example.thedonsdarling.data.preferences

import android.content.SharedPreferences
import com.example.thedonsdarling.domain.preferences.Preferences
import javax.inject.Inject

class DefaultPreferences @Inject constructor(
    private val sharedPref: SharedPreferences,
    ) : Preferences {
    override fun toggleGuideEnabled(enabled: Boolean) {
        sharedPref.edit()
            .putBoolean(Preferences.GUIDES_ENABLED, enabled)
            .apply()
    }

    override fun getGuideEnabled(): Boolean {
        return sharedPref.getBoolean(
            Preferences.GUIDES_ENABLED,
            true
        )
    }

    override fun saveShouldShowOnboarding(shouldShow: Boolean) {
        sharedPref.edit()
            .putBoolean(Preferences.KEY_SHOULD_SHOW_ONBOARDING, shouldShow)
            .apply()
    }

    override fun loadShouldShowOnboarding(): Boolean {
        return sharedPref.getBoolean(
            Preferences.KEY_SHOULD_SHOW_ONBOARDING,
            true
        )
    }

}