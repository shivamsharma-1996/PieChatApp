package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam.piechatapp.domain.model.ConnectionStatus
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.usecase.GetConnectionStatusUseCase
import com.shivam.piechatapp.domain.usecase.GetConversationsUseCase
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertManager
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val conversationRepository: ConversationRepository,
    networkAlertManager: NetworkAlertManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationsUiState())
    val uiState: StateFlow<ConversationsUiState> = _uiState.asStateFlow()
    val networkAlertState: StateFlow<NetworkAlertState> = networkAlertManager.alertState

    init {
        observeConversations()
        observeConnectionStatus()
    }

    private fun observeConversations() {
        viewModelScope.launch {
            getConversationsUseCase().collect { conversations ->
                _uiState.value = _uiState.value.copy(
                    conversations = conversations,
                    isLoading = false
                )
            }
        }
    }

    private fun observeConnectionStatus() {
        viewModelScope.launch {
            getConnectionStatusUseCase().collect { status ->
                _uiState.value = _uiState.value.copy(
                    connectionStatus = status,
                    isLoading = status == ConnectionStatus.Connecting
                )
            }
        }
    }

    fun markConversationAsRead(userName: String) {
        conversationRepository.markConversationAsRead(userName)
    }
}