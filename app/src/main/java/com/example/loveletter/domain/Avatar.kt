package com.example.loveletter.domain

import androidx.annotation.DrawableRes
import com.example.loveletter.R

sealed class Avatar(
    val description: String,
    @DrawableRes val avatar: Int,
) {
    object PurpleChar : Avatar(
        description = "Purple Avatar",
        avatar = R.drawable.purplechar
    )

    object BlueChar : Avatar(
        description = "Blue Avatar",
        avatar = R.drawable.bluechar
    )

    object GoldChar : Avatar(
        description = "GOld Avatar",
        avatar = R.drawable.goldchar
    )

    object GreenChar : Avatar(
        description = "Green Avatar",
        avatar = R.drawable.greenchar
    )

    object RedChar : Avatar(
        description = "Red Avatar",
        avatar = R.drawable.redchar
    )

    object PinkChar : Avatar(
        description = "Pink Avatar",
        avatar = R.drawable.pinkchar
    )

    companion object {
        fun setAvatar(code: Int?): Avatar {
            return when (code) {
                0 -> PurpleChar
                1 -> BlueChar
                2 -> GoldChar
                3 -> GreenChar
                4 -> RedChar
                5 -> PinkChar
                else -> PurpleChar
            }
        }
    }
}
