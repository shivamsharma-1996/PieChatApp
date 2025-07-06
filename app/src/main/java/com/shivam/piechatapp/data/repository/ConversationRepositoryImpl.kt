package com.shivam.piechatapp.data.repository

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor() : ConversationRepository {
    override fun getConversations(): Flow<List<Conversation>> {
        TODO("Not yet implemented")
    }

    override fun addMessage(message: ChatMessage) {
        TODO("Not yet implemented")
    }

    override fun markConversationAsRead(userName: String) {
        TODO("Not yet implemented")
    }

}