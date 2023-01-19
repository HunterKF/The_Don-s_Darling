package com.example.loveletter.domain

import androidx.annotation.DrawableRes
import com.example.loveletter.R

sealed class CardAvatar(
    val cardName: String,
    val number: Int,
    val ruleDescription: Int,
    @DrawableRes val avatar: Int,
    val ruleShortDescription: Int,
    val numberInGame: Int,
) {
    object Guard : CardAvatar(
        cardName = "Policeman",
        number = 1,
        avatar = R.drawable.icon_police_officer,
        ruleDescription = R.string.rule_policeman,
        ruleShortDescription = R.string.short_rule_policeman,
        numberInGame = 6
    )

    object Priest : CardAvatar(
        cardName = "Private Eye",
        number = 2,
        ruleDescription = R.string.rule_private_eye,
        avatar = R.drawable.icon_detective,
        R.string.short_rule_private_eye,
        2
    )

    object Baron : CardAvatar(
        cardName = "Moneylender",
        number = 3,
        ruleDescription = R.string.rule_moneylender,
        avatar = R.drawable.icon_loan_shark,
        R.string.short_rule_moneylender,
        2
    )

    object Handmaid : CardAvatar(
        cardName = "Doctor",
        number = 4,
        ruleDescription = R.string.rule_doctor,
        avatar = R.drawable.icon_doctor,
        R.string.short_rule_doctor,
        2
    )

    object Prince : CardAvatar(
        cardName = "Wiseguy",
        number = 5,
        ruleDescription = R.string.rule_wiseguy,
        avatar = R.drawable.icon_mobster,
        R.string.short_rule_wiseguy,
        2
    )

    object King : CardAvatar(
        cardName = "The Don",
        number = 6,
        ruleDescription = R.string.rule_the_don,
        avatar = R.drawable.icon_godfather,
        R.string.short_rule_the_don,
        1
    )

    object Countess : CardAvatar(
        cardName = "Courtesan",
        number = 7,
        ruleDescription =  R.string.rule_courtesan,
        avatar = R.drawable.icon_courtesan,
        R.string.short_rule_courtesan,
        1
    )

    object Princess : CardAvatar(
        cardName = "Darling",
        number = 8,
        ruleDescription = R.string.rule_darling,
        avatar = R.drawable.icon_the_daughter,
        R.string.short_rule_darling,
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
