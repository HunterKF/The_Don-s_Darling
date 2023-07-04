package com.example.thedonsdarling.presentation.mygames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thedonsdarling.domain.repository.FireStoreRepository
import com.example.thedonsdarling.domain.use_cases.UseCases
import com.example.thedonsdarling.domain.util.user.HandleUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGamesViewModel @Inject constructor(
    private val useCases: UseCases
)  : ViewModel() {

    private val loadingState = MutableStateFlow<MyGamesState>(MyGamesState.Loading)

    val state = loadingState.asStateFlow()

    fun observeRoom() {
        viewModelScope.launch {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                useCases.observeMyGames(currentUser).map { fireStoreUser ->
                    MyGamesState.Loaded(
                        firestoreUser = fireStoreUser
                    )
                }.collect {
                    loadingState.emit(it)
                }
            }
        }
    }
}