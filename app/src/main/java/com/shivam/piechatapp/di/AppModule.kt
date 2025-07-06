package com.shivam.piechatapp.di

import com.shivam.piechatapp.data.repository.ConversationRepositoryImpl
import com.shivam.piechatapp.data.repository.PieSocketWebSocketRepository
import com.shivam.piechatapp.domain.repository.ConversationRepository
import com.shivam.piechatapp.domain.repository.WebSocketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindWebSocketRepository(
        pieSocketWebSocketRepository: PieSocketWebSocketRepository
    ): WebSocketRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(
        conversationRepositoryImpl: ConversationRepositoryImpl
    ): ConversationRepository
} 