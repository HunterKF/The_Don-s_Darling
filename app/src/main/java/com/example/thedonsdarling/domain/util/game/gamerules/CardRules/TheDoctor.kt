package com.example.thedonsdarling.domain.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.models.Player

class TheDoctor {
    companion object {
        fun toggleProtection(player: Player): Boolean {
            return !player.protected
        }
    }
}