package com.example.thedonsdarling.util.game.gamerules.CardRules

import com.example.thedonsdarling.domain.Player

class TheDoctor {
    companion object {
        fun toggleProtection(player: Player): Boolean {
            return !player.protected
        }
    }
}