package com.shivam.piechatapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shivam.piechatapp.Constants
import com.shivam.piechatapp.presentation.ui.screens.chat.ChatScreen
import com.shivam.piechatapp.presentation.ui.screens.convesation.ConversationsScreen

@Composable
fun PieChatNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Conversations.route,
        modifier = modifier
    ) {
        composable(Screen.Conversations.route) {
            ConversationsScreen(
                onConversationClick = { convoPartnerName ->
                    navController.navigate(Screen.Chat.createRoute(convoPartnerName))
                }
            )
        }
        composable(Screen.Chat.route) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString(Constants.KEY_CONVO_PARTNER_NAME) ?: "Unknown"
            ChatScreen(
                userName = userName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
} 