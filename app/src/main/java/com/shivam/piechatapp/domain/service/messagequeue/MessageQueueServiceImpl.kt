package com.shivam.piechatapp.domain.service.messagequeue

import com.shivam.piechatapp.data.messagequeue.MessageQueue
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.MessageStatus
import com.shivam.piechatapp.domain.repository.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageQueueServiceImpl @Inject constructor(
    private val messageQueue: MessageQueue,
    private val socketRepository: PieSocketWebSocketRepository,
    private val conversationRepository: ConversationRepository
) : MessageQueueService {

    override fun queueMessage(message: ChatMessage) {
        messageQueue.enqueue(message)
    }

    override fun processQueue() {
        var shouldContinue = true
        while (messageQueue.hasQueuedMessages() && shouldContinue) {
            val message = messageQueue.dequeue()
            message?.let {
                val result = socketRepository.sendMessage(it) // Try to send the queued message
                if (result.isSuccess) {
                    conversationRepository.updateMessageStatus(it.id, MessageStatus.SENT)
                } else {
                    messageQueue.enqueue(it)  // put it back in the queue and stop processing
                    shouldContinue = false
                }
            }
        }
    }

    override fun getQueueSize(): Int = messageQueue.size()

    override fun hasQueuedMessages(): Boolean = messageQueue.hasQueuedMessages()
}