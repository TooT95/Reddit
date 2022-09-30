package com.example.reddit.network

import com.example.reddit.model.AuthBody
import com.example.reddit.model.AuthResponse
import com.example.reddit.utils.Utils
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenApi {

    @FormUrlEncoded
    @POST(Utils.TOKEN_BASE_URL)
    suspend fun getAuthToken(@Body accessTokenBody: AuthBody): AuthResponse

}