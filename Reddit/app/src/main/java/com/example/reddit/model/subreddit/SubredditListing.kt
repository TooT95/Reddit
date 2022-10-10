package com.example.reddit.model.subreddit

sealed class SubredditListing {

    abstract val id: String
    abstract val score: Int

    data class ListingVideo(
        override val id: String,
        val title: String,
        val numComments: Long,
        val author: String,
        val created: Double,
        val videoUrl: String,
        override val score: Int,
    ) :
        SubredditListing()

    data class ListingPost(
        override val id: String,
        val title: String,
        val numComments: Long,
        val selfText: String,
        val created: Double,
        val author: String,
        override val score: Int,
    ) :
        SubredditListing()

    data class ListingImage(
        override val id: String,
        val title: String,
        val numComments: Long,
        val author: String,
        val created: Double,
        val imageUrl: String,
        override val score: Int,
        val imageWidth: Int,
        val imageHeight: Int,
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
        const val COL_IMAGE_URl_COMMON = "url_overridden_by_dest"
        const val COL_IS_VIDEO = "is_video"
        const val COL_SCORE = "score"
        const val COL_IMAGE_WIDTH = "thumbnail_width"
        const val COL_IMAGE_HEIGHT = "thumbnail_height"
    }
}