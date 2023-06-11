package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.data.gameserver.repository.FireStoreRepositoryImpl
import com.example.thedonsdarling.domain.models.Player
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JoinGameTest {
    @Mock
    private lateinit var mockDbGame: CollectionReference
    private lateinit var mockDbPlayers: CollectionReference

    @Mock
    private lateinit var mockDocument: DocumentReference

    @Mock
    private lateinit var mockGetTask: Task<DocumentSnapshot>
    @Mock
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var mockRepo: FireStoreRepository

    @Before
    fun setup() {
        mockDbGame = Firebase.firestore.collection("game")
        mockDbPlayers = Firebase.firestore.collection("players")
        mockRepo = FireStoreRepositoryImpl(mockDbGame, mockDbPlayers)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testJoinGame_codeNotFound() = runBlocking {
        // Arrange
        val roomCode = "ROOM123"
        val player = Player().copy(
            nickName = "John"
        )
    }
}