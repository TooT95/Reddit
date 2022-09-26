package com.example.reddit.utils

import android.content.Context

object Utils {

    private const val ONBOARD_VALUE_KEY = "onboard passed"
    private const val APP_SHARED_PREF_KEY = "app shared pref"

    fun onboardPassed(context: Context): Boolean {
        return context.getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .getBoolean(ONBOARD_VALUE_KEY, false)
    }

    fun saveOnboardPassed(context: Context, value: Boolean) {
        context.getSharedPreferences(APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(ONBOARD_VALUE_KEY, value)
            .apply()
    }

}