package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApi {

    @GET(NetworkUtils.SUB_URL_COMMENT_INFO)
    fun getCommentInfo(
        @Path(NetworkUtils.PATH_COMMENT_LINK, encoded = true) commentLink: String,
        @Path(NetworkUtils.PATH_COMMENT_LINK_ID, encoded = true) linkId: String,
    ): Call<ResponseBody>

    @POST(NetworkUtils.SUB_URL_COMMENT_VOTE)
    fun voteComment(
        @Query(NetworkUtils.PATH_COMMENT_LINK_ID) linkId: String,
        @Query(NetworkUtils.QUERY_COMMENT_DIR) dir: Int,
    ): Call<ResponseBody>

    @POST(NetworkUtils.SUB_URL_COMMENT_ADD)
    fun addComment(
        @Query(NetworkUtils.QUERY_API_TYPE) apiType: String,
        @Query(NetworkUtils.QUERY_TEXT) text: String,
        @Query(NetworkUtils.QUERY_PARENT) parent: String,
    ): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_USER_COMMENT_LIST)
    fun getUserCommentList(
        @Path(NetworkUtils.PATH_FRIEND_NAME, encoded = true) userName: String,
    ): Call<ResponseBody>
}