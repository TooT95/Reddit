package com.example.reddit.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.databinding.ItemFriendListBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.Account

class FriendListAdapter :
    ListAdapter<Account, FriendListAdapter.FriendListHolder>(ObjectDiffUtil<Account>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListHolder {
        return FriendListHolder(parent.inflateLayout(R.layout.item_friend_list))
    }

    override fun onBindViewHolder(holder: FriendListHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class FriendListHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemFriendListBinding.bind(view)
        fun onBind(account: Account) {
            with(binding) {
                txtFriendId.text = account.id
                txtFriendName.text = account.name
                ivAvatar.glideImageWithParams(itemView, account.avatarUrl)
            }
        }

    }

}