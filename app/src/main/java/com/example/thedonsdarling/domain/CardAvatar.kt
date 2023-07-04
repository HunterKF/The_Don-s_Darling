package com.example.thedonsdarling.domain

import androidx.annotation.DrawableRes
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.models.UiText

sealed class CardAvatar(
    val cardName: Int,
    val number: Int,
    val ruleDescription: Int,
    @DrawableRes val avatar: Int,
    val ruleShortDescription: Int,
    val numberInGame: Int,
) {
    object Policeman : CardAvatar(
        cardName = R.string.card_name_policeman,
        number = 1,
        avatar = R.drawable.icon_police_officer,
        ruleDescription = R.string.rule_policeman,
        ruleShortDescription = R.string.short_rule_policeman,
        numberInGame = 6
    )

    object PrivateEye : CardAvatar(
        cardName = R.string.card_name_private_eye,
        number = 2,
        ruleDescription = R.string.rule_private_eye,
        avatar = R.drawable.icon_private_eye,
        R.string.short_rule_private_eye,
        2
    )

    object Moneylender : CardAvatar(
        cardName = R.string.card_name_moneylender,
        number = 3,
        ruleDescription = R.string.rule_moneylender,
        avatar = R.drawable.icon_moneylender,
        R.string.short_rule_moneylender,
        2
    )

    object Doctor : CardAvatar(
        cardName = R.string.card_name_doctor,
        number = 4,
        ruleDescription = R.string.rule_doctor,
        avatar = R.drawable.icon_doctor,
        R.string.short_rule_doctor,
        2
    )

    object Wiseguy : CardAvatar(
        cardName = R.string.card_name_wiseguy,
        number = 5,
        ruleDescription = R.string.rule_wiseguy,
        avatar = R.drawable.icon_mobster,
        R.string.short_rule_wiseguy,
        2
    )

    object TheDon : CardAvatar(
        cardName = R.string.card_name_the_don,
        number = 6,
        ruleDescription = R.string.rule_the_don,
        avatar = R.drawable.icon_the_don,
        R.string.short_rule_the_don,
        1
    )

    object Courtesan : CardAvatar(
        cardName = R.string.card_name_courtesan,
        number = 7,
        ruleDescription = R.string.rule_courtesan,
        avatar = R.drawable.icon_courtesan,
        R.string.short_rule_courtesan,
        1
    )

    object Darling : CardAvatar(
        cardName = R.string.card_name_darling,
        number = 8,
        ruleDescription = R.string.rule_darling,
        avatar = R.drawable.icon_darling,
        R.string.short_rule_darling,
        1
    )

    companion object {
        fun setCardAvatar(code: Int?): CardAvatar {
            return when (code) {
                1 -> Policeman
                2 -> PrivateEye
                3 -> Moneylender
                4 -> Doctor
                5 -> Wiseguy
                6 -> TheDon
                7 -> Courtesan
                8 -> Darling
                else -> Policeman
            }
        }
        fun getStringResource(code: Int?): UiText.StringResource {
            return when (code) {
                1 -> UiText.StringResource(R.string.card_name_policeman)
                2 -> UiText.StringResource(R.string.card_name_private_eye)
                3 -> UiText.StringResource(R.string.card_name_moneylender)
                4 -> UiText.StringResource(R.string.card_name_doctor)
                5 -> UiText.StringResource(R.string.card_name_wiseguy)
                6 -> UiText.StringResource(R.string.card_name_the_don)
                7 -> UiText.StringResource(R.string.card_name_courtesan)
                8 -> UiText.StringResource(R.string.card_name_darling)
                else -> UiText.StringResource(R.string.card_name_policeman)
            }
        }
    }



}

val cardDefaults = listOf<CardAvatar>(
    CardAvatar.Policeman,
    CardAvatar.PrivateEye,
    CardAvatar.Moneylender,
    CardAvatar.Doctor,
    CardAvatar.Wiseguy,
    CardAvatar.TheDon,
    CardAvatar.Courtesan,
    CardAvatar.Darling
)
