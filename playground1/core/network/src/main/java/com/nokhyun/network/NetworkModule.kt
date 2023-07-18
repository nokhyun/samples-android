package com.nokhyun.network

import com.nokhyun.network.annotations.OtherInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideServiceFactory(okHttpClient: OkHttpClient) = ServiceFactory(okHttpClient)

    @Provides
    @Singleton
    fun provideOtherInterceptor() = com.nokhyun.network.interceptors.OtherInterceptor()

    @Provides
    @Singleton
    fun provideFakeService(serviceFactory: ServiceFactory) = serviceFactory.create("https://jsonplaceholder.typicode.com/", FakeService::class.java)

    @OtherInterceptor
    @Provides
    @Singleton
    fun provideOtherInterceptorOkHttpClient(
        otherInterceptor: com.nokhyun.network.interceptors.OtherInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(otherInterceptor)
        .build()
}