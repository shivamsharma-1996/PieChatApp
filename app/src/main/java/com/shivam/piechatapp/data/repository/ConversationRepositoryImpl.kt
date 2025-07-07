package com.shivam.piechatapp.data.repository

import com.shivam.piechatapp.Constants
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.domain.model.MessageStatus
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
                unreadCount = if (message.senderName != Constants.LOGGED_IN_USER_ID) {
                    existingConversation.unreadCount + 1
                } else {
                    existingConversation.unreadCount
                },
                messages = updatedMessages
            )

            currentConversations[existingConversationIndex] = updatedConversation
        } else {
            val newConversation = Conversation(
                partner = conversationPartner,
                lastMessage = message.message,
                lastMessageTimestamp = message.timestamp,
                unreadCount = if (message.senderName != Constants.LOGGED_IN_USER_ID) 1 else 0,
                messages = listOf(message)
            )

            currentConversations.add(newConversation)
        }

        // Sort conversations by latest message timestamp
        currentConversations.sortByDescending { it.lastMessageTimestamp }

        _conversations.value = currentConversations
    }

    override fun markConversationAsRead(userName: String) {
        val currentConversations = _conversations.value.toMutableList()
        val conversationIndex = currentConversations.indexOfFirst { it.partner == userName }

        if (conversationIndex != -1) {
            val conversation = currentConversations[conversationIndex]
            val updatedConversation = conversation.copy(unreadCount = 0)
            currentConversations[conversationIndex] = updatedConversation
            _conversations.value = currentConversations
        }
    }

    override fun getMessagesForUser(userName: String): Flow<List<ChatMessage>> {
        return conversationsFlow.map { conversations ->
            conversations.firstOrNull { it.partner == userName }?.messages ?: emptyList()
        }
    }

    override fun updateMessageStatus(messageId: String, newStatus: MessageStatus) {
        val currentConversations = _conversations.value.toMutableList()

        for (conversationIndex in currentConversations.indices) {
            val conversation = currentConversations[conversationIndex]
            val messageIndex = conversation.messages.indexOfFirst { it.id == messageId }

            if (messageIndex != -1) {
                val message = conversation.messages[messageIndex]
                val updatedMessage = message.copy(status = newStatus)
                val updatedMessages = conversation.messages.toMutableList()
                updatedMessages[messageIndex] = updatedMessage

                val updatedConversation = conversation.copy(messages = updatedMessages)
                currentConversations[conversationIndex] = updatedConversation
                break
            }
        }

        _conversations.value = currentConversations
    }
}