package com.shivam.piechatapp.domain.usecase

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.repository.WebSocketRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(message: ChatMessage) {
        conversationRepository.addMessage(message)
        webSocketRepository.sendMessage(message)
    }
} 