package com.example.reddit.repository

import android.app.Application
import android.util.Base64
import com.example.reddit.network.SubredditApi
import com.example.reddit.utils.Utils
import okhttp3.*
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val application: Application,
) {
    suspend fun authTokenGot(): Boolean {
        val response = getToken().execute()
        if (response.isSuccessful) {
            val jsonData: String = response.body!!.string()
            val accessToken = JSONObject(jsonData).getString("access_token")
            Utils.setAccessToken(application, accessToken)
            Timber.d("accessToken $accessToken")
            return true
        }
        return false
    }

    private fun getToken(): Call {
        val authString = Utils.CLIENT_ID + ":" + Utils.CLIENT_SECRET
        val encodedAuthString = Base64.encodeToString(authString.toByteArray(),
            Base64.NO_WRAP)
        val client = OkHttpClient().newBuilder()
            .build()
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("code", Utils.getAuthCode(application))
            .addFormDataPart("grant_type", "authorization_code")
            .addFormDataPart("redirect_uri", "http://www.example.com/unused/redirect/uri")
            .build()
        val request = Request.Builder()
            .url("https://www.reddit.com/api/v1/access_token")
            .method("POST", body)
            .addHeader("Authorization",
                "Basic $encodedAuthString")
            .build()
        return client.newCall(request)
    }

}