package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentApi {

    @GET(NetworkUtils.SUB_URL_COMMENT_INFO)
    fun getCommentInfo(
        @Path(NetworkUtils.PATH_COMMENT_LINK, encoded = true) commentLink: String,
        @Path(NetworkUtils.PATH_COMMENT_LINK_ID, encoded = true) linkId: String,
    ): Call<ResponseBody>
}