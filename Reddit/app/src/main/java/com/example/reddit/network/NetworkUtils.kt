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

    const val SUB_URL_SUB_NEW = "/subreddits/new"
    const val SUB_URL_SUB_POPULAR = "/subreddits/popular"
    const val SUB_URL_SUBREDDIT_SUBSCRIBER = "/subreddits/mine/subscriber"
    const val SUB_URL_ME = "/api/me"
    const val SUB_URL_COMMENT_INFO = "{commentlink}{id}"
    const val SUB_URL_COMMENT_VOTE = "/api/vote"
    const val SUB_URL_FRIEND_LIST = "/api/v1/me/friends"
    const val SUB_URL_FRIEND_FOLLOW_UN_FOLLOW = "/api/v1/me/friends/{friendName}"
    const val SUB_URL_FRIEND_INFO = "/user/{friendName}/about"
    const val SUB_URL_SAVED_LIST = "/user/{friendName}/saved"
    const val SUB_URL_USER_COMMENT_LIST = "/user/{friendName}/comments"
    const val SUB_URL_SUB_SEARCH = "/subreddits/search"

    const val SUB_URL_SUB_HOT = "/r/{sr}/hot.json"
    const val SUB_URL_SUBREDDIT_SUBSCRIBE = "/api/subscribe.json?"

    const val SUB_URL_SAVE_UN_SAVE = "/api/{action}?"

    const val PATH_SAVE_ACTION = "action"
    const val PATH_FRIEND_NAME = "friendName"
    const val PATH_COMMENT_LINK = "commentlink"
    const val PATH_COMMENT_LINK_ID = "id"

    const val QUERY_COMMENT_DIR = "dir"
}