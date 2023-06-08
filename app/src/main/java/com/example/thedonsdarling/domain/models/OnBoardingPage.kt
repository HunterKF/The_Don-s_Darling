package com.example.thedonsdarling.domain.models

import androidx.annotation.DrawableRes
import com.example.thedonsdarling.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.icon_mafia_group,
        title = "Welcome",
        description = "Play against others to win over the Don's Darling."
    )
    object Second : OnBoardingPage(
        image = R.drawable.icon_resource,
        title = "How to Play",
        description = "Use your connections to take out the competition."
    )
    object Third : OnBoardingPage(
        image = R.drawable._12_512_b,
        title = "Ready?",
        description = "Will you be the one holding Darling at the end of the game?"
    )
}
