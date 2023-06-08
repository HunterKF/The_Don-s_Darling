package com.example.thedonsdarling.di

import android.content.Context
import com.example.thedonsdarling.data.gameserver.repository.FireStoreRepositoryImpl
import com.example.thedonsdarling.domain.DataStoreRepository
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.example.thedonsdarling.domain.use_cases.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context,
    ) = DataStoreRepository(context = context)

    @Provides
    @Singleton
    fun provideGameReference() = Firebase.firestore.collection("game")

    @Provides
    @Singleton
    fun providePlayersReference() = Firebase.firestore.collection("players")


    @Provides
    @Singleton
    fun provideFireStoreRepository(
        dbPlayers: CollectionReference,
        dbGame: CollectionReference,
    ): FireStoreRepository = FireStoreRepositoryImpl(dbPlayers, dbGame)

    @Provides
    fun provideUseCases(
        repository: FireStoreRepository,
    ) = UseCases(
        addGameToMultiplePlayers = AddGameToMultiplePlayers(repository),
        addGameToPlayer = AddGameToPlayer(repository),
        createRoom = CreateRoom(repository),
        createUserPlayer = CreateUserPlayer(repository),
        deleteRoomFromFirestore = DeleteRoomFromFirestore(repository),
        deleteUserGameRoomForAll = DeleteUserGameRoomForAll(repository),
        deleteUserGameRoomForLocal = DeleteUserGameRoomForLocal(repository),
        observeMyGames = ObserveMyGames(repository),
        removePlayerFromGame = RemovePlayerFromGame(repository),
        removeSingleGameFromAllPlayersJoinedGames = RemoveSingleGameFromAllPlayersJoinedGames(
            repository
        ),
        removeSingleGameFromPlayerJoinedList = RemoveSingleGameFromPlayerJoinedList(repository),
        setGameInDB = SetGameInDB(repository),
        startGame = StartGame(),
        subscribeToRealtimeUpdates = SubscribeToRealtimeUpdates(repository),
        checkGame = CheckGame(repository),
        joinGame = JoinGame(repository),
        startNewRound = StartNewRound(),
        startNewGame = StartNewGame(),
        updateUnreadStatusForLocal = UpdateUnreadStatusForLocal(repository),
        updateUnreadStatusForAll = UpdateUnreadStatusForAll(repository),
        sendMessage = SendMessage(repository)
    )
}