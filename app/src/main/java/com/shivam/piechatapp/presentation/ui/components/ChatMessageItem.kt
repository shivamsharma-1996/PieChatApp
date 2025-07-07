package com.shivam.piechatapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivam.piechatapp.domain.model.ChatMessage
import com.shivam.piechatapp.domain.model.MessageStatus
import com.shivam.piechatapp.utils.format

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    isLocal: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLocal) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isLocal) Alignment.End else Alignment.Start
        ) {
            Text(
                text = message.message,
                modifier = Modifier
                    .background(
                        color = if (isLocal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                color = if (isLocal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isLocal) Arrangement.End else Arrangement.Start
            ) {
                Text(
                    text = message.timestamp.format("hh:mm a"),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, end = 4.dp, start = 4.dp)
                )
                if (isLocal && message.status == MessageStatus.QUEUED) {
                    Text(
                        text = "Queued",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 2.dp, end = 4.dp, start = 4.dp)
                    )
                }
            }
        }
    }
}