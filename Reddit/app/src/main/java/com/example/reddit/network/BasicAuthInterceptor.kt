package com.example.reddit.network

import com.example.reddit.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Authorization", "bearer ${Utils.getAccessToken()}")
            .build()
        return chain.proceed(request)
    }
}