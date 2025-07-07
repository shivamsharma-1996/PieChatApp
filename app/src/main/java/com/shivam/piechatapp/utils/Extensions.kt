package com.shivam.piechatapp.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(pattern: String = "MMM dd, hh:mm a"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

suspend fun SnackbarHostState.showSuccessSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showSnackbar(
        message = message,
        actionLabel = "SUCCESS", // We'll use this as a flag
        duration = duration
    )
}

suspend fun SnackbarHostState.showErrorSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    showSnackbar(
        message = message,
        actionLabel = "ERROR",
        duration = duration
    )
}

