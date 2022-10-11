package com.example.reddit.repository

import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.network.NetworkUtils
import com.example.reddit.network.SubredditApi
import com.example.reddit.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SubredditRepository @Inject constructor(private val subApi: SubredditApi) {

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun getSubredditList(method: String, after: String? = null): List<Subreddit> {
        return suspendCoroutine { continuation ->
            scope.launch {
                subApi.apply {
                    if (method == METHOD_NEW_SUBREDDIT_LIST) {
                        getNewList(after)
                    } else {
                        getPopularList(after)
                    }.enqueue(object : Callback<ResponseBody> {
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
        }
    }

    suspend fun subscribeSubreddit(
        index: Int,
        subreddit: Subreddit,
    ): Int? {
        return suspendCoroutine { continuation ->
            scope.launch {
                val action = if (subreddit.userSubscriber) "unsub" else "sub"
                subApi.subUnsubSubreddit(subreddit.dNamePrefixed, action)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            if (response.isSuccessful) {
                                continuation.resume(index)
                            } else {
                                continuation.resume(null)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })
            }

        }
    }

    private fun getSubListParsedJson(jsonString: String): List<Subreddit> {
        val subredditList = mutableListOf<Subreddit>()
        val childrenObjectList = childrenJsonArray(jsonString)

        for (item in 0 until childrenObjectList.length()) {
            val currentItemJson =
                childrenObjectList.getJSONObject(item).getJSONObject(Utils.COL_DATA_API)

            val id = currentItemJson.getString(Subreddit.COL_ID)
            val displayName = currentItemJson.getString(Subreddit.COL_DISPLAY_NAME)
            val title = currentItemJson.getString(Subreddit.COL_TITLE)
            val name = currentItemJson.getString(Subreddit.COL_NAME)
            val isFollower = currentItemJson.getBoolean(Subreddit.COL_USER_SUBSCRIBER)
            val headerImage = currentItemJson.getString(Subreddit.COL_HEADER_IMAGE)
            val dNamePrefixed = currentItemJson.getString(Subreddit.COL_NAME_PREFIXED)

            subredditList.add(Subreddit(id,
                displayName,
                title,
                isFollower,
                name,
                headerImage,
                dNamePrefixed))
        }
        return subredditList
    }

    companion object {
        fun childrenJsonArray(jsonString: String, isSubreddit: Boolean = true): JSONArray {
            val jsonObject = JSONObject(jsonString)
            val jsonDataObject = jsonObject.getJSONObject(Utils.COL_DATA_API)
            if (isSubreddit) {
                Utils.SUB_AFTER = jsonDataObject.getString(NetworkUtils.QUERY_AFTER)
            } else
                Utils.SUB_LISTING_AFTER = jsonDataObject.getString(NetworkUtils.QUERY_AFTER)
            return jsonDataObject.getJSONArray(Utils.COL_CHILDREN_API)
        }

        const val METHOD_NEW_SUBREDDIT_LIST = "new"
        const val METHOD_POPULAR_SUBREDDIT_LIST = "popular"
    }
}