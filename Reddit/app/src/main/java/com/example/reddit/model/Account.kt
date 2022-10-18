package com.example.reddit.model

data class Account(
    val id: String,
    var avatarUrl: String,
    val name: String,
    var isFriend: Boolean = false,
    val karma: Int = 0,
)
