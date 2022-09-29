package com.example.reddit.utils

import android.content.Context

object Utils {

    private const val ONBOARD_VALUE_KEY = "onboard passed"
    const val APP_SHARED_PREF_KEY = "app shared pref"
    private const val AUTH_CODE_KEY = "auth code key"

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