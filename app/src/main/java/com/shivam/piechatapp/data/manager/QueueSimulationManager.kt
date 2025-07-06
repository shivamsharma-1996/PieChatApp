package com.shivam.piechatapp.data.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueSimulationManager @Inject constructor() {
    private val _queueMode = MutableStateFlow(false)
    val queueMode: StateFlow<Boolean> = _queueMode.asStateFlow()

    fun setQueueMode(enabled: Boolean) {
        _queueMode.value = enabled
    }
} 