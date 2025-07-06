package com.shivam.piechatapp.domain.repository

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.domain.model.MessageStatus
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversations(): Flow<List<Conversation>>
    fun addMessage(message: ChatMessage)
    fun markConversationAsRead(userName: String)
    fun getMessagesForUser(userName: String): Flow<List<ChatMessage>>
    fun updateMessageStatus(messageId: String, newStatus: MessageStatus)
}