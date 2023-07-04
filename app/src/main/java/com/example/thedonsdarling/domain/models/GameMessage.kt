package com.example.thedonsdarling.domain.models

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.thedonsdarling.R
import com.example.thedonsdarling.domain.CardAvatar

data class GameMessage(
    val gameMessageType: String,
    val players: List<Player>?,
    val player1: Player?,
    val player2: Player?,
    val usedCard: Int? = null,
) {
    constructor(
    ) : this(
        gameMessageType = "",
        players = null,
        player1 = null,
        player2 = null,
        usedCard = null
    )

    companion object {
        fun fromMessageReturnToastString(context: Context, gameMessage: GameMessage): String {
            when (gameMessage.gameMessageType) {
                "game_round_over_winner" -> {
                    return context.getString(
                        R.string.round_over_winner_message,
                        gameMessage.player1!!.nickName
                    )
                }
                "game_round_over_tie_2_players" -> {
                    return context.getString(
                        R.string.game_tie_message_2_players,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "game_round_over_tie_3_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return context.getString(
                        R.string.game_tie_message_3_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2]
                    )
                }
                "game_round_over_tie_4_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return context.getString(
                        R.string.game_tie_message_4_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2],
                        nicknames[3]
                    )
                }
                "game_start" -> {
                    return context.getString(R.string.start_game_announcement_toast)
                }

                "game_over" -> {
                    return context.getString(R.string.game_over_winner_message)
                }
                "game_new_round" -> {
                    return context.getString(R.string.new_round_message)
                }
                //CARDS

                "card_police_correct" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard).asString(context)
                    return context.getString(
                        R.string.card_policemen_toast_message_correct,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )
                }
                "card_police_incorrect" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)
                    return context.getString(
                        R.string.card_policemen_toast_message_incorrect,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )
                }
                "card_private_eye" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_private_eye_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "card_money_lender_p1_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_moneylender_toast_message_win,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_p2_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_moneylender_toast_message_lose,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_draw" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_moneylender_toast_message_tie,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_the_doctor" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_doctor_toast_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_the_doctor_protected" -> {
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)

                    return context.getString(
                        R.string.card_doctor_protection_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                    /*Todo - This is special and won't work this way.*/
                }
                "card_wise_guy_forced_to_discard" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)

                    return context.getString(
                        R.string.card_wiseguy_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )


                }
                "card_wise_guy_forced_to_discard_darling" -> {
                    /*Todo - This is special and won't work this way.*/

                    return context.getString(
                        R.string.card_darling_forced_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "card_wise_guy_forced_to_discard_empty_deck" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)

                    return context.getString(
                        R.string.card_wiseguy_toast_message_empty_deck,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                }
                "card_the_don" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_the_don_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_courtesan" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_courtesan_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_darling" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_darling_toast_message,
                        gameMessage.player1!!.nickName
                    )

                }
                else -> {
                    return context.getString(R.string.unknown_error)
                }

            }
        }

        fun fromMessageReturnMessageString(context: Context, gameMessage: GameMessage): String {
            when (gameMessage.gameMessageType) {
                "game_round_over_winner" -> {
                    return context.getString(
                        R.string.round_over_winner_message,
                        gameMessage.player1!!.nickName
                    )
                }
                "game_round_over_tie_2_players" -> {
                    return context.getString(
                        R.string.game_tie_message_2_players,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "game_round_over_tie_3_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return context.getString(
                        R.string.game_tie_message_3_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2]
                    )
                }
                "game_round_over_tie_4_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return context.getString(
                        R.string.game_tie_message_4_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2],
                        nicknames[3]
                    )
                }
                "game_start" -> {
                    return context.getString(R.string.start_game_announcement_message)
                }

                "game_over" -> {
                    return context.getString(R.string.game_over_winner_message)
                }
                "game_new_round" -> {
                    return context.getString(R.string.new_round_message)
                }
                //CARDS

                "card_police_correct" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.player2!!.hand.first())
                        .asString(context)
                    return context.getString(
                        R.string.card_policemen_message_correct,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2.nickName,
                        card
                    )
                }
                "card_police_incorrect" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)
                    return context.getString(
                        R.string.card_policemen_message_incorrect,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )
                }
                "card_private_eye" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(R.string.card_private_eye_message)
                }
                "card_money_lender_p1_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_moneylender_message_win,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_p2_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_moneylender_message_lose,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_draw" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_moneylender_message_tie,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_the_doctor" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_doctor_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_the_doctor_protected" -> {
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)

                    return context.getString(
                        R.string.card_doctor_protection_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                    /*Todo - This is special and won't work this way.*/
                }
                "card_wise_guy_forced_to_discard" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)

                    return context.getString(
                        R.string.card_wiseguy_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )


                }
                "card_wise_guy_forced_to_discard_darling" -> {
                    /*Todo - This is special and won't work this way.*/

                    return context.getString(
                        R.string.card_darling_forced_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "card_wise_guy_forced_to_discard_empty_deck" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.usedCard!!).asString(context)

                    return context.getString(
                        R.string.card_wiseguy_message_empty_deck,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                }
                "card_the_don" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_the_don_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_courtesan" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_courtesan_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_darling" -> {
                    /*Todo - This is special and won't work this way.*/
                    return context.getString(
                        R.string.card_darling_message,
                        gameMessage.player1!!.nickName
                    )

                }
                else -> {
                    return context.getString(R.string.unknown_error)
                }

            }
        }

        @Composable
        fun fromMessageReturnToastString(gameMessage: GameMessage): String {
            when (gameMessage.gameMessageType) {
                "game_round_over_winner" -> {
                    return stringResource(
                        R.string.round_over_winner_message,
                        gameMessage.player1!!.nickName
                    )
                }
                "game_round_over_tie_2_players" -> {
                    return stringResource(
                        R.string.game_tie_message_2_players,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "game_round_over_tie_3_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return stringResource(
                        R.string.game_tie_message_3_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2]
                    )
                }
                "game_round_over_tie_4_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return stringResource(
                        R.string.game_tie_message_4_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2],
                        nicknames[3]
                    )
                }
                "game_start" -> {
                    return stringResource(R.string.start_game_announcement_toast)
                }

                "game_over" -> {
                    return stringResource(R.string.game_over_winner_message)
                }
                "game_new_round" -> {
                    return stringResource(R.string.new_round_message)
                }
                //CARDS

                "card_police_correct" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.player2!!.hand.first()).asString()
                    return stringResource(
                        R.string.card_policemen_toast_message_correct,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2.nickName,
                        card
                    )
                }
                "card_police_incorrect" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()
                    return stringResource(
                        R.string.card_policemen_toast_message_incorrect,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )
                }
                "card_private_eye" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(R.string.card_private_eye_message)
                }
                "card_money_lender_p1_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_moneylender_toast_message_win,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_p2_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_moneylender_toast_message_lose,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_draw" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_moneylender_toast_message_tie,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_the_doctor" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_doctor_toast_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_the_doctor_protected" -> {
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()

                    return stringResource(
                        R.string.card_doctor_protection_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                    /*Todo - This is special and won't work this way.*/
                }
                "card_wise_guy_forced_to_discard" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()

                    return stringResource(
                        R.string.card_wiseguy_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )


                }
                "card_wise_guy_forced_to_discard_darling" -> {
                    /*Todo - This is special and won't work this way.*/

                    return stringResource(
                        R.string.card_darling_forced_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "card_wise_guy_forced_to_discard_empty_deck" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()

                    return stringResource(
                        R.string.card_wiseguy_toast_message_empty_deck,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                }
                "card_the_don" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_the_don_toast_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_courtesan" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_courtesan_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_darling" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_darling_toast_message,
                        gameMessage.player1!!.nickName
                    )

                }
                else -> {
                    return stringResource(R.string.unknown_error)
                }

            }
        }

        @Composable
        fun fromMessageReturnMessageString(gameMessage: GameMessage): String {
            when (gameMessage.gameMessageType) {
                "game_round_over_winner" -> {
                    return stringResource(
                        R.string.round_over_winner_message,
                        gameMessage.player1!!.nickName
                    )
                }
                "game_round_over_tie_2_players" -> {
                    return stringResource(
                        R.string.game_tie_message_2_players,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "game_round_over_tie_3_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return stringResource(
                        R.string.game_tie_message_3_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2]
                    )
                }
                "game_round_over_tie_4_players" -> {
                    val nicknames: List<String> = gameMessage.players!!.map { player ->
                        player.nickName
                    }
                    return stringResource(
                        R.string.game_tie_message_4_players,
                        nicknames[0],
                        nicknames[1],
                        nicknames[2],
                        nicknames[3]
                    )
                }
                "game_start" -> {
                    return stringResource(R.string.start_game_announcement_message)
                }

                "game_over" -> {
                    return stringResource(R.string.game_over_winner_message)
                }
                "game_new_round" -> {
                    return stringResource(R.string.new_round_message)
                }
                //CARDS

                "card_police_correct" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card =
                        CardAvatar.getStringResource(gameMessage.player2!!.hand.first()).asString()
                    return stringResource(
                        R.string.card_policemen_message_correct,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2.nickName,
                        card
                    )
                }
                "card_police_incorrect" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()
                    return stringResource(
                        R.string.card_policemen_message_incorrect,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )
                }
                "card_private_eye" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(R.string.card_private_eye_message)
                }
                "card_money_lender_p1_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_moneylender_message_win,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_p2_wins" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_moneylender_message_lose,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_money_lender_draw" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_moneylender_message_tie,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_the_doctor" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_doctor_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_the_doctor_protected" -> {
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()

                    return stringResource(
                        R.string.card_doctor_protection_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                    /*Todo - This is special and won't work this way.*/
                }
                "card_wise_guy_forced_to_discard" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()

                    return stringResource(
                        R.string.card_wiseguy_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )


                }
                "card_wise_guy_forced_to_discard_darling" -> {
                    /*Todo - This is special and won't work this way.*/

                    return stringResource(
                        R.string.card_darling_forced_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )
                }
                "card_wise_guy_forced_to_discard_empty_deck" -> {
                    /*Todo - This is special and won't work this way.*/
                    val card = CardAvatar.getStringResource(gameMessage.usedCard!!).asString()

                    return stringResource(
                        R.string.card_wiseguy_message_empty_deck,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName,
                        card
                    )

                }
                "card_the_don" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_the_don_message,
                        gameMessage.player1!!.nickName,
                        gameMessage.player2!!.nickName
                    )

                }
                "card_courtesan" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_courtesan_message,
                        gameMessage.player1!!.nickName
                    )

                }
                "card_darling" -> {
                    /*Todo - This is special and won't work this way.*/
                    return stringResource(
                        R.string.card_darling_message,
                        gameMessage.player1!!.nickName
                    )

                }
                else -> {
                    return stringResource(R.string.unknown_error)
                }

            }
        }

    }
}

