package com.shivam.piechatapp.data.network

import com.shivam.piechatapp.data.handler.MessageHandler
import com.shivam.piechatapp.data.manager.QueueSimulationManager
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.model.ConnectionStatus
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertManager
import com.shivam.piechatapp.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// Encapsulates the network monitoring
@Singleton
class NetworkMonitor @Inject constructor(
    private val logger: Logger,
    private val networkStatusManager: NetworkStatusManager,
    private val messageHandler: MessageHandler,
    private val socketRepository: PieSocketWebSocketRepository,
    private val networkAlertManager: NetworkAlertManager,
    private val queueSimulationManager: QueueSimulationManager
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var isFirstStatusCheck = true

    fun startMonitoring() {
        networkStatusManager.startNetworkMonitoring()
        observeNetworkStatus()
        observeSocketStatus()
    }

    fun stopMonitoring() {
        networkStatusManager.stopNetworkMonitoring()
    }

    private fun observeNetworkStatus() = scope.launch {
        networkStatusManager.networkStatusFlow.collectLatest { isOnline ->
            if (isOnline) handleOnlineStatus()
            else handleOfflineStatus()
        }
    }

    private fun observeSocketStatus() = scope.launch {
        socketRepository.getConnectionStatus().collectLatest { status ->
            val queueMode = queueSimulationManager.queueMode.value

            if (status == ConnectionStatus.Connected && !queueMode && messageHandler.hasQueuedMessages()) {
                // Process queued messages when socket connects and not in queue mode
                logger.d(message = "Socket connected - processing queued messages")
                messageHandler.processQueuedMessages()
            }
        }
    }

    private fun handleOnlineStatus() {
        if (!isFirstStatusCheck) {
            networkAlertManager.hideAlert {
                networkAlertManager.showBackOnlineAlert(
                    messageHandler.hasQueuedMessages(),
                    queueSimulationManager.queueMode.value
                )
                networkAlertManager.clearAlertAfterDelay()
            }
        }
        isFirstStatusCheck = false

        socketRepository.disconnect() // invalidate the socket connection
        socketRepository.connect() // fresh socket connect
    }

    private fun handleOfflineStatus() {
        if (isFirstStatusCheck) isFirstStatusCheck = false
        networkAlertManager.showNoInternetAlert()
    }
}
