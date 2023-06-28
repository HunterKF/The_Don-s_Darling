package com.example.thedonsdarling.util

import android.content.Context
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.models.Player

sealed class UiEvent {
    object Success : UiEvent()
    object CreateUserPlayer : UiEvent()
    object RemovePlayer : UiEvent()
    data class DeleteRoom(val gameRoom: GameRoom) : UiEvent()
    data class ExitGame(val gameRoom: GameRoom) : UiEvent()
    data class EndRound(
        val alivePlayers: List<Player>,
        val game: GameRoom,
        val playerIsPlaying: Boolean,
    ) : UiEvent()

    object ObserveRoom : UiEvent()
    data class StartRound(val gameRoom: GameRoom) : UiEvent()

    data class LeaveGame(val gameRoom: GameRoom) : UiEvent()

    data class CheckRoom(val context: Context) : UiEvent()
    data class JoinGame(val context: Context, val navigate: () -> Unit) : UiEvent() {

    }

    data class StartNewGame(val gameRoom: GameRoom) : UiEvent()
    data class UpdateUnreadStatus(val gameRoom: GameRoom) : UiEvent() {

    }

    data class SendMessage(val gameRoom: GameRoom, val logMessage: LogMessage) : UiEvent()
    data class InitialStart(
        val gameRoom: GameRoom,
        val context: Context,
        val onNavigate: () -> Unit,
    ) : UiEvent()

    data class OnPlay(val card: Int, val player: Player, val gameRoom: GameRoom) : UiEvent()
    data class OnSelectPlayer(val selectedPlayer: Player, val gameRoom: GameRoom) : UiEvent()

    data class OnGuess(val card: Int, val gameRoom: GameRoom) : UiEvent()
}