sealed class GameMessageType(
    val messageType: String,
) {
    object RoundOverWinner : GameMessageType(
        messageType = "game_round_over_winner"
    )

    object RoundOverTie2 : GameMessageType(
        messageType = "game_round_over_tie_2_players"
    )

    object RoundOverTie3 : GameMessageType(
        messageType = "game_round_over_tie_3_players"
    )

    object RoundOverTie4 : GameMessageType(
        messageType = "game_round_over_tie_4_players"
    )

    object GameStart : GameMessageType(
        messageType = "game_start"
    )

    object GameOver : GameMessageType(
        messageType = "game_over"
    )

    object NewRound : GameMessageType(
        messageType = "game_new_round"
    )

    /*CARDS*/
    object PoliceCorrect : GameMessageType(
        messageType = "card_police_correct"
    )

    object PoliceWrong : GameMessageType(
        messageType = "card_police_correct"
    )

    object PrivateEye : GameMessageType(
        messageType = "card_private_eye"
    )

    object MoneyLenderP1Win : GameMessageType(
        messageType = "card_money_lender_p1_wins"
    )

    object MoneyLenderP2Win : GameMessageType(
        messageType = "card_money_lender_p2_wins"
    )

    object MoneyLenderDraw : GameMessageType(
        messageType = "card_money_lender_draw"
    )

    object TheDoctor : GameMessageType(
        messageType = "card_the_doctor"
    )

    object TheDoctorProtected : GameMessageType(
        messageType = "card_the_doctor_protected"
    )

    object WiseGuyForcedToDiscard : GameMessageType(
        messageType = "card_wise_guy_forced_to_discard"
    )

    object WiseGuyForcedToDiscardDarling : GameMessageType(
        messageType = "card_wise_guy_forced_to_discard_darling"
    )

    object WiseGuyForcedToDiscardAndEmptyDeck : GameMessageType(
        messageType = "card_wise_guy_forced_to_discard_empty_deck"
    )

    object TheDon : GameMessageType(
        messageType = "card_the_don"
    )

    object Courtesan : GameMessageType(
        messageType = "card_courtesan"
    )

    object Darling : GameMessageType(
        messageType = "card_darling"
    )

}