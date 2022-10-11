package com.example.reddit.network

object NetworkUtils {

    const val QUERY_AFTER = "after"
    const val QUERY_SUBREDDIT_DETAIL = "sr_detail"
    const val QUERY_SUBREDDIT_FILTR = "sr"
    const val QUERY_SUBREDDIT_PREFIXED_NAME = "sr_name"
    const val QUERY_LIMIT = "limit"
    const val QUERY_ACTION = "action"

    const val PATH_SUB_NEW = "/subreddits/new"
    const val PATH_SUB_POPULAR = "/subreddits/popular"

    const val PATH_SUB_HOT = "/r/{sr}/hot.json"
    const val PATH_SUBREDDIT_SUBSCRIBE = "/api/subscribe.json?"

}