package com.shivam.piechatapp.domain.model

import java.util.Date

data class Conversation(
    val userName: String,
    val lastMessage: String,
    val lastMessageTimestamp: Date,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
) 