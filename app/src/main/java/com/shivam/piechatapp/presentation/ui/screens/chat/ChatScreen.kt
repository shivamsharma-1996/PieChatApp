package com.shivam.piechatapp.presentation.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shivam.piechatapp.presentation.ui.components.AppTopBar
import com.shivam.piechatapp.presentation.ui.components.ChatMessageItem
import com.shivam.piechatapp.presentation.ui.components.QueueModeToggle
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlert
import com.shivam.piechatapp.presentation.ui.components.alerts.network.NetworkAlertState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userName: String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val (message, setMessage) = remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState(emptyList())
    val networkAlertState by viewModel.networkAlertState.collectAsState(NetworkAlertState.Hidden)
    val queueMode by viewModel.queueMode.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Always scroll to bottom when messages change
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = userName,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().imePadding(),
                verticalArrangement = Arrangement.Bottom
            ) {
                NetworkAlert(
                    alertState = networkAlertState,
                    modifier = Modifier.fillMaxWidth()
                )

                Divider()

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(messages) { msg ->
                        ChatMessageItem(
                            message = msg,
                            isLocal = msg.senderName != viewModel.conversationPartnerName
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = message,
                        onValueChange = setMessage,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...") }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(
                        onClick = {
                            viewModel.sendMessage(message)
                            setMessage("")

                            coroutineScope.launch {
                                if (messages.isNotEmpty()) {
                                    listState.animateScrollToItem(messages.lastIndex)
                                }
                            }
                        },
                        enabled = message.isNotBlank()
                    ) {
                        Text("Send")
                    }
                }
                QueueModeToggle(
                    isQueueModeEnabled = queueMode,
                    onToggle = { viewModel.setQueueMode(it) }
                )
            }
        }
    }
}