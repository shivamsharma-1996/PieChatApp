package com.shivam.piechatapp.presentation.ui.screens.convesation

import com.shivam.piechatapp.domain.model.ConnectionStatus
import com.shivam.piechatapp.domain.model.Conversation

data class ConversationsUiState(
    val conversations: List<Conversation> = emptyList(),
    val connectionStatus: ConnectionStatus = ConnectionStatus.Disconnected,
    val isLoading: Boolean = true
)