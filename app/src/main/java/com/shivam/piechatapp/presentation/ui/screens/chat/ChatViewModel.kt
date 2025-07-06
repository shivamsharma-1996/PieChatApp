package com.shivam.piechatapp.presentation.ui.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    conversationRepository: ConversationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userName: String = savedStateHandle["userName"] ?: "Unknown"
    val messages: Flow<List<ChatMessage>> = conversationRepository.getMessagesForUser(userName)
} 