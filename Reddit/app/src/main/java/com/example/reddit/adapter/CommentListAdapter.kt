package com.example.reddit.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.databinding.ItemCommentBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.Comment

class CommentListAdapter :
    ListAdapter<Comment, CommentListAdapter.CommentViewHolder>(ObjectDiffUtil<Comment>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(parent.inflateLayout(R.layout.item_comment))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemCommentBinding.bind(view)
        fun onBind(comment: Comment) {
            with(binding) {
                ivAvatar.glideImageWithParams(itemView, "")
                txtBody.text = comment.body
                txtAuthorName.text = comment.author
                txtPublishedAt.text = SubredditListingAdapter.getPublishedAtText(comment.date)
                txtVoteCount.text = comment.score.toString()

                when (comment.likes) {
                    null -> {
                        ivVoteUpNotMarked.isVisible = true
                        ivVoteDownNotMarked.isVisible = true
                        ivVoteUpMarked.isVisible = false
                        ivVoteDownMarked.isVisible = false
                    }
                    true -> {
                        ivVoteUpMarked.isVisible = true
                        ivVoteDownNotMarked.isVisible = true
                        ivVoteUpNotMarked.isVisible = false
                        ivVoteDownMarked.isVisible = false
                    }
                    false -> {
                        ivVoteUpNotMarked.isVisible = true
                        ivVoteDownMarked.isVisible = true
                        ivVoteUpMarked.isVisible = false
                        ivVoteDownNotMarked.isVisible = false
                    }
                }
                txtReplyText.text =
                    itemView.resources.getString(R.string.text_reply_count,
                        comment.replyCount.toString())
                txtReplyText.isVisible = comment.replyCount != 0
            }
        }
    }
}