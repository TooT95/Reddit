package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SubredditApi {

    @GET("/new.json?")
    fun getNewList(
        @Query("after") after: String?,
        @Query("limit") limit: Int,
        @Query("sr_detail") srDetail: Int,
    ): Call<ResponseBody>

}