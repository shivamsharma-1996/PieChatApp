package com.shivam.piechatapp.presentation.ui.components.alerts.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkAlertManager @Inject constructor() {

    private val _alertState = MutableStateFlow<NetworkAlertState>(NetworkAlertState.Hidden)
    val alertState: StateFlow<NetworkAlertState> = _alertState.asStateFlow()

    fun showNoInternetAlert() {
        _alertState.value = NetworkAlertState.NoInternet()
    }

    fun showBackOnlineAlert(hasQueuedMessages: Boolean, queueMode: Boolean) {
        val message = if (hasQueuedMessages) {
             if (queueMode) {
                 "Device is back online, turn off the Queue mode to send the queued msgs!"
             } else {
                 "Device is back online, sending queued messages!"
             }
        } else {
            "Device is back online"
        }
        _alertState.value = NetworkAlertState.BackOnline(message, hasQueuedMessages)
    }

    fun showMessageQueuedAlert() {
        _alertState.value = NetworkAlertState.MessageQueued()
    }

    fun hideAlert(onHide: (() -> Unit)? = null) {
        _alertState.value = NetworkAlertState.Hidden
        onHide?.invoke()
    }

    fun clearAlertAfterDelay(delayMillis: Long = 3000, onCleared: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.Main).launch {
            kotlinx.coroutines.delay(delayMillis)
            hideAlert()
            onCleared?.invoke()
        }
    }
} 