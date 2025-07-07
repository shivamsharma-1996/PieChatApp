package com.shivam.piechatapp.data.repository

import com.piesocket.channels.Channel
import com.piesocket.channels.PieSocket
import com.piesocket.channels.misc.PieSocketEvent
import com.piesocket.channels.misc.PieSocketEventListener
import com.piesocket.channels.misc.PieSocketOptions
import com.shivam.piechatapp.Constants
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.ConnectionStatus
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.repository.WebSocketRepository
import com.shivam.piechatapp.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PieSocketWebSocketRepository @Inject constructor(
    private val logger: Logger,
    private val conversationRepository: ConversationRepository
) : WebSocketRepository {
    
    private var pieSocket: PieSocket? = null
    private var channel: Channel? = null

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Disconnected)
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    private val connectionStatusFlow: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()
    private val messagesFlow: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    companion object {
        private const val TAG = "PieSocketWebSocketRepo"
        private const val CLUSTER_ID = "s14881.blr1"
        private const val API_KEY = "XuGHlUqVmqBDlHxSP9Sn5hXKGCD6DohwYe62IxE9"
        private const val ROOM_ID = "chat-shivam"
    }

    override fun getConnectionStatus(): Flow<ConnectionStatus> = connectionStatusFlow

    override fun getMessages(): Flow<List<ChatMessage>> = messagesFlow
    
    override fun connect() {
        logger.d(TAG, "Connecting to PieSocket...")
        try {
            _connectionStatus.value = ConnectionStatus.Connecting

            val options = PieSocketOptions().apply {
                clusterId = CLUSTER_ID
                apiKey = API_KEY
            }

            options.setPresence(true)
            options.setNotifySelf(false)

            options.userId = Constants.LOGGED_IN_USER_ID

            pieSocket = PieSocket(options)
            channel = pieSocket?.join(ROOM_ID)

            setupEventListeners()
            
        } catch (e: Exception) {
            _connectionStatus.value = ConnectionStatus.Error
        }
    }
    
    override fun disconnect() {
        try {
            channel?.disconnect()
            pieSocket = null
            channel = null
            _connectionStatus.value = ConnectionStatus.Disconnected
        } catch (e: Exception) {
            _connectionStatus.value = ConnectionStatus.Error
        }
    }
    
    override fun sendMessage(message: ChatMessage): Result<Unit> {
        return try {
            if (!isConnected()) {
                return Result.failure(Exception("Not connected"))
            }
            
            val event = PieSocketEvent("new-message")
            val data = JSONObject().apply {
                put(Constants.KEY_SENDER_NAME, message.senderName)
                put(Constants.KEY_MESSAGE, message.message)
            }
            event.data = data.toString()
            
            channel?.publish(event)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun isConnected(): Boolean {
        return _connectionStatus.value == ConnectionStatus.Connected
    }
    
    private fun setupEventListeners() {
        channel?.let { ch ->
            ch.listen("system:connected", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                    logger.d(TAG, "Connected to PieSocket Room: $event")

                    _connectionStatus.value = ConnectionStatus.Connected
                }
            })
            
            ch.listen("system:disconnected", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                    logger.d(TAG, "Disconnected to PieSocket Room: $event")

                    _connectionStatus.value = ConnectionStatus.Disconnected
                }
            })
            
            ch.listen("new-message", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                    try {
                        val data = JSONObject(event.data)
                        val senderName = data.optString(Constants.KEY_SENDER_NAME)
                        val message = data.optString(Constants.KEY_MESSAGE)

                        if (senderName == Constants.LOGGED_IN_USER_ID) {
                            // skip the message and sender and receiver's username can't be same
                            return;
                        }

                        val chatMessage = ChatMessage(
                            id = UUID.randomUUID().toString(),
                            senderName = senderName,
                            receiverName = Constants.LOGGED_IN_USER_ID,
                            message = message,
                            timestamp = Date()
                        )
                        
                        val currentMessages = _messages.value.toMutableList()
                        currentMessages.add(chatMessage)
                        _messages.value = currentMessages
                        
                        conversationRepository.addMessage(chatMessage)
                        
                    } catch (e: Exception) {
                    }
                }
            })
        }
    }
} 