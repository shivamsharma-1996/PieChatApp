package com.shivam.piechatapp.data.messagequeue

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.MessageStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageQueue @Inject constructor() {
    private val _queuedMessages = mutableListOf<ChatMessage>()
    
    fun enqueue(message: ChatMessage) {
        _queuedMessages.add(message.copy(status = MessageStatus.QUEUED))
    }
    
    fun dequeue(): ChatMessage? {
        return if (_queuedMessages.isNotEmpty()) {
            _queuedMessages.removeAt(0)
        } else null
    }
    
    fun getQueuedMessages(): List<ChatMessage> = _queuedMessages.toList()
    
    fun clear() {
        _queuedMessages.clear()
    }
    
    fun hasQueuedMessages(): Boolean = _queuedMessages.isNotEmpty()
    
    fun size(): Int = _queuedMessages.size
}