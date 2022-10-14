package com.example.reddit.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.reddit.model.Account
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.model.subreddit.SubredditListing

class ObjectDiffUtil<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when (oldItem) {
            is Subreddit -> {
                newItem as Subreddit
                oldItem.id == newItem.id
            }
            is SubredditListing -> {
                newItem as SubredditListing
                oldItem.id == newItem.id
            }
            is Account -> {
                newItem as Account
                oldItem.id == newItem.id
            }
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

}