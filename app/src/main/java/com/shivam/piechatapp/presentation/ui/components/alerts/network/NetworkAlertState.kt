package com.shivam.piechatapp.presentation.ui.components.alerts.network

sealed class NetworkAlertState {
    object Hidden : NetworkAlertState()
    
    data class NoInternet(
        val message: String = "No internet connection"
    ) : NetworkAlertState()
    
    data class BackOnline(
        val message: String = "Device is back online",
    ) : NetworkAlertState()
    
    data class MessageQueued(
        val message: String = "Message has been queued, retrying later when internet is back"
    ) : NetworkAlertState()
}