package com.shivam.piechatapp.presentation.ui.components.alerts.network

import android.R.attr.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkAlertManager @Inject constructor() {

    private val _alertState = MutableStateFlow<NetworkAlertState>(NetworkAlertState.Hidden)
    val alertState: StateFlow<NetworkAlertState> = _alertState.asStateFlow()

    fun showNoInternetAlert() {
        _alertState.value = NetworkAlertState.NoInternet()
    }

    fun showBackOnlineAlert() {
        _alertState.value = NetworkAlertState.BackOnline()
    }

    fun showMessageQueuedAlert() {
        _alertState.value = NetworkAlertState.MessageQueued()
    }

    fun hideAlert() {
        _alertState.value = NetworkAlertState.Hidden
    }
} 