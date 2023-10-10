package com.nokhyun.fakepaging

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//@Qualifier
//annotation class FakePaging

@Module
@InstallIn(SingletonComponent::class)
object FakePagingModule {

//    @Provides
//    @Singleton
//    @FakePaging
//    fun provideFakePagingService(serviceFactory: ServiceFactory): FakePagingService = serviceFactory.create("https://api.instantwebtools.net", FakePagingService::class.java)
}