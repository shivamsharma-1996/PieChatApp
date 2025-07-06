package com.shivam.piechatapp.data.handler

import com.shivam.piechatapp.data.network.NetworkStatusManager
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.MessageStatus
import com.shivam.piechatapp.domain.service.messagequeue.MessageQueueService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageHandler @Inject constructor(
    private val networkStatusManager: NetworkStatusManager,
    private val socketRepository: PieSocketWebSocketRepository,
    private val messageQueueService: MessageQueueService
) {
    
    fun sendMessage(message: ChatMessage): ChatMessage {
        // Check both internet connectivity and socket connection
        val canSend = networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()
        
        return if (canSend) {
            // Try to send immediately
            val result = socketRepository.sendMessage(message)
            if (result.isSuccess) {
                message.copy(status = MessageStatus.SENT)
            } else {
                // If sending fails, queue the message
                messageQueueService.queueMessage(message)
                message.copy(status = MessageStatus.QUEUED)
            }
        } else {
            // Queue the message if conditions are not met
            messageQueueService.queueMessage(message)
            message.copy(status = MessageStatus.QUEUED)
        }
    }
    
    fun processQueuedMessages() {
        // Only process queue if both conditions are met
        if (networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()) {
            messageQueueService.processQueue()
        }
    }
    
    fun isReadyToSend(): Boolean {
        return networkStatusManager.isNetworkAvailableNow() && socketRepository.isConnected()
    }
    
    fun hasQueuedMessages(): Boolean {
        return messageQueueService.hasQueuedMessages()
    }
    
    fun getQueueSize(): Int {
        return messageQueueService.getQueueSize()
    }
}