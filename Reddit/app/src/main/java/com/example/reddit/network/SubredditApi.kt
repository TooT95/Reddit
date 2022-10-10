package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SubredditApi {

    @GET("/subreddits/new")
    fun getNewList(): Call<ResponseBody>

    @GET("/subreddits/popular")
    fun getPopularList(): Call<ResponseBody>

    @GET("/r/{sr}/hot.json")
    fun getSrListingList(
        @Path("sr") srName: String,
        @Query("limit") limit: Int,
        @Query("after") after: String?,
        @Query("sr_detail") sr_detail: Int,
    ): Call<ResponseBody>

}