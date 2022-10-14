package com.example.reddit.network

object NetworkUtils {

    const val QUERY_AFTER = "after"
    const val QUERY_SEARCH = "q"
    const val QUERY_SUBREDDIT_DETAIL = "sr_detail"
    const val QUERY_SUBREDDIT_FILTR = "sr"
    const val QUERY_SUBREDDIT_PREFIXED_NAME = "sr_name"
    const val QUERY_LIMIT = "limit"
    const val QUERY_ACTION = "action"
    const val QUERY_ID = "id"
    const val QUERY_CATEGORY = "category"

    const val PATH_SUB_NEW = "/subreddits/new"
    const val PATH_SUB_POPULAR = "/subreddits/popular"
    const val PATH_SUBREDDIT_SUBSCRIBER = "/subreddits/mine/subscriber"
    const val PATH_ME = "/api/me"
    const val PATH_FRIEND_LIST = "/api/v1/me/friends"
    const val PATH_FRIEND_INFO = "/user/{friendName}/about"
    const val PATH_SUB_SEARCH = "/subreddits/search"

    const val PATH_SUB_HOT = "/r/{sr}/hot.json"
    const val PATH_SUBREDDIT_SUBSCRIBE = "/api/subscribe.json?"

    const val PATH_SAVE_UNSAVE = "/api/{action}?"

    const val PATH_SAVE_ACTION = "action"
    const val PATH_FRIEND_NAME = "friendName"

}