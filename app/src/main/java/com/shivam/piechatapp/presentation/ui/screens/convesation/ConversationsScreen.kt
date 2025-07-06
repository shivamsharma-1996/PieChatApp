package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shivam.piechatapp.domain.model.ConnectionStatus
import com.shivam.piechatapp.presentation.ui.components.AppTopBar
import com.shivam.piechatapp.presentation.ui.components.ConversationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    onConversationClick: (String) -> Unit,
    viewModel: ConversationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.connectionStatus) {
        when (uiState.connectionStatus) {
            is ConnectionStatus.Connecting -> {
                snackbarHostState.showSnackbar("Making a connection, please wait...")
            }
            is ConnectionStatus.Connected -> {
                snackbarHostState.showSnackbar("Connected to chat server")
            }
            is ConnectionStatus.Disconnected -> {
                snackbarHostState.showSnackbar("Lost connection to chat server")
            }
            is ConnectionStatus.Error -> {
                snackbarHostState.showSnackbar("Connection error. Please check your internet.")
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Conversations")
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(16.dp))
                        Text(
                            text = "Making a connection, please wait...",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                uiState.conversations.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        Text(
                            text = "No conversations available",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.conversations) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                onClick = {
                                    onConversationClick(conversation.userName)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}