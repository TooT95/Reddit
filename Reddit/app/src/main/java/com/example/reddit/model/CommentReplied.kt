package com.example.reddit.model

data class CommentReplied(val mainComment: Comment, val commentList: List<Comment>)