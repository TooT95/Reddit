package com.example.reddit.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET(NetworkUtils.SUB_URL_ME)
    fun getMe(): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_FRIEND_LIST)
    fun getFriendList(): Call<ResponseBody>

    @GET(NetworkUtils.SUB_URL_FRIEND_INFO)
    fun getFriendInfo(@Path(NetworkUtils.PATH_FRIEND_NAME) friendName: String): Call<ResponseBody>

}