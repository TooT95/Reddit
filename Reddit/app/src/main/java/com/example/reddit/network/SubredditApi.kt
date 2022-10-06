package com.example.reddit.network

import retrofit2.http.GET
import retrofit2.http.Query

interface SubredditApi {

    @GET("/new.json?")
    suspend fun getNewList(
        @Query("after") after: String?,
        @Query("limit") limit: Int,
        @Query("sr_detail") srDetail: Int,
    ): SubWrapper

}