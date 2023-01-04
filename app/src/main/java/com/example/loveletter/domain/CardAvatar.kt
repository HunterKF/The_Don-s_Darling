package com.example.loveletter.domain

import android.util.Log
import androidx.annotation.DrawableRes
import com.example.loveletter.R
import com.example.loveletter.TAG

sealed class CardAvatar(
    val cardName: String,
    val number: Int,
    val ruleDescription: Int,
    @DrawableRes val avatar: Int,
    val ruleShortDescription: Int,
    val numberInGame: Int,
) {
    object Guard : CardAvatar(
        cardName = "Guard",
        number = 1,
        avatar = R.drawable.icon_gallo,
        ruleDescription = R.string.rule_guard,
        ruleShortDescription = R.string.short_rule_guard,
        numberInGame = 6
    )

    object Priest : CardAvatar(
        cardName = "Priest",
        number = 2,
        ruleDescription = R.string.rule_priest,
        avatar = R.drawable.icon_catrin,
        R.string.short_rule_priest,
        2
    )

    object Baron : CardAvatar(
        cardName = "Baron",
        number = 3,
        ruleDescription = R.string.rule_baron,
        avatar = R.drawable.icon_valiente,
        R.string.short_rule_baron,
        2
    )

    object Handmaid : CardAvatar(
        cardName = "Handmaid",
        number = 4,
        ruleDescription = R.string.rule_handmaid,
        avatar = R.drawable.icon_dama,
        R.string.short_rule_handmaid,
        2
    )

    object Prince : CardAvatar(
        cardName = "Prince",
        number = 5,
        ruleDescription = R.string.rule_prince,
        avatar = R.drawable.icon_diablo,
        R.string.short_rule_prince,
        2
    )

    object King : CardAvatar(
        cardName = "King",
        number = 6,
        ruleDescription = R.string.rule_king,
        avatar = R.drawable.icon_diablo,
        R.string.short_rule_king,
        1
    )

    object Countess : CardAvatar(
        cardName = "Countess",
        number = 7,
        ruleDescription =  R.string.rule_countess,
        avatar = R.drawable.icon_sirena,
        R.string.short_rule_countess,
        1
    )

    object Princess : CardAvatar(
        cardName = "Princess",
        number = 8,
        ruleDescription = R.string.rule_princess,
        avatar = R.drawable.icon_muerte,
        R.string.short_rule_princess,
        1
    )

    companion object {
        fun setCardAvatar(code: Int?): CardAvatar {
            return when (code) {
                1 -> Guard
                2 -> Priest
                3 -> Baron
                4 -> Handmaid
                5 -> Prince
                6 -> King
                7 -> Countess
                8 -> Princess
                else -> Guard
            }
        }
    }
}
