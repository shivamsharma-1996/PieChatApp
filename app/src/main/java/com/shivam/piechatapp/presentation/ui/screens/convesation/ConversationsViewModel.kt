package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.lifecycle.ViewModel
import com.shivam.piechatapp.domain.usecase.ConnectWebSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val connectWebSocketUseCase: ConnectWebSocketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationsUiState())
    val uiState: StateFlow<ConversationsUiState> = _uiState.asStateFlow()

    fun connect() {
        connectWebSocketUseCase()
    }
}