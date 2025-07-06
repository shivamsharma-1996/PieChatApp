package com.shivam.piechatapp.presentation.ui.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam.piechatapp.Constants
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.usecase.SendMessageUseCase
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertManager
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    conversationRepository: ConversationRepository,
    networkAlertManager: NetworkAlertManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val conversationPartnerName: String = savedStateHandle[Constants.KEY_CONVO_PARTNER_NAME] ?: "Unknown"
    val messages: Flow<List<ChatMessage>> = conversationRepository.getMessagesForUser(conversationPartnerName)
    val networkAlertState: Flow<NetworkAlertState> = networkAlertManager.alertState

    fun sendMessage(messageText: String) {
        val message = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderName = Constants.LOGGED_IN_USER_ID,
            receiverName = conversationPartnerName,
            message = messageText,
            timestamp = Date(),
            isRead = true
        )

        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }
}