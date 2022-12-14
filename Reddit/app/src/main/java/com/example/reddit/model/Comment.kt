package com.example.reddit.model

import com.example.reddit.network.CommentApi

data class Comment(
    val id: String,
    val body: String,
    val date: Double,
    val author: String,
    var score: Int,
    var likes: Boolean?,
    val commentLink: String,
    val replyCount: Int,
    val linkId: String,
    var commentOwner: Comment? = null,
) {


    companion object {
        const val COL_ID = "id"
        const val COL_BODY = "body"
        const val COL_DATE = "created"
        const val COL_AUTHOR = "author"
        const val COL_SCORE = "score"
        const val COL_LIKES = "likes"
        const val COL_REPLY_COMMENT_LINK = "permalink"
        const val COL_REPLY_COUNT = "replies"
        const val COL_LINK_ID = "name"
    }
}
