package com.example.reddit.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.databinding.ItemSubredditListBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.Subreddit

class SubredditListAdapter(private val itemClickListener: (item: Subreddit, listenerType: ListenerType) -> Unit) :
    ListAdapter<Subreddit, SubredditListAdapter.SubredditListViewHolder>(ObjectDiffUtil<Subreddit>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListViewHolder {
        return SubredditListViewHolder(itemClickListener,
            parent.inflateLayout(R.layout.item_subreddit_list))
    }

    override fun onBindViewHolder(holder: SubredditListViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    class SubredditListViewHolder(
        private val itemClickListener: (item: Subreddit, listenerType: ListenerType) -> Unit,
        view: View,
    ) :
        RecyclerView.ViewHolder(view) {

        private val itemBinding = ItemSubredditListBinding.bind(view)

        fun onBind(subreddit: Subreddit) {
            itemView.setOnClickListener {
                itemClickListener(subreddit, ListenerType.OPEN_SUBREDDIT)
            }
            with(itemBinding) {
                txtName.text = subreddit.title
                ivHeaderImg.isVisible = subreddit.headerImage.isNotEmpty()
                if (subreddit.headerImage.isNotEmpty()) {
                    ivHeaderImg.glideImageWithParams(itemView, subreddit.headerImage)
                }
                if (subreddit.userSubscriber) {
                    ivSubscribe.setImageResource(R.drawable.ic_subscriber)
                } else {
                    ivSubscribe.setImageResource(R.drawable.ic_notsubscriber)
                }
                ivSubscribe.setOnClickListener {
                    itemClickListener(subreddit, ListenerType.SUBSCRIBE)
                }
            }
        }
    }
}