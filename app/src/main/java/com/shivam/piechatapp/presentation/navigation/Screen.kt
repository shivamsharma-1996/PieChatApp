package com.shivam.piechatapp.presentation.navigation

import com.shivam.piechatapp.Constants

sealed class Screen(val route: String) {
    object Conversations : Screen("conversations")
    object Chat : Screen("chat/{${Constants.KEY_CONVO_PARTNER_NAME}}") {
        fun createRoute(conversationPartnerName: String) = "chat/$conversationPartnerName"
    }
} 