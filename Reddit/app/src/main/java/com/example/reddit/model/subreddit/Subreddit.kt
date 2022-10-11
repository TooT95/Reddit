package com.example.reddit.model.subreddit

data class Subreddit(
    val id: String,
    val displayName: String,
    val title: String,
    var userSubscriber: Boolean,
    val name: String,
    val headerImage: String,
    val dNamePrefixed: String,
) {

    companion object {
        const val COL_ID = "id"
        const val COL_DISPLAY_NAME = "display_name"
        const val COL_USER_SUBSCRIBER = "user_is_subscriber"
        const val COL_TITLE = "title"
        const val COL_NAME = "name"
        const val COL_HEADER_IMAGE = "mobile_banner_image"
        const val COL_NAME_PREFIXED = "display_name_prefixed"

    }

}