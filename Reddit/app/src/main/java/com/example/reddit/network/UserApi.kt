package com.example.reddit.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    @GET(NetworkContract.SUB_URL_ME)
    fun getMe(): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_FRIEND_LIST)
    fun getFriendList(): Call<ResponseBody>

    @GET(NetworkContract.SUB_URL_FRIEND_INFO)
    fun getFriendInfo(@Path(NetworkContract.PATH_FRIEND_NAME) friendName: String): Call<ResponseBody>

    @PUT(NetworkContract.SUB_URL_FRIEND_FOLLOW_UN_FOLLOW)
    fun followUser(
        @Path(NetworkContract.PATH_FRIEND_NAME) userName: String,
        @Body params: RequestBody,
    ): Call<ResponseBody>

    @DELETE(NetworkContract.SUB_URL_FRIEND_FOLLOW_UN_FOLLOW)
    fun unFollowUser(@Path(NetworkContract.PATH_FRIEND_NAME) userName: String): Call<ResponseBody>

}