package com.example.reddit.model.subreddit

data class Subreddit(
    val id:String,
    val displayName: String,
    val title: String,
    val userSubscriber: Boolean,
    val name: String,
) {

    companion object {
        const val COL_ID = "id"
        const val COL_DISPLAY_NAME = "display_name"
        const val COL_USER_SUBSCRIBER = "user_is_subscriber"
        const val COL_TITLE = "title"
        const val COL_NAME = "name"
    }

}