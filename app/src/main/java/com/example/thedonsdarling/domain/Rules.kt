package com.example.thedonsdarling.domain

import com.example.thedonsdarling.R

sealed class Rules(
    val title: Int,
    val description: List<Int>,
) {
    object InitialRules : Rules(
        title = R.string.rules_playing_the_game,
        description = listOf(
            R.string.rules_playing_the_game_one,
            R.string.rules_playing_the_game_two,
            R.string.rules_playing_the_game_three
        )
    )
    object PlayerTurn : Rules(
        title = R.string.rules_player_turn,
        description = listOf(
            R.string.rules_player_turn_one
        )
    )
    object OutOfTheRound : Rules(
        title = R.string.rules_out_of_the_round,
        description = listOf(
            R.string.rules_out_of_the_round_one,
            R.string.rules_out_of_the_round_two
        )
    )
    object PlayedAndDiscardedCards : Rules(
        title = R.string.rules_discard_cards,
        description = listOf(
            R.string.rules_discard_cards_one,
        )
    )
    object EndOfARound : Rules(
        title = R.string.rule_end_of_round,
        description = listOf(
            R.string.rule_end_of_round_one,
        )
    )
    object DeckRunsOut : Rules(
        title = R.string.rule_deck_runs_out,
        description = listOf(
            R.string.rule_deck_runs_out_one,
            R.string.rule_deck_runs_out_two,
            R.string.rule_deck_runs_out_three,
        )
    )
    object OnePlayerLeft : Rules(
        title = R.string.rule_one_player_left,
        description = listOf(
            R.string.rule_one_player_left_one
        )
    )
}
val rules = listOf(
    Rules.InitialRules,
    Rules.PlayerTurn,
    Rules.OutOfTheRound,
    Rules.PlayedAndDiscardedCards,
    Rules.EndOfARound,
    Rules.DeckRunsOut,
    Rules.OnePlayerLeft
)