package com.example.thedonsdarling.domain.preferences

interface Preferences {
    fun toggleGuideEnabled(enabled: Boolean)
    fun getGuideEnabled(): Boolean

    fun saveShouldShowOnboarding(shouldShow: Boolean)
    fun loadShouldShowOnboarding(): Boolean

    companion object {
        const val GUIDES_ENABLED = "guides_enabled"
        const val KEY_SHOULD_SHOW_ONBOARDING = "should_show_onboarding"

    }
}