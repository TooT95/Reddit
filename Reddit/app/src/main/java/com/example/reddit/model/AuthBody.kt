package com.example.reddit.model

data class AuthBody(val code: String, val grant_type: String, val redirect_uri: String)