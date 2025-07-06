package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivam.piechatapp.domain.model.Conversation
import com.shivam.piechatapp.presentation.ui.components.AppTopBar
import com.shivam.piechatapp.presentation.ui.components.ConversationItem
import java.util.Date
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    onConversationClick: (String) -> Unit
) {
    val dummyConversations = listOf(
        Conversation(userName = "Test1", lastMessage = "Hey, how are you", lastMessageTimestamp = Date()),
        Conversation(userName = "Test2", lastMessage = "Bye", lastMessageTimestamp = Date())
    )

    Scaffold(
        topBar = {
            AppTopBar(title = "Conversations")
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            items(dummyConversations) { conversation ->
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
