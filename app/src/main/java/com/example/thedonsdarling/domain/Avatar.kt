package com.example.thedonsdarling.domain

import androidx.annotation.DrawableRes
import com.example.thedonsdarling.R

sealed class Avatar(
    val description: Int,
    @DrawableRes val avatar: Int,
) {
    object Angela : Avatar(
        description = R.string.character_name_angela,
        avatar = R.drawable.character_angela
    )

    object Luigi : Avatar(
        description = R.string.character_name_luigi,
        avatar = R.drawable.character_luigi
    )

    object Paulie : Avatar(
        description = R.string.character_name_paulie,
        avatar = R.drawable.character_paulie
    )

    object Salvator : Avatar(
        description = R.string.character_name_salvator,
        avatar = R.drawable.character_salvator
    )

    object Sylvia : Avatar(
        description = R.string.character_name_sylvia,
        avatar = R.drawable.character_sylvia
    )

    object Tony : Avatar(
        description = R.string.character_name_tony,
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
