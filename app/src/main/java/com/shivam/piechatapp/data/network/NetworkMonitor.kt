package com.shivam.piechatapp.data.network

import com.shivam.piechatapp.data.handler.MessageHandler
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// Encapsulates the network monitoring
@Singleton
class NetworkMonitor @Inject constructor(
    private val networkStatusManager: NetworkStatusManager,
    private val messageHandler: MessageHandler,
    private val socketRepository: PieSocketWebSocketRepository
) {

    fun startMonitoring() {
        networkStatusManager.startNetworkMonitoring()

        // Observe network status changes
        CoroutineScope(Dispatchers.IO).launch {
            networkStatusManager.networkStatusFlow.collectLatest { isOnline ->
                if (isOnline) {
                    // Reconnect PieSocket when back online
                    socketRepository.connect()
                }
            }
        }

        // Observe socket connection status changes
        CoroutineScope(Dispatchers.IO).launch {
            socketRepository.getConnectionStatus().collectLatest { status ->
                if (status == ConnectionStatus.Connected && messageHandler.hasQueuedMessages()) {
                    // Process queued messages when socket connects
                    messageHandler.processQueuedMessages()
                }
            }
        }

        fun stopMonitoring() {
            networkStatusManager.stopNetworkMonitoring()
        }
    }
}