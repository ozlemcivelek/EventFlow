package com.example.eventflow.database.di

import com.example.eventflow.database.repository.AuthRepository
import com.example.eventflow.database.repository.AuthRepositoryImpl
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.database.repository.EventRepositoryImpl
import com.example.eventflow.database.repository.ServiceRepository
import com.example.eventflow.database.repository.ServiceRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideEventRepository(): EventRepository = EventRepositoryImpl()


    @Provides
    fun provideServiceRepository(): ServiceRepository = ServiceRepositoryImpl()
}