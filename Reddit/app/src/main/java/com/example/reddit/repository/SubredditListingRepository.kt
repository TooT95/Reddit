package com.example.reddit.repository

import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.network.SubredditListingApi
import com.example.reddit.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SubredditListingRepository @Inject constructor(private val subApi: SubredditListingApi) {

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun getSubredditListingList(
        srName: String,
        after: String? = null,
    ): List<SubredditListing> {
        return suspendCoroutine { continuation ->
            scope.launch {
                subApi.getSrListingList(srName, 20, after, 0)
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

    suspend fun saveUnsaveListing(name: String, save: Boolean, index: Int): Int? {
        return suspendCoroutine { continuation ->
            subApi.saveUnSavePost(if (save) UN_SAVE_LISTING_ACTION else SAVE_LISTING_ACTION,
                name,
                CATEGORY_LISTING).enqueue(object : Callback<ResponseBody> {
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

    private fun getSrListingList(jsonString: String): List<SubredditListing> {
        val subredditListing = mutableListOf<SubredditListing>()
        val childrenObjectList = SubredditRepository.childrenJsonArray(jsonString, false)
        for (item in 0 until childrenObjectList.length()) {
            val currentItemJson =
                childrenObjectList.getJSONObject(item).getJSONObject(Utils.COL_DATA_API)

            subredditListing.add(getListing(currentItemJson))

        }
        return subredditListing
    }

    suspend fun getSavedListing(accountName: String): List<SubredditListing> {
        return suspendCoroutine { continuation ->
            scope.launch {
                subApi.getSavedList(accountName)
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

    companion object {
        const val SAVE_LISTING_ACTION = "save"
        const val UN_SAVE_LISTING_ACTION = "unsave"
        const val CATEGORY_LISTING = "link"

        fun getListing(currentItemJson: JSONObject): SubredditListing {
            val isVideo = currentItemJson.getBoolean(SubredditListing.COL_IS_VIDEO)
            val id = currentItemJson.getString(SubredditListing.COL_ID)
            val url = currentItemJson.getString(SubredditListing.COL_URL)
            val title = currentItemJson.getString(SubredditListing.COL_TITLE)
            val commentNum = currentItemJson.getLong(SubredditListing.COL_COMMENTS_NUM)
            val author = currentItemJson.getString(SubredditListing.COL_AUTHOR)
            val created = currentItemJson.getDouble(SubredditListing.COL_CREATED)
            val selfText = currentItemJson.getString(SubredditListing.COL_SELF_TEXT)
            val imageUrl = currentItemJson.getString(SubredditListing.COL_IMAGE_URl)
            val saved = currentItemJson.getBoolean(SubredditListing.COL_SAVED)
            val fullName = currentItemJson.getString(SubredditListing.COL_FULL_NAME)
            val commentLink = currentItemJson.getString(SubredditListing.COL_COMMENT_LINK)
            return when {
                isVideo -> {
                    val mediaObject = currentItemJson.getJSONObject(SubredditListing.COL_MEDIA)
                        .getJSONObject(SubredditListing.COL_REDDIT_MEDIA)
                    val videoUrl = mediaObject.getString(SubredditListing.COL_VIDEO_URL)
                    SubredditListing.ListingVideo(id,
                        url,
                        saved,
                        title,
                        commentNum,
                        author,
                        created,
                        videoUrl,
                        fullName,
                        commentLink)
                }
                imageUrl.contains("self") ||
                        imageUrl.contains("default")
                        || imageUrl.isEmpty() -> {
                    SubredditListing.ListingPost(id,
                        url,
                        saved,
                        title,
                        commentNum,
                        selfText,
                        created,
                        author,
                        fullName,
                        commentLink)
                }
                else -> {
                    var imageUrlCommon = ""
                    try {
                        imageUrlCommon =
                            currentItemJson.getString(SubredditListing.COL_IMAGE_URl_COMMON)
                    } catch (t: Throwable) {
                    }
//                            if (imageUrl.contains(".jpg")) imageUrl else currentItemJson.getString(
//                                SubredditListing.COL_IMAGE_URl_COMMON)
                    SubredditListing.ListingImage(id,
                        url,
                        saved,
                        title,
                        commentNum,
                        author,
                        created,
                        imageUrlCommon,
                        fullName,
                        commentLink)
                }
            }
        }
    }
}