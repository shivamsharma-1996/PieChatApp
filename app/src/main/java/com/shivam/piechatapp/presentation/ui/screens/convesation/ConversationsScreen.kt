package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shivam.piechatapp.presentation.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    onConversationClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Conversations")
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
    }
} 