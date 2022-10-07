package com.example.reddit.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.subreddit.SubredditListing

class SubredditListingAdapter :
    ListAdapter<SubredditListing, SubredditListingAdapter.SubredditListingHolder>(ObjectDiffUtil<SubredditListing>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListingHolder {
        return SubredditListingHolder(parent.inflateLayout(R.layout.fragment_subreddit_listing))
    }

    override fun onBindViewHolder(holder: SubredditListingHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class SubredditListingHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}