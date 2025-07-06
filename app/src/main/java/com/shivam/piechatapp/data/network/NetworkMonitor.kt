package com.shivam.piechatapp.data.network

import javax.inject.Inject
import javax.inject.Singleton

// Encapsulates the network monitoring
@Singleton
class NetworkMonitor @Inject constructor(
    private val networkStatusManager: NetworkStatusManager,
) {

    fun startMonitoring() {
        networkStatusManager.startNetworkMonitoring()

        // TODO - Observe two things - network status changes 2. socket connection status changes
    }

    fun stopMonitoring() {
        networkStatusManager.stopNetworkMonitoring()
    }
}