package com.shivam.piechatapp.data.repository

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor() : ConversationRepository {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    private val conversationsFlow = _conversations.asStateFlow()

    override fun getConversations(): Flow<List<Conversation>> = conversationsFlow

    override fun addMessage(message: ChatMessage) {
        val currentConversations = _conversations.value.toMutableList()

        val existingConversationIndex = currentConversations.indexOfFirst {
            it.userName == message.userName
        }

        if (existingConversationIndex != -1) {
            val existingConversation = currentConversations[existingConversationIndex]
            val updatedMessages = existingConversation.messages + message

            val updatedConversation = existingConversation.copy(
                lastMessage = message.message,
                lastMessageTimestamp = message.timestamp,
                unreadCount = existingConversation.unreadCount + 1,
                messages = updatedMessages
            )

            currentConversations[existingConversationIndex] = updatedConversation
        } else {
            val newConversation = Conversation(
                userName = message.userName,
                lastMessage = message.message,
                lastMessageTimestamp = message.timestamp,
                unreadCount = 1,
                messages = listOf(message)
            )

            currentConversations.add(newConversation)
        }

        _conversations.value = currentConversations
    }

    override fun markConversationAsRead(userName: String) {
        TODO("Not yet implemented")

    }

    override fun getMessagesForUser(userName: String): List<ChatMessage> {
        return _conversations.value.firstOrNull { it.userName == userName }?.messages ?: emptyList()
    }
}