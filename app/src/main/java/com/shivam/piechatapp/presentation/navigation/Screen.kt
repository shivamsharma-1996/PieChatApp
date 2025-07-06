package com.shivam.piechatapp.presentation.navigation

sealed class Screen(val route: String) {
    object Conversations : Screen("conversations")
    object Chat : Screen("chat/{userName}") {
        fun createRoute(userName: String) = "chat/$userName"
    }
} 