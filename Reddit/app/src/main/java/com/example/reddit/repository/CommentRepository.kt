package com.example.reddit.repository

import android.net.Uri
import com.example.reddit.model.CommentListing
import com.example.reddit.network.CommentApi
import com.example.reddit.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        return CommentListing(listing, emptyList())
    }
}