package com.example.reddit.network

import com.example.reddit.model.AuthResponse
import com.example.reddit.utils.Utils
import okhttp3.MediaType
import retrofit2.http.*

interface TokenApi {

    @Multipart
    @POST(Utils.TOKEN_BASE_URL)
    suspend fun getAuthToken(
        @Header("Content-Type") contentType: MediaType,
        @Part("code") code: String,
        @Part("grant_type") grant_type: String,
        @Part("redirect_uri") redirect_uri: String,
    ): AuthResponse

}