package com.shivam.piechatapp.data.repository

import com.piesocket.channels.Channel
import com.piesocket.channels.PieSocket
import com.piesocket.channels.misc.PieSocketEvent
import com.piesocket.channels.misc.PieSocketEventListener
import com.piesocket.channels.misc.PieSocketOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.util.Date
import java.util.UUID
import android.util.Log
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.repository.WebSocketRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.apply
import kotlin.collections.toMutableList
import kotlin.let

@Singleton
class PieSocketWebSocketRepository @Inject constructor(
    private val conversationRepository: ConversationRepository
) : WebSocketRepository {
    
    private var pieSocket: PieSocket? = null
    private var channel: Channel? = null

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    private val messagesFlow: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    companion object {
        private const val TAG = "PieSocketWebSocketRepo"
        private const val CLUSTER_ID = "s14881.blr1"
        private const val API_KEY = "XuGHlUqVmqBDlHxSP9Sn5hXKGCD6DohwYe62IxE9"
        private const val ROOM_ID = "chat-shivam"
    }

    override fun getMessages(): Flow<List<ChatMessage>> = messagesFlow
    
    override fun connect() {
        Log.d(TAG, "Connecting to PieSocket...")
        try {
            val options = PieSocketOptions().apply {
                clusterId = CLUSTER_ID
                apiKey = API_KEY
            }

            options.setPresence(true)
            options.userId = "androidshivam"

            pieSocket = PieSocket(options)
            channel = pieSocket?.join(ROOM_ID)

       /*     channel!!.listen("new-message", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                    Log.d(TAG, "New message received: " + event.getData())
                    val members = channel!!.allMembers
                    Log.d(TAG, "members: " + members.toString())
                }
            })*/

            setupEventListeners()
            
        } catch (e: Exception) {
        }
    }
    
    override fun disconnect() {
        try {
            channel?.disconnect()
            pieSocket = null
            channel = null
        } catch (e: Exception) {
        }
    }
    
    override fun sendMessage(message: ChatMessage): Result<Unit> {
        return try {
            if (!isConnected()) {
                return Result.failure(Exception("Not connected"))
            }
            
            val event = PieSocketEvent("new-message")
            val data = JSONObject().apply {
                put("user_name", message.userName)
                put("message", message.message)
            }
            event.data = data.toString()
            
            channel?.publish(event)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun isConnected(): Boolean {
        return true
    }
    
    private fun setupEventListeners() {
        channel?.let { ch ->
            ch.listen("system:connected", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                    val newMessage = PieSocketEvent("new-message")
                    newMessage.setData("Hello!")

                    ch.publish(newMessage)
                }
            })
            
            ch.listen("system:disconnected", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                }
            })
            
            ch.listen("on_incoming_message", object : PieSocketEventListener() {
                override fun handleEvent(event: PieSocketEvent) {
                    try {
                        val data = JSONObject(event.data)
                        val userName = data.optString("user_name")
                        val message = data.optString("message")
                        
                        val chatMessage = ChatMessage(
                            id = UUID.randomUUID().toString(),
                            userName = userName,
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