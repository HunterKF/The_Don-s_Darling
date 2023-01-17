package com.example.loveletter.domain

import androidx.annotation.DrawableRes
import com.example.loveletter.R

sealed class Avatar(
    val description: String,
    @DrawableRes val avatar: Int,
) {
    object Angela : Avatar(
        description = "Angela",
        avatar = R.drawable.character_angela
    )

    object Luigi : Avatar(
        description = "Luigi",
        avatar = R.drawable.character_luigi
    )

    object Paulie : Avatar(
        description = "Paulie",
        avatar = R.drawable.character_paulie
    )

    object Salvator : Avatar(
        description = "Salvator",
        avatar = R.drawable.character_salvator
    )

    object Sylvia : Avatar(
        description = "Sylvia",
        avatar = R.drawable.character_sylvia
    )

    object Tony : Avatar(
        description = "Tony",
        avatar = R.drawable.character_tony
    )

    companion object {
        fun setAvatar(code: Int?): Avatar {
            return when (code) {
                1 -> Luigi
                2 -> Salvator
                3 -> Paulie
                4 -> Tony
                5 -> Angela
                6 -> Sylvia
                else -> Angela
            }
        }
    }
}
