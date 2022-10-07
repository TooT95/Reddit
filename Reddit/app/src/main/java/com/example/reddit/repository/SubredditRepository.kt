package com.example.reddit.repository

import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.network.SubredditApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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

    suspend fun getSubredditList(method: String): List<Subreddit> {
        return suspendCoroutine { continuation ->
            scope.launch {
                subApi.apply {
                    if (method == METHOD_NEW_SUBREDDIT_LIST) {
                        getNewList()
                    } else {
                        getPopularList()
                    }.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            val responseString = response.body()?.string().orEmpty()
                            continuation.resume(getSubListParsedJson(responseString))
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                    })
                }
            }
        }
    }

    private fun getSubListParsedJson(jsonString: String): List<Subreddit> {
        val subredditList = mutableListOf<Subreddit>()
        val jsonObject = JSONObject(jsonString)
        val jsonDataObject = jsonObject.getJSONObject("data")
        val childrenObjectList = jsonDataObject.getJSONArray("children")

        for (item in 0 until childrenObjectList.length()) {
            val currentItemJson = childrenObjectList.getJSONObject(item).getJSONObject("data")

            val id = currentItemJson.getString(Subreddit.COL_ID)
            val displayName = currentItemJson.getString(Subreddit.COL_DISPLAY_NAME)
            val title = currentItemJson.getString(Subreddit.COL_TITLE)
            val name = currentItemJson.getString(Subreddit.COL_NAME)
            val isFollower = currentItemJson.getBoolean(Subreddit.COL_USER_SUBSCRIBER)

            subredditList.add(Subreddit(id, displayName, title, isFollower, name))
        }
        return subredditList
    }

    companion object {
        const val METHOD_NEW_SUBREDDIT_LIST = "new"
        const val METHOD_POPULAR_SUBREDDIT_LIST = "new"
    }
}