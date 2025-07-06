package com.shivam.piechatapp.domain.usecase

import com.shivam.piechatapp.data.handler.MessageHandler
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val messageHandler: MessageHandler
) {
    suspend operator fun invoke(message: ChatMessage) {
        val processedMessage = messageHandler.sendMessage(message)

        conversationRepository.addMessage(processedMessage)
    }
}