package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SubredditListingApi {

    @GET(NetworkUtils.PATH_SUB_HOT)
    fun getSrListingList(
        @Path(NetworkUtils.QUERY_SUBREDDIT_FILTR) srName: String,
        @Query(NetworkUtils.QUERY_LIMIT) limit: Int,
        @Query(NetworkUtils.QUERY_AFTER) after: String?,
        @Query(NetworkUtils.QUERY_SUBREDDIT_DETAIL) sr_detail: Int,
    ): Call<ResponseBody>

    @POST(NetworkUtils.PATH_SAVE_UNSAVE)
    fun saveUnsavePost(
        @Path(NetworkUtils.PATH_SAVE_ACTION) saveUnsave: String,
        @Query(NetworkUtils.QUERY_ID) id: String,
        @Query(NetworkUtils.QUERY_CATEGORY) category: String,
    ): Call<ResponseBody>

}