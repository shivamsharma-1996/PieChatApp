package com.shivam.piechatapp.domain.usecase

import com.shivam.piechatapp.domain.model.ConnectionStatus
import com.shivam.piechatapp.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConnectionStatusUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(): Flow<ConnectionStatus> {
        return webSocketRepository.getConnectionStatus()
    }
} 