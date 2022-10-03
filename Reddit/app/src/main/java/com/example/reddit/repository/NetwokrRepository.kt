package com.example.reddit.repository

import android.app.Application
import android.util.Log
import com.example.reddit.model.AuthResponse
import com.example.reddit.network.AccessToken
import com.example.reddit.network.TokenApi
import com.example.reddit.utils.Utils
import okhttp3.MultipartBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class NetwokrRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val application: Application,
) {

    suspend fun getAuthToken(): AuthResponse {
//        val authBody = AuthBody()
//        val response = AccessToken.getLL(Utils.getAuthCode(application)).execute()
//        if (response.isSuccessful) {
//            val jsonData: String = response.body()!!.string()
//            val jobject = JSONObject(jsonData)
//            Timber.d("object: ${jobject.get("access_token")}")
//        }
        return tokenApi.getAuthToken(MultipartBody.FORM, Utils.getAuthCode(application),
            "authorization_code",
            Utils.REDIRECT_URI)
    }


}