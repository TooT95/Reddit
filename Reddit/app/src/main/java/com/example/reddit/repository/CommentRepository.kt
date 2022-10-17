package com.example.reddit.repository

import android.net.Uri
import com.example.reddit.model.Comment
import com.example.reddit.model.CommentListing
import com.example.reddit.network.CommentApi
import com.example.reddit.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.URLEncoder
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CommentRepository @Inject constructor(private val api: CommentApi) {

    suspend fun getCommentInfo(commentLink: String): CommentListing {
        return suspendCoroutine { continuation ->
            api.getCommentInfo(commentLink, "")
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            val responseString = response.body()?.string().orEmpty()
                            continuation.resume(getCommentInfoParsedJson(responseString))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }

                })
        }
    }

    private fun getCommentInfoParsedJson(jsonString: String): CommentListing {
        val commonArray = JSONArray(jsonString)
        val childrenArray = commonArray.getJSONObject(0).getJSONObject(Utils.COL_DATA_API)
            .getJSONArray(Utils.COL_CHILDREN_API)
        val currentItemJson = childrenArray.getJSONObject(0).getJSONObject(Utils.COL_DATA_API)
        val listing = SubredditListingRepository.getListing(currentItemJson)
        return CommentListing(listing, getCommentList(commonArray.getJSONObject(1)))
    }

    private fun getCommentList(jsonObject: JSONObject): List<Comment> {
        val commentList = mutableListOf<Comment>()
        val childrenArray = jsonObject.getJSONObject(Utils.COL_DATA_API)
            .getJSONArray(Utils.COL_CHILDREN_API)
        for (index in 0 until childrenArray.length()) {
            val currentItemJson =
                childrenArray.getJSONObject(index).getJSONObject(Utils.COL_DATA_API)
            try {
                val id = currentItemJson.getString(Comment.COL_ID)
                val body = currentItemJson.getString(Comment.COL_BODY)
                val date = currentItemJson.getDouble(Comment.COL_DATE)
                val author = currentItemJson.getString(Comment.COL_AUTHOR)
                val score = currentItemJson.getInt(Comment.COL_SCORE)
                val likeJson = currentItemJson.get(Comment.COL_LIKES)
                val likes = if (likeJson.toString() == "null") null else likeJson as Boolean
                val commentLink = currentItemJson.getString(Comment.COL_REPLY_COMMENT_LINK)
                val replies = currentItemJson.get(Comment.COL_REPLY_COUNT)
                var replyCount = 0
                if (replies.toString() != "") {
                    replyCount = currentItemJson.getJSONObject(Comment.COL_REPLY_COUNT)
                        .getJSONObject(Utils.COL_DATA_API)
                        .getJSONArray(Utils.COL_CHILDREN_API).length()
                }
                commentList.add(Comment(id,
                    body,
                    date,
                    author,
                    score,
                    likes,
                    commentLink,
                    replyCount))
            } catch (t: Throwable) {
                Timber.d("commenterror: ${t.message}")
            }
        }
        return commentList
    }
}