package com.shivam.piechatapp.presentation.ui.screens.convesation

import androidx.lifecycle.ViewModel
import com.shivam.piechatapp.domain.usecase.ConnectWebSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val connectWebSocketUseCase: ConnectWebSocketUseCase
) : ViewModel() {

    fun connect() {
        connectWebSocketUseCase()
    }
}