package com.example.reddit.model

import com.example.reddit.model.subreddit.SubredditListing

data class CommentListing(val listing: SubredditListing, val commentList: List<Comment>)