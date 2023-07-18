package com.nokhyun.fakepaging

import com.nokhyun.network.RetrofitFactory
import com.nokhyun.network.ServiceFactory
import com.nokhyun.network.ServiceFactory_Factory
import com.nokhyun.passenger.FakePagingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FakePagingModule {

    @Provides
    @Singleton
    fun provideFakePagingService(serviceFactory: ServiceFactory) = serviceFactory.create("https://api.instantwebtools.net", FakePagingService::class.java)

    @Provides
    @Singleton
    fun provideFakePagingRepository(fakePagingRemoteDataSource: FakePagingRemoteDataSource): FakePagingRepository = FakePagingRepositoryImpl(fakePagingRemoteDataSource)

    @Provides
    @Singleton
    fun provideFakePagingRemoteDataSource(service: FakePagingService): FakePagingRemoteDataSource = FakePagingRemoteDataSourceImpl(service)
}