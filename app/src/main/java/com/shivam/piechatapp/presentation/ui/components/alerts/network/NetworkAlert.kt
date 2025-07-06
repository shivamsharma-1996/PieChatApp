package com.shivam.piechatapp.presentation.ui.components.alerts.network

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shivam.piechatapp.R

@Composable
fun NetworkAlert(
    alertState: NetworkAlertState,
    modifier: Modifier = Modifier
) {
    when (alertState) {
        is NetworkAlertState.Hidden -> {
            // Don't show anything
        }
        is NetworkAlertState.NoInternet -> {
            NetworkAlertContent(
                message = alertState.message,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                painter = R.drawable.ic_no_internet,
                modifier = modifier
            )
        }
        is NetworkAlertState.BackOnline -> {
            val backgroundColor = if (alertState.hasQueuedMessages) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val textColor = if (alertState.hasQueuedMessages) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            NetworkAlertContent(
                message = alertState.message,
                backgroundColor = backgroundColor,
                textColor = textColor,
                painter = R.drawable.ic_internet_back,
                modifier = modifier
            )
        }
        is NetworkAlertState.MessageQueued -> {
            NetworkAlertContent(
                message = alertState.message,
                backgroundColor = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.onError,
                painter = R.drawable.ic_message_queue,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun NetworkAlertContent(
    message: String,
    backgroundColor: Color,
    textColor: Color,
    painter: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = painter),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = message,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
} 