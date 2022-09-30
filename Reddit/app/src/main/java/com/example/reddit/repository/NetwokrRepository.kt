package com.example.reddit.repository

import android.app.Application
import android.content.Context
import com.example.reddit.model.AuthBody
import com.example.reddit.model.AuthResponse
import com.example.reddit.network.TokenApi
import com.example.reddit.utils.Utils
import javax.inject.Inject

class NetwokrRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val application: Application
) {

    suspend fun getAuthToken(): AuthResponse {
        val authBody =
            AuthBody(Utils.getAuthCode(application), "authorization_code", Utils.REDIRECT_URI)
        return tokenApi.getAuthToken(authBody)
    }


}