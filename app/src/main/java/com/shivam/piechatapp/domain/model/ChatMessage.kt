package com.shivam.piechatapp.domain.model

import java.util.Date

data class ChatMessage(
    val id: String = "",
    val senderName: String,
    val receiverName: String,
    val message: String,
    val timestamp: Date = Date(),
    val isRead: Boolean = false,
    val status: MessageStatus = MessageStatus.SENDING
) 