package com.example.reddit.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.databinding.ItemCommentBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.Comment
import com.example.reddit.model.ListenerType

class CommentListAdapter(private val onItemClicked: (item: Comment, listenerType: ListenerType) -> Unit) :
    ListAdapter<Comment, CommentListAdapter.CommentViewHolder>(ObjectDiffUtil<Comment>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(onItemClicked, parent.inflateLayout(R.layout.item_comment))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    class CommentViewHolder(
        private val onItemClicked: (item: Comment, listenerType: ListenerType) -> Unit,
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        private val binding = ItemCommentBinding.bind(view)
        fun onBind(comment: Comment) {
            printCommentInfo(onItemClicked, comment, itemView)
        }

    }

    companion object {
        fun printCommentInfo(
            onItemClicked: (item: Comment, listenerType: ListenerType) -> Unit,
            comment: Comment,
            view: View,
            printReply:Boolean = true
        ) {
            with(view) {
                findViewById<ImageView>(R.id.iv_avatar).glideImageWithParams(view, "")
                findViewById<TextView>(R.id.txt_body).text = comment.body
                findViewById<TextView>(R.id.txt_author_name).text = comment.author
                findViewById<TextView>(R.id.txt_published_at).text =
                    SubredditListingAdapter.getPublishedAtText(comment.date)
                findViewById<TextView>(R.id.txt_vote_count).text = comment.score.toString()

                val ivVoteUpNotMarked = findViewById<ImageView>(R.id.iv_vote_up_not_marked)
                val ivVoteDownNotMarked = findViewById<ImageView>(R.id.iv_vote_down_not_marked)
                val ivVoteUpMarked = findViewById<ImageView>(R.id.iv_vote_up_marked)
                val ivVoteDownMarked = findViewById<ImageView>(R.id.iv_vote_down_marked)
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
            }
            with(view.findViewById<TextView>(R.id.txt_reply_text)) {
                text =
                    view.resources.getString(com.example.reddit.R.string.text_reply_count,
                        comment.replyCount.toString())
                isVisible = comment.replyCount != 0 && printReply
                setOnClickListener {
                    onItemClicked(comment, com.example.reddit.model.ListenerType.COMMENT)
                }
            }
        }
    }
}