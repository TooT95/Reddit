package com.example.reddit.repository

import android.app.Application
import com.example.reddit.network.AccessToken
import com.example.reddit.utils.Utils
import org.json.JSONObject
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val application: Application,
) {
    suspend fun authTokenGot(): Boolean {
        val response = AccessToken.getLL(Utils.getAuthCode(application)).execute()
        if (response.isSuccessful) {
            val jsonData: String = response.body()!!.string()
            Utils.setAccessToken(application, JSONObject(jsonData).getString("access_token"))
            return true
        }
        return false
    }


}