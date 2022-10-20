package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApi {

    @GET(NetworkContract.SUB_URL_COMMENT_INFO)
    fun getCommentInfo(
        @Path(NetworkContract.PATH_COMMENT_LINK, encoded = true) commentLink: String,
        @Path(NetworkContract.PATH_COMMENT_LINK_ID, encoded = true) linkId: String,
    ): Call<ResponseBody>

    @POST(NetworkContract.SUB_URL_COMMENT_VOTE)
    fun voteComment(
        @Query(NetworkContract.PATH_COMMENT_LINK_ID) linkId: String,
        @Query(NetworkContract.QUERY_COMMENT_DIR) dir: Int,
    ): Call<ResponseBody>

    @POST(NetworkContract.SUB_URL_COMMENT_ADD)
    fun addComment(
        @Query(NetworkContract.QUERY_API_TYPE) apiType: String,
        @Query(NetworkContract.QUERY_TEXT) text: String,
        @Query(NetworkContract.QUERY_PARENT) parent: String,
    ): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_USER_COMMENT_LIST)
    fun getUserCommentList(
        @Path(NetworkContract.PATH_FRIEND_NAME, encoded = true) userName: String,
    ): Call<ResponseBody>
}