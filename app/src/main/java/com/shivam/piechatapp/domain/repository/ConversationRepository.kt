package com.shivam.piechatapp.domain.repository

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversations(): Flow<List<Conversation>>
    fun addMessage(message: ChatMessage)
    fun markConversationAsRead(userName: String)
    fun getMessagesForUser(userName: String): List<ChatMessage>
}