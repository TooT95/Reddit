package com.example.reddit.utils

import android.content.Context

object Utils {

    private const val ONBOARD_VALUE_KEY = "onboard passed"
    const val APP_SHARED_PREF_KEY = "app shared pref"
    private const val AUTH_CODE_KEY = "auth code key"
    private const val BASE_URL = "https://www.reddit.com/api/v1"
    private const val CLIENT_ID = "gnpcyrAKl8k8AMR6fVjeGg"
    private const val REDIRECT_URI = "http://www.example.com/unused/redirect/uri"
    private const val TOKEN_BASE_URL = "/access_token"
    private const val AUTH_BASE_URL = "/authorize"

    const val oAuthUrl =
        "${BASE_URL + AUTH_BASE_URL}?client_id=$CLIENT_ID&response_type=code&state=MobileAppReddit&redirect_uri=$REDIRECT_URI&duration=temporary&scope=identity"
    const val tokenBaseUrl = BASE_URL + TOKEN_BASE_URL

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

}