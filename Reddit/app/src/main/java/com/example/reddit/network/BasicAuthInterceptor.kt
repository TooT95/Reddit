package com.example.reddit.network

import com.example.reddit.utils.Utils
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {
    private var credentials: String = Credentials.basic(Utils.CLIENT_ID, Utils.CLIENT_SECRET)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)

    }
}