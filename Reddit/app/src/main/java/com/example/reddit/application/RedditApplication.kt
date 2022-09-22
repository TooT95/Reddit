package com.example.reddit.application

import android.app.Application
import com.example.reddit.BuildConfig
import timber.log.Timber

class RedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}