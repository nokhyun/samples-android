package com.nokhyun.network_impl

import com.nokhyun.network.FakePagingService
import com.nokhyun.network.FakeService
import com.nokhyun.network.annotations.FakePaging
import com.nokhyun.network.annotations.OtherInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideServiceFactory(okHttpClient: OkHttpClient) = ServiceFactory(okHttpClient)

    @OtherInterceptor
    @Provides
    @Singleton
    fun provideOtherInterceptor() = com.nokhyun.network_impl.interceptors.OtherInterceptor()

    @Provides
    @Singleton
    fun provideFakeService(serviceFactory: ServiceFactory) = serviceFactory.create("https://jsonplaceholder.typicode.com/", FakeService::class.java)

    @Provides
    @Singleton
    @FakePaging
    fun provideFakePagingService(serviceFactory: ServiceFactory): FakePagingService = serviceFactory.create("https://api.instantwebtools.net", FakePagingService::class.java)

    @Provides
    @Singleton
    fun provideOtherInterceptorOkHttpClient(
        @OtherInterceptor otherInterceptor: com.nokhyun.network_impl.interceptors.OtherInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .addInterceptor(otherInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()
}