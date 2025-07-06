package com.shivam.piechatapp.data.handler

import com.shivam.piechatapp.data.network.NetworkStatusManager
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.MessageStatus
import com.shivam.piechatapp.domain.service.messagequeue.MessageQueueService
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageHandler @Inject constructor(
    private val networkStatusManager: NetworkStatusManager,
    private val socketRepository: PieSocketWebSocketRepository,
    private val messageQueueService: MessageQueueService,
    private val networkAlertManager: NetworkAlertManager
) {
    
    fun sendMessage(message: ChatMessage): ChatMessage {
        // Check both internet connectivity and socket connection
        val canSend = networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()
        
        return if (canSend) {
            val result = socketRepository.sendMessage(message) // Try to send immediately
            if (result.isSuccess) {
                message.copy(status = MessageStatus.SENT)
            } else {
                messageQueueService.queueMessage(message) // If sending fails, queue the message
                message.copy(status = MessageStatus.QUEUED)
            }
        } else {
            messageQueueService.queueMessage(message) // Queue the message if conditions are not met

            networkAlertManager.showMessageQueuedAlert()
            networkAlertManager.clearAlertAfterDelay(3000) {
                if (!networkStatusManager.isNetworkAvailableNow()) {
                    networkAlertManager.showNoInternetAlert()
                }
            }

            message.copy(status = MessageStatus.QUEUED)
        }
    }
    
    fun processQueuedMessages() {
        if (networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()) {
            messageQueueService.processQueue()
        }
    }

    fun hasQueuedMessages(): Boolean {
        return messageQueueService.hasQueuedMessages()
    }
}