package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SubredditApi {

    @GET(NetworkUtils.SUB_URL_SUB_NEW)
    fun getNewList(@Query(NetworkUtils.QUERY_AFTER) after: String?): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_SUB_POPULAR)
    fun getPopularList(@Query(NetworkUtils.QUERY_AFTER) after: String?): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_SUB_SEARCH)
    fun searchSubreddit(
        @Query(NetworkUtils.QUERY_AFTER) after: String?,
        @Query(NetworkUtils.QUERY_SEARCH) q: String,
    ): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_SUBREDDIT_SUBSCRIBER)
    fun getSubscribedList(@Query(NetworkUtils.QUERY_AFTER) after: String?): Call<ResponseBody>

    @POST(NetworkUtils.SUB_URL_SUBREDDIT_SUBSCRIBE)
    fun subUnSubSubreddit(
        @Query(NetworkUtils.QUERY_SUBREDDIT_PREFIXED_NAME) dName: String,
        @Query(NetworkUtils.QUERY_ACTION) action: String?,
    ): Call<ResponseBody>

}