package com.shivam.piechatapp.domain.service.messagequeue

import com.shivam.piechatapp.data.messagequeue.MessageQueue
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageQueueServiceImpl @Inject constructor(
    private val messageQueue: MessageQueue,
    private val socketRepository: PieSocketWebSocketRepository
) : MessageQueueService {

    override fun queueMessage(message: ChatMessage) {
        messageQueue.enqueue(message)
    }

    override fun processQueue() {
        var shouldContinue = true
        while (messageQueue.hasQueuedMessages() && shouldContinue) {
            val message = messageQueue.dequeue()
            message?.let {
                // Try to send the queued message
                val result = socketRepository.sendMessage(it)
                if (result.isSuccess) {
                    // Message sent successfully, update status to SENT
                    // TODO - update the message status in the repository
                } else {
                    // If sending fails, put it back in the queue and stop processing
                    messageQueue.enqueue(it)
                    shouldContinue = false
                }
            }
        }
    }

    override fun getQueueSize(): Int = messageQueue.size()

    override fun hasQueuedMessages(): Boolean = messageQueue.hasQueuedMessages()
}