package com.example.reddit.repository

import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.network.SubredditApi
import com.example.reddit.utils.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
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

    suspend fun getSubredditListingList(srName: String): List<SubredditListing> {
        return suspendCoroutine { continuation ->
            scope.launch {
                subApi.getSrListingList(srName, 20, null, 0)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>,
                        ) {
                            val responseString = response.body()?.string().orEmpty()
                            continuation.resume(getSrListingList(responseString))
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                    })
            }
        }
    }

    private fun getSrListingList(jsonString: String): List<SubredditListing> {
        val subredditListing = mutableListOf<SubredditListing>()
        val childrenObjectList = childrenJsonArray(jsonString)
        for (item in 0 until childrenObjectList.length()) {
            val currentItemJson =
                childrenObjectList.getJSONObject(item).getJSONObject(Utils.COL_DATA_API)

            val isVideo = currentItemJson.getBoolean(SubredditListing.COL_IS_VIDEO)
            val id = currentItemJson.getString(SubredditListing.COL_ID)
            val title = currentItemJson.getString(SubredditListing.COL_TITLE)
            val commentNum = currentItemJson.getLong(SubredditListing.COL_COMMENTS_NUM)
            val author = currentItemJson.getString(SubredditListing.COL_AUTHOR)
            val created = currentItemJson.getDouble(SubredditListing.COL_CREATED)
            val selfText = currentItemJson.getString(SubredditListing.COL_SELF_TEXT)
            val imageUrl = currentItemJson.getString(SubredditListing.COL_IMAGE_URl)
            val score = currentItemJson.getInt(SubredditListing.COL_SCORE)
            when {
                isVideo -> {
                    val mediaObject = currentItemJson.getJSONObject(SubredditListing.COL_MEDIA)
                        .getJSONObject(SubredditListing.COL_REDDIT_MEDIA)
                    val videoUrl = mediaObject.getString(SubredditListing.COL_VIDEO_URL)
                    subredditListing.add(SubredditListing.ListingVideo(id,
                        title,
                        commentNum,
                        author,
                        created,
                        videoUrl,
                        score))
                }
                imageUrl.contains("self") -> {
                    subredditListing.add(SubredditListing.ListingPost(id,
                        title,
                        commentNum,
                        selfText,
                        created,
                        author,
                        score))
                }
                imageUrl.contains("default") -> {
                    subredditListing.add(SubredditListing.ListingPost(id,
                        title,
                        commentNum,
                        title,
                        created,
                        author,
                        score))
                }
                else -> {
                    val width = currentItemJson.getInt(SubredditListing.COL_IMAGE_WIDTH)
                    val height = currentItemJson.getInt(SubredditListing.COL_IMAGE_HEIGHT)
                    val imageUrlCommon =currentItemJson.getString(SubredditListing.COL_IMAGE_URl_COMMON)
                    subredditListing.add(SubredditListing.ListingImage(id,
                        title,
                        commentNum,
                        author,
                        created,
                        imageUrlCommon,
                        score,
                        width,
                        height))
                }
            }

        }
        return subredditListing
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

            subredditList.add(Subreddit(id, displayName, title, isFollower, name))
        }
        return subredditList
    }

    private fun childrenJsonArray(jsonString: String): JSONArray {
        val jsonObject = JSONObject(jsonString)
        val jsonDataObject = jsonObject.getJSONObject(Utils.COL_DATA_API)
        return jsonDataObject.getJSONArray(Utils.COL_CHILDREN_API)
    }

    companion object {
        const val METHOD_NEW_SUBREDDIT_LIST = "new"
        const val METHOD_POPULAR_SUBREDDIT_LIST = "popular"
    }
}