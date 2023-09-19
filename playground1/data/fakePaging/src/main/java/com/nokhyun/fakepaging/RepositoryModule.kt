package com.nokhyun.fakepaging

import com.nokhyun.passenger.FakePagingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFakePagingRepository(
        fakePagingRepo: FakePagingRepositoryImpl
    ): FakePagingRepository
}