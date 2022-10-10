package com.example.reddit.model.subreddit

sealed class SubredditListing {

    data class ListingVideo(
        val id: String,
        val title: String,
        val commentsNum: Long,
        val author: String,
        val created: Double,
        val videoUrl: String,
    ) :
        SubredditListing()

    data class ListingPost(
        val id: String,
        val title: String,
        val numComments: Long,
        val selfText: String,
        val created: Double,
        val author: String,
    ) :
        SubredditListing()

    data class ListingImage(
        val id: String,
        val title: String,
        val numComments: Long,
        val author: String,
        val created: Double,
        val url: String,
    ) :
        SubredditListing()

    companion object {
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_COMMENTS_NUM = "num_comments"
        const val COL_AUTHOR = "author"
        const val COL_CREATED = "created"
        const val COL_MEDIA = "media"
        const val COL_REDDIT_MEDIA = "reddit_video"
        const val COL_VIDEO_URL = "fallback_url"
        const val COL_SELF_TEXT = "selftext"
        const val COL_IMAGE_URl = "thumbnail"
        const val COL_IS_VIDEO = "is_video"
    }
}