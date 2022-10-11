package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SubredditApi {

    @GET(NetworkUtils.PATH_SUB_NEW)
    fun getNewList(@Query(NetworkUtils.QUERY_AFTER) after: String?): Call<ResponseBody>

    @GET(NetworkUtils.PATH_SUB_POPULAR)
    fun getPopularList(@Query(NetworkUtils.QUERY_AFTER) after: String?): Call<ResponseBody>

    @GET(NetworkUtils.PATH_SUB_SEARCH)
    fun searchSubreddit(
        @Query(NetworkUtils.QUERY_AFTER) after: String?,
        @Query(NetworkUtils.QUERY_SEARCH) q: String,
    ): Call<ResponseBody>

    @POST(NetworkUtils.PATH_SUBREDDIT_SUBSCRIBE)
    fun subUnsubSubreddit(
        @Query(NetworkUtils.QUERY_SUBREDDIT_PREFIXED_NAME) dName: String,
        @Query(NetworkUtils.QUERY_ACTION) action: String?,
    ): Call<ResponseBody>
}