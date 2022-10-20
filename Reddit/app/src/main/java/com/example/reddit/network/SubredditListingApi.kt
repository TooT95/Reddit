package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SubredditListingApi {

    @GET(NetworkContract.SUB_URL_SUB_HOT)
    fun getSrListingList(
        @Path(NetworkContract.QUERY_SUBREDDIT_FILTR) srName: String,
        @Query(NetworkContract.QUERY_LIMIT) limit: Int,
        @Query(NetworkContract.QUERY_AFTER) after: String?,
        @Query(NetworkContract.QUERY_SUBREDDIT_DETAIL) sr_detail: Int,
    ): Call<ResponseBody>

    @POST(NetworkContract.SUB_URL_SAVE_UN_SAVE)
    fun saveUnSavePost(
        @Path(NetworkContract.PATH_SAVE_ACTION) saveUnsave: String,
        @Query(NetworkContract.QUERY_ID) id: String,
        @Query(NetworkContract.QUERY_CATEGORY) category: String,
    ): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_SAVED_LIST)
    fun getSavedList(
        @Path(NetworkContract.PATH_FRIEND_NAME) accountName: String,
    ): Call<ResponseBody>

}