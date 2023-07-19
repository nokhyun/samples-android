package com.nokhyun.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class OtherInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }
}