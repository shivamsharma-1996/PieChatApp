package com.shivam.piechatapp.domain.model

import java.util.Date

data class ChatMessage(
    val id: String = "",
    val userName: String,
    val message: String,
    val timestamp: Date = Date(),
    val isRead: Boolean = false
) 