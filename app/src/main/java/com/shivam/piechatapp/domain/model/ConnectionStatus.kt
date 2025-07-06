package com.shivam.piechatapp.domain.model

sealed class ConnectionStatus {
    object Connecting : ConnectionStatus()
    object Connected : ConnectionStatus()
    object Disconnected : ConnectionStatus()
    object Error : ConnectionStatus()
} 