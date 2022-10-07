package com.example.reddit.model.subreddit

data class Subreddit(val displayName: String, val userSubscriber: Boolean, val name: String) {

    companion object{
        const val COL_DISPLAY_NAME = "display_name"
        const val COL_USER_SUBSCRIBER = "user_is_subscriber"
        const val COL_NAME = "name"
    }

}