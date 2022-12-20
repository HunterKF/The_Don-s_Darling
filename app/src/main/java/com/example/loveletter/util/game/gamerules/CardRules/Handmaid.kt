package com.example.loveletter.util.game.gamerules.CardRules

import com.example.loveletter.domain.Player

class Handmaid {
    companion object {
        fun toggleProtection(player: Player): Boolean {
            return !player.protected
        }
    }
}