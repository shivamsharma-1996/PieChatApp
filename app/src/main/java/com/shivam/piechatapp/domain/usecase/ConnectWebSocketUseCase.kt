package com.shivam.piechatapp.domain.usecase

import com.shivam.piechatapp.domain.repository.WebSocketRepository
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke() {
        webSocketRepository.connect()
    }
} 