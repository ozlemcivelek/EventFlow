package com.example.eventflow.database.di

import android.content.Context
import android.content.SharedPreferences
import com.example.eventflow.database.repository.AuthRepository
import com.example.eventflow.database.repository.AuthRepositoryImpl
import com.example.eventflow.database.repository.CustomerRepository
import com.example.eventflow.database.repository.CustomerRepositoryImpl
import com.example.eventflow.database.repository.EventRepository
import com.example.eventflow.database.repository.EventRepositoryImpl
import com.example.eventflow.database.repository.ServiceRepository
import com.example.eventflow.database.repository.ServiceRepositoryImpl
import com.example.eventflow.database.usecase.GetEventUseCase
import com.example.eventflow.database.usecase.GetReservationsUseCase
import com.example.eventflow.database.usecase.ScheduleNotificationUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideEventRepository(firebaseAuth: FirebaseAuth): EventRepository =
        EventRepositoryImpl(firebaseAuth)

    @Provides
    fun provideServiceRepository(firebaseAuth: FirebaseAuth): ServiceRepository =
        ServiceRepositoryImpl(firebaseAuth)

    @Provides
    fun provideCustomerRepository(firebaseAuth: FirebaseAuth): CustomerRepository =
        CustomerRepositoryImpl(firebaseAuth)

    @Provides
    fun provideReservationUseCase(
        eventRepository: EventRepository,
        customerRepository: CustomerRepository,
    ): GetReservationsUseCase =
        GetReservationsUseCase(eventRepository, customerRepository)

    @Provides
    fun provideEventUseCase(
        eventRepository: EventRepository,
        customerRepository: CustomerRepository,
    ): GetEventUseCase =
        GetEventUseCase(eventRepository, customerRepository)

    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    @Provides
    fun provideScheduleNotificationUseCase(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences,
    ) = ScheduleNotificationUseCase(context, sharedPreferences)
}