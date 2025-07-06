package com.shivam.piechatapp

import android.app.Application
import com.shivam.piechatapp.data.network.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PieChatApplication : Application() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate() {
        super.onCreate()
        // Start network monitoring
        networkMonitor.startMonitoring()
    }
}