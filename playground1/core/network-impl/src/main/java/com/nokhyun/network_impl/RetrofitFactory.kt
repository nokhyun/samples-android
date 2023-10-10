package com.nokhyun.network_impl

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

interface RetrofitFactory {
    abstract class Provider {
        protected val contentType = "application/json".toMediaType()
        abstract fun <R> create(baseUrl: String, klass: Class<R>): R
    }
}

class ServiceFactory @Inject constructor(
    private val okHttpClient: OkHttpClient
) : RetrofitFactory.Provider() {
    override fun <R> create(baseUrl: String, klass: Class<R>): R {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(klass)
    }
}