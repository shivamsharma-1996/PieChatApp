package com.shivam.piechatapp.presentation.ui.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam.piechatapp.Constants
import com.shivam.piechatapp.data.handler.MessageHandler
import com.shivam.piechatapp.data.manager.QueueSimulationManager
import com.shivam.piechatapp.data.network.NetworkStatusManager
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.usecase.SendMessageUseCase
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertManager
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    conversationRepository: ConversationRepository,
    networkAlertManager: NetworkAlertManager,
    private val networkStatusManager: NetworkStatusManager,
    private val savedStateHandle: SavedStateHandle,
    private val queueSimulationManager: QueueSimulationManager,
    private val socketRepository: PieSocketWebSocketRepository,
    private val messageHandler: MessageHandler
) : ViewModel() {
    val conversationPartnerName: String = savedStateHandle[Constants.KEY_CONVO_PARTNER_NAME] ?: "Unknown"
    val messages: Flow<List<ChatMessage>> = conversationRepository.getMessagesForUser(conversationPartnerName)
    val networkAlertState: Flow<NetworkAlertState> = networkAlertManager.alertState

    val queueMode: StateFlow<Boolean> = queueSimulationManager.queueMode

    companion object {
        private const val KEY_SIMULATE_ONLINE = "simulate_online"
    }

    init {
        // Restore last toggle state if present
        val lastSimulated = savedStateHandle.get<Boolean>(KEY_SIMULATE_ONLINE)
        if (lastSimulated != null) {
            setSimulatedNetworkAvailable(lastSimulated)
        }
    }

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

    fun setSimulatedNetworkAvailable(available: Boolean) {
        networkStatusManager.setSimulatedNetworkAvailable(available)
        savedStateHandle[KEY_SIMULATE_ONLINE] = available
    }

    fun setQueueMode(enabled: Boolean) {
        queueSimulationManager.setQueueMode(enabled)
        if (!enabled) {
            // If queue mode is turned off, try to process queued messages
            if (networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()) {
                messageHandler.processQueuedMessages()
            }
        }
    }
}