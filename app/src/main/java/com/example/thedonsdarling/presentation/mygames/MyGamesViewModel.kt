package com.example.thedonsdarling.presentation.mygames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.util.user.HandleUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MyGamesViewModel : ViewModel() {

    private val loadingState = MutableStateFlow<MyGamesState>(MyGamesState.Loading)

    val state = loadingState.asStateFlow()

    fun observeRoom() {
        viewModelScope.launch {
            HandleUser.observeMyGames().map { firestoreUser ->
                MyGamesState.Loaded(
                    firestoreUser = firestoreUser
                )
            }.collect {
                loadingState.emit(it)
            }
        }
    }
}