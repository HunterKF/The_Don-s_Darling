package com.example.loveletter.domain

import androidx.annotation.DrawableRes
import com.example.loveletter.R

sealed class CardAvatar(
    val description: String,
    val number: String,
    @DrawableRes val avatar: Int,
) {
    object PinkChar : CardAvatar(
        description = "Pink Avatar",
        number = "1",
        avatar = R.drawable.pinkchar
    )

    companion object {
        fun setCardAvatar(code: Int?): Avatar {
            return when (code) {
                0 -> Avatar.PurpleChar
                1 -> Avatar.BlueChar
                2 -> Avatar.GoldChar
                3 -> Avatar.GreenChar
                4 -> Avatar.RedChar
                5 -> Avatar.PinkChar
                else -> Avatar.PurpleChar
            }
        }
    }
}
