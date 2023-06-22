package com.example.thedonsdarling.data.entity

import com.example.thedonsdarling.data.mapper.toGameRoomEntity
import com.example.thedonsdarling.domain.models.GameRoom
import com.example.thedonsdarling.domain.models.LogMessage
import com.example.thedonsdarling.domain.models.UiText
import io.mockk.InternalPlatformDsl.toArray
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.util.*

class GameRoomMapperTest {

    @Test
    fun `Test GameRoom to Entity`() {
        val gameRoom = GameRoom().copy(
            gameLog = arrayListOf(
                LogMessage(
                ).copy(
                    message = UiText.StringResource(
                        resId = 124
                    ),
                    toastMessage =  UiText.StringResource(
                        resId = 124
                    ),
                    type = "gamelog",
                    uid = "1234",
                    date = Date()
                )
            )
        )
        val entity = gameRoom.toGameRoomEntity()
        println(entity)
    }
}