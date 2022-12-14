package com.example.reddit.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.reddit.model.Account
import okhttp3.*
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue

object Utils {

    fun accessTokenValid(context: Context): Boolean {
        val queue = LinkedBlockingQueue<Boolean>()

        Thread {
            val client = OkHttpClient().newBuilder()
                .build()
            val request = Request.Builder()
                .url("https://oauth.reddit.com/api/me")
                .get()
                .addHeader("Authorization",
                    "bearer ${getSharedPrefAccessToken(context)}")
                .build()
            val response = client.newCall(request).execute()
            queue.add(response.isSuccessful)
        }.start()

        return queue.take()
    }

    private const val ONBOARD_VALUE_KEY = "onboard passed"
    private const val AUTH_CODE_KEY = "auth code key"
    private const val TOKEN_KEY = "token key"
    private const val REDIRECT_URI = "http://www.example.com/unused/redirect/uri"
    const val APP_SHARED_PREF_KEY = "app shared pref"
    const val STATE = "MY_RANDOM_STRING_1"
    const val BASE_URL = "https://oauth.reddit.com"
    const val CLIENT_ID = "DuckTiShJSX-1lEC17rBRA"
    const val CLIENT_SECRET = ""
    private var ACCESS_TOKEN = ""
    const val AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
            "&response_type=code&state=%s&redirect_uri=%s&" +
            "duration=permanent&scope=identity read account mysubreddits subscribe history vote save submit privatemessages"
    val oAuthUrl = String.format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI)

    const val COL_DATA_API = "data"
    const val COL_JSON_API = "json"
    const val COL_THINGS_API = "things"
    const val COL_CHILDREN_API = "children"

    var SUB_AFTER = ""
    var SUB_LISTING_AFTER = ""
    var account: Account? = null

    fun onboardPassed(context: Context): Boolean {
        return context.getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .getBoolean(ONBOARD_VALUE_KEY, false)
    }

    fun saveOnboardPassed(context: Context, value: Boolean) {
        context.saveField {
            putBoolean(ONBOARD_VALUE_KEY, value)
        }
    }

    fun setAuthCode(context: Context, authCode: String) {
        context.saveField {
            putString(AUTH_CODE_KEY, authCode)
        }
    }

    fun getAuthCode(context: Context): String {
        return context.getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .getString(AUTH_CODE_KEY, "") ?: ""
    }

    private fun getSharedPrefAccessToken(context: Context): String {
        ACCESS_TOKEN = context.getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .getString(TOKEN_KEY, "") ?: ""
        return ACCESS_TOKEN
    }

    fun setAccessToken(context: Context, token: String) {
        ACCESS_TOKEN = token
        context.saveField {
            putString(TOKEN_KEY, token)
        }
    }

    fun getAccessToken(): String {
        return ACCESS_TOKEN
    }

    private fun Context.saveField(
        saveFunction: SharedPreferences.Editor.() -> Unit,
    ) {
        getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .apply {
                saveFunction()
            }.apply()
    }
}