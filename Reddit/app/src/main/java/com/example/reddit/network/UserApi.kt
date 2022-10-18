package com.example.reddit.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    @GET(NetworkUtils.SUB_URL_ME)
    fun getMe(): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_FRIEND_LIST)
    fun getFriendList(): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_FRIEND_INFO)
    fun getFriendInfo(@Path(NetworkUtils.PATH_FRIEND_NAME) friendName: String): Call<ResponseBody>

    @PUT(NetworkUtils.SUB_URL_FRIEND_FOLLOW_UN_FOLLOW)
    fun followUser(
        @Path(NetworkUtils.PATH_FRIEND_NAME) userName: String,
        @Body params: RequestBody,
    ): Call<ResponseBody>

    @DELETE(NetworkUtils.SUB_URL_FRIEND_FOLLOW_UN_FOLLOW)
    fun unFollowUser(@Path(NetworkUtils.PATH_FRIEND_NAME) userName: String): Call<ResponseBody>

}