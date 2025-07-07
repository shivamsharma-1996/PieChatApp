package com.shivam.piechatapp.utils

import android.util.Log
import javax.inject.Inject

interface Logger {
    fun d(tag: String = DEFAULT_TAG, message: String)
    fun i(tag: String = DEFAULT_TAG, message: String)
    fun w(tag: String = DEFAULT_TAG, message: String)
    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null)

    companion object {
        const val DEFAULT_TAG = "PieChatApp"
    }
}

class AndroidLogger @Inject constructor() : Logger {
    private val ENABLE = true

    override fun d(tag: String, message: String) {
        if (ENABLE) Log.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        if (ENABLE) Log.i(tag, message)
    }

    override fun w(tag: String, message: String) {
        if (ENABLE) Log.w(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (ENABLE) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }
} 