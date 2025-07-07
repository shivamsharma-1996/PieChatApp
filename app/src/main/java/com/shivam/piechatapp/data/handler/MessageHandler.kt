package com.shivam.piechatapp.data.handler

import com.shivam.piechatapp.data.manager.QueueSimulationManager
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
    private val networkAlertManager: NetworkAlertManager,
    private val queueSimulationManager: QueueSimulationManager
) {

    fun sendMessage(message: ChatMessage): ChatMessage {
        val queueMode = queueSimulationManager.queueMode.value

        // Check both internet connectivity and socket connection
        val canSend = networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()

        return when {
            queueMode -> {
                // Always queue in simulation mode
                queueAndNotify(message)
            }

            canSend -> {
                val result = socketRepository.sendMessage(message) // Try to send immediately
                if (result.isSuccess) {
                    message.copy(status = MessageStatus.SENT)
                } else {
                    // If sending fails, queue the message
                    queueAndNotify(message)
                }
            }

            else -> {
                // Queue the message if conditions are not met
                queueAndNotify(message)
            }
        }
    }

    fun processQueuedMessages() {
        val queueMode = queueSimulationManager.queueMode.value
        if (!queueMode && networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()) {
            // Process queued messages when socket is connected and internet is available
            messageQueueService.processQueue()
        }
    }

    fun hasQueuedMessages(): Boolean {
        return messageQueueService.hasQueuedMessages()
    }

    private fun queueAndNotify(message: ChatMessage): ChatMessage {
        messageQueueService.queueMessage(message)

        // Show alert for queued message
        networkAlertManager.showMessageQueuedAlert()

        // Clear alert after delay, but if still offline, show no internet alert
        networkAlertManager.clearAlertAfterDelay(3000) {
            if (!networkStatusManager.isNetworkAvailableNow()) {
                networkAlertManager.showNoInternetAlert()
            }
        }

        return message.copy(status = MessageStatus.QUEUED)
    }
}