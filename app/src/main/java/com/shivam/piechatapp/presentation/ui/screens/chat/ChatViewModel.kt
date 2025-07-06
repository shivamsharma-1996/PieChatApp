package com.shivam.piechatapp.presentation.ui.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    conversationRepository: ConversationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userName: String = savedStateHandle["userName"] ?: "Unknown"
    val messages: List<ChatMessage> = conversationRepository.getMessagesForUser(userName)
} 