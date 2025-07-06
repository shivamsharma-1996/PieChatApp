package com.shivam.piechatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PieChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}