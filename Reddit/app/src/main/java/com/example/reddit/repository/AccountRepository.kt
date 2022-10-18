package com.example.reddit.repository

import com.example.reddit.model.Account
import com.example.reddit.network.SubredditApi
import com.example.reddit.network.UserApi
import com.example.reddit.utils.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AccountRepository @Inject constructor(
    private val subredditApi: SubredditApi,
    private val userApi: UserApi,
) {

    suspend fun getMe(): Account {
        return suspendCoroutine { continuation ->
            userApi.getMe().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string().orEmpty()
                        continuation.resume(getMeParsedJson(responseString))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }

    suspend fun getSubredditList(after: String? = null): Int {
        return suspendCoroutine { continuation ->
            subredditApi.getSubscribedList(after).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string().orEmpty()
                        continuation.resume(getSubListParsedJson(responseString))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })

        }
    }

    suspend fun getFriendList(): List<Account> {
        return suspendCoroutine { continuation ->
            userApi.getFriendList().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string().orEmpty()
                        continuation.resume(getFriendListParsedJson(responseString))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }

    suspend fun getFriendInfo(friendName: String): Account {
        return suspendCoroutine { continuation ->
            userApi.getFriendInfo(friendName).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string().orEmpty()
                        continuation.resume(getMeParsedJson(responseString))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }

    suspend fun followUser(userName: String, follow: Boolean): Boolean {
        return suspendCoroutine { continuation ->
            userApi.followUser(userName,
                createJsonRequestBody("name" to userName, "notes" to "add"))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(follow)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
        }
    }

    suspend fun unfollowUser(userName: String, follow: Boolean): Boolean {
        return suspendCoroutine { continuation ->
            userApi.unFollowUser(userName).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(follow)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
        RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(mapOf(*params)).toString())

    private fun getFriendListParsedJson(jsonString: String): List<Account> {
        val friendList = mutableListOf<Account>()
        val jsonObject = JSONObject(jsonString)
        val childrenList =
            jsonObject.getJSONObject(Utils.COL_DATA_API).getJSONArray(Utils.COL_CHILDREN_API)
        for (item in 0 until childrenList.length()) {
            val currentJson = childrenList.getJSONObject(item)
            val id = currentJson.getString(COL_ID)
            val name = currentJson.getString(COL_NAME)
            friendList.add(Account(id, "", name))
        }
        return friendList
    }

    private fun getSubListParsedJson(jsonString: String): Int {
        val childrenObjectList = SubredditRepository.childrenJsonArray(jsonString)
        return childrenObjectList.length()
    }

    private fun getMeParsedJson(responseString: String): Account {
        val jsonObject = JSONObject(responseString)
        val data = jsonObject.getJSONObject(Utils.COL_DATA_API)

        val name = data.getString(COL_NAME)
        val id = data.getString(COL_ID)
        val avatar = data.getString(COL_AVATAR)
        val avatarSnoo = data.getString(COL_AVATAR_SNOO)
        val karma = data.getInt(COL_KARMA)
        val isFriend = data.getBoolean(COL_IS_FRIEND)
        return Account(id, avatarSnoo.ifEmpty { avatar }, name, isFriend, karma)
    }

    companion object {
        private const val COL_NAME = "name"
        private const val COL_ID = "id"
        private const val COL_AVATAR = "icon_img"
        private const val COL_KARMA = "comment_karma"
        private const val COL_IS_FRIEND = "is_friend"
        private const val COL_AVATAR_SNOO = "snoovatar_img"
    }

}