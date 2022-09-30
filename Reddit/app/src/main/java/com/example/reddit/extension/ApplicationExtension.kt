package com.example.reddit.utils

import android.content.Context
import android.content.SharedPreferences

fun Context.saveField(
    saveFunction: SharedPreferences.Editor.() -> Unit
) {
    getSharedPreferences(Utils.APP_SHARED_PREF_KEY, Context.MODE_PRIVATE)
        .edit()
        .apply {
            saveFunction()
        }.apply()
}