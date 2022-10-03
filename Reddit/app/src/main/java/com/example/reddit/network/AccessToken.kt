package com.example.reddit.network

import okhttp3.*

object AccessToken {
    fun getLL(code: String?): Call {
        val client = OkHttpClient().newBuilder()
            .build()
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("code", code)
            .addFormDataPart("grant_type", "authorization_code")
            .addFormDataPart("redirect_uri", "http://www.example.com/unused/redirect/uri")
            .build()
        val request = Request.Builder()
            .url("https://www.reddit.com/api/v1/access_token")
            .method("POST", body)
            .addHeader("Authorization",
                "Basic NDhkQTh5RG9GZnRDaTdqSG5tU2ozUTpac0djQnhZb3RmSzBhdHAzUnRzV2xqdmNucHRJM3c=")
            .addHeader("Cookie",
                "edgebucket=NMiqhn5s7nj179rMdR; loid=0000000000sy8h8pac.2.1664534359447.Z0FBQUFBQmpOc2RYTEh1NjUwUEZfMXhMdmRBU3hUN1I5dVRJeXozRWpyczhPQmhBaDlGbmF3dVdkd1h6ZWpnYm5CMWJYN0V5MGg0NXJkZXZ2QzVvX2U5YWdLT1R0dGYyOEI3TEVrQU1Cam5GUEhvOGxMVUZEdGg2RmVkVGNxZkcxRl9YM29Jck15Szc; session_tracker=EZFaTZUz4x9l0MzE6H.0.1664535849295.Z0FBQUFBQmpOczBwUnhDSGd0dXhBMjBZdkFZN1lrWC1rbkR1MEpGckJDSndwdzZhU0FWamxnTlZEQjFYV21BcERmVWRhOFRIcWJjUTJ0eUV1YzF1SDVlbl9lcTB6MGtyQUJUNnU1clRZN19vTDhIWmhwUkhuN3QtVVE5TEV4QTlCRXVmLUF3SFVhdGY")
            .build()
        return client.newCall(request)
    }
}