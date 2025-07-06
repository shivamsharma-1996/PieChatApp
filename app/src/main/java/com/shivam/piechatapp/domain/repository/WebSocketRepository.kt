package com.shivam.piechatapp.domain.repository

import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.ConnectionStatus
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun getConnectionStatus(): Flow<ConnectionStatus>
    fun getMessages(): Flow<List<ChatMessage>>
    fun connect()
    fun disconnect()
    fun sendMessage(message: ChatMessage): Result<Unit>
    fun isConnected(): Boolean
} 