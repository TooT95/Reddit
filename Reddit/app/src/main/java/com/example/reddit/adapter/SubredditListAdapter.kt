package com.example.reddit.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.databinding.ItemSubredditListBinding
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.subreddit.Subreddit

class SubredditListAdapter(private val onItemClicked: (itemId: Int) -> Unit) :
    ListAdapter<Subreddit, SubredditListAdapter.SubredditListViewHolder>(ObjectDiffUtil<Subreddit>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListViewHolder {
        return SubredditListViewHolder(onItemClicked,
            parent.inflateLayout(R.layout.item_subreddit_list))
    }

    override fun onBindViewHolder(holder: SubredditListViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    class SubredditListViewHolder(private val onItemClicked: (itemId: Int) -> Unit, view: View) :
        RecyclerView.ViewHolder(view) {

        private val itemBinding = ItemSubredditListBinding.bind(view)

        init {
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        private fun initAdapter() {

        }

        fun onBind(subreddit: Subreddit) {
            with(itemBinding) {
                txtName.text = subreddit.title
                if (subreddit.userSubscriber) {
                    ivSubscribe.setImageResource(R.drawable.ic_subscriber)
                }
            }
        }
    }
}