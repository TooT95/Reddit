package com.example.reddit.model

data class AuthResponse(
    val access_token: String?,
    val token_type: String?,
    val expires_in: Long,
    val scope: String?,
    val error: String?
)
