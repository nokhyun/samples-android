package com.nokhyun.fakepaging

import com.nokhyun.network.RetrofitFactory
import com.nokhyun.network.ServiceFactory
import com.nokhyun.network.ServiceFactory_Factory
import com.nokhyun.passenger.FakePagingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class FakePaging

@Module
@InstallIn(SingletonComponent::class)
internal object FakePagingModule {

    @Provides
    @Singleton
    @FakePaging
    fun provideFakePagingService(serviceFactory: ServiceFactory): FakePagingService = serviceFactory.create("https://api.instantwebtools.net", FakePagingService::class.java)
}