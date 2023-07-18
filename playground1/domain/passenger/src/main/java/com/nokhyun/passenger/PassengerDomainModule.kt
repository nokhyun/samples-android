package com.nokhyun.passenger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PassengerDomainModule {

    @Provides
    @Singleton
    fun provideFakePagingPassengerUseCase(fakePagingRepository: FakePagingRepository) = FakePagingPassengerUseCase(fakePagingRepository)
}