package com.shivam.piechatapp.data.repository

import com.shivam.piechatapp.Constants
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor() : ConversationRepository {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    private val conversationsFlow = _conversations.asStateFlow()

    override fun getConversations(): Flow<List<Conversation>> = conversationsFlow

    override fun addMessage(message: ChatMessage) {
        val currentConversations = _conversations.value.toMutableList()

        val conversationPartner = if (message.senderName == Constants.LOGGED_IN_USER_ID) {
            message.receiverName
        } else {
            message.senderName
        }

        val existingConversationIndex = currentConversations.indexOfFirst {
            it.partner == conversationPartner
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
                partner = conversationPartner,
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

    override fun getMessagesForUser(userName: String): Flow<List<ChatMessage>> {
        return conversationsFlow.map { conversations ->
            conversations.firstOrNull { it.partner == userName }?.messages ?: emptyList()
        }
    }
}