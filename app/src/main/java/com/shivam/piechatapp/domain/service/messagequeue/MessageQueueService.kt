package com.shivam.piechatapp.domain.service.messagequeue

import com.shivam.piechatapp.domain.model.ChatMessage

interface MessageQueueService {
    fun queueMessage(message: ChatMessage)
    fun processQueue()
    fun getQueueSize(): Int
    fun hasQueuedMessages(): Boolean
}