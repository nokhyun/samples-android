package com.nokhyun.fakepaging

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindFakePagingRemoteDataSource(
        fakePagingRemoteDateSourceImpl: FakePagingRemoteDataSourceImpl
    ): FakePagingRemoteDataSource
}