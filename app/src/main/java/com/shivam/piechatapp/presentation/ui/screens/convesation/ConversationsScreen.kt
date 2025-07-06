package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shivam.piechatapp.presentation.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    onConversationClick: (String) -> Unit,
    viewModel: ConversationsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Conversations")
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
    }
} 