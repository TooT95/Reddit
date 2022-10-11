package com.example.reddit.model.subreddit

sealed class SubredditListing {

    abstract val id: String
    abstract val url: String
    abstract var saved: Boolean
    abstract val title: String
    abstract val numComments: Long
    abstract val author: String
    abstract val created: Double
    abstract val fullName: String

    data class ListingVideo(
        override val id: String,
        override val url: String,
        override var saved: Boolean,
        override val title: String,
        override val numComments: Long,
        override val author: String,
        override val created: Double,
        val videoUrl: String,
        override val fullName: String,
    ) :
        SubredditListing()

    data class ListingPost(
        override val id: String,
        override val url: String,
        override var saved: Boolean,
        override val title: String,
        override val numComments: Long,
        val selfText: String,
        override val created: Double,
        override val author: String,
        override val fullName: String,
    ) :
        SubredditListing()

    data class ListingImage(
        override val id: String,
        override val url: String,
        override var saved: Boolean,
        override val title: String,
        override val numComments: Long,
        override val author: String,
        override val created: Double,
        val imageUrl: String,
        override val fullName: String,
    ) :
        SubredditListing()

    companion object {
        const val COL_ID = "id"
        const val COL_URL = "url"
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
        const val COL_SAVED = "saved"
        const val COL_FULL_NAME = "name"
    }
}