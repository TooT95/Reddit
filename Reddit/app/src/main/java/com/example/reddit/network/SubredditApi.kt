package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SubredditApi {

    @GET(NetworkContract.SUB_URL_SUB_NEW)
    fun getNewList(@Query(NetworkContract.QUERY_AFTER) after: String?): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_SUB_POPULAR)
    fun getPopularList(@Query(NetworkContract.QUERY_AFTER) after: String?): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_SUB_SEARCH)
    fun searchSubreddit(
        @Query(NetworkContract.QUERY_AFTER) after: String?,
        @Query(NetworkContract.QUERY_SEARCH) q: String,
    ): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_SUBREDDIT_SUBSCRIBER)
    fun getSubscribedList(@Query(NetworkContract.QUERY_AFTER) after: String?): Call<ResponseBody>

    @POST(NetworkContract.SUB_URL_SUBREDDIT_SUBSCRIBE)
    fun subUnSubSubreddit(
        @Query(NetworkContract.QUERY_SUBREDDIT_PREFIXED_NAME) dName: String,
        @Query(NetworkContract.QUERY_ACTION) action: String?,
    ): Call<ResponseBody>

}