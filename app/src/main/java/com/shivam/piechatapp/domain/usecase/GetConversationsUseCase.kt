package com.shivam.piechatapp.domain.usecase

import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    operator fun invoke(): Flow<List<Conversation>> {
        return conversationRepository.getConversations()
    }
}