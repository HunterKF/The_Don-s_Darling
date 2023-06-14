package com.example.thedonsdarling.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.thedonsdarling.data.gameserver.preferences.DefaultPreferences
import com.example.thedonsdarling.data.gameserver.repository.FireStoreRepositoryImpl
import com.example.thedonsdarling.domain.DataStoreRepository
import com.example.thedonsdarling.domain.preferences.Preferences
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
import javax.inject.Named
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
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return DefaultPreferences(sharedPreferences)
    }




    @Provides
    @Singleton
    @Named("dbGame")
    fun provideGameReference() = Firebase.firestore.collection("game")

    @Provides
    @Singleton
    @Named("dbPlayers")
    fun providePlayersReference() = Firebase.firestore.collection("players")


    @Provides
    @Singleton
    fun provideFireStoreRepository(
        @Named("dbPlayers") dbPlayers: CollectionReference,
        @Named("dbGame") dbGame: CollectionReference,
    ): FireStoreRepository = FireStoreRepositoryImpl(dbPlayers, dbGame)

    @Provides
    @Singleton
    fun provideUseCases(
        repository: FireStoreRepository,
    ) = UseCases(
        addGameToMultiplePlayers = AddGameToMultiplePlayers(repository),
        addGameToPlayer = AddGameToPlayer(repository),
        createBlankRoom = CreateBlankRoom(repository),
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