package com.example.reddit.adapter

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.databinding.ItemListingImageBinding
import com.example.reddit.databinding.ItemListingPostBinding
import com.example.reddit.databinding.ItemListingVideoBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.extension.inflateLayout
import com.example.reddit.extension.setUrlAndMediaPlayer
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.SubredditListing
import java.util.*

class SubredditListingAdapter(private val onItemClicked: (item: SubredditListing, listenerType: ListenerType) -> Unit) :
    ListAdapter<SubredditListing, SubredditListingAdapter.SubredditListingHolder>(ObjectDiffUtil<SubredditListing>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListingHolder {
        return when (viewType) {
            IMAGE_TYPE -> {
                SubredditListingHolder.ImageListingHolder(onItemClicked,
                    parent.inflateLayout(R.layout.item_listing_image))
            }
            POST_TYPE -> {
                SubredditListingHolder.PostListingHolder(onItemClicked,
                    parent.inflateLayout(R.layout.item_listing_post))
            }
            VIDEO_TYPE -> {
                SubredditListingHolder.VideoListingHolder(onItemClicked,
                    parent.inflateLayout(R.layout.item_listing_video))
            }
            else -> {
                SubredditListingHolder.ImageListingHolder(onItemClicked,
                    parent.inflateLayout(R.layout.item_listing_image))
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is SubredditListing.ListingImage -> {
                IMAGE_TYPE
            }
            is SubredditListing.ListingPost -> {
                POST_TYPE
            }
            is SubredditListing.ListingVideo -> {
                VIDEO_TYPE
            }
            else -> super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: SubredditListingHolder, position: Int) {
        when (val currentItem = currentList[position]) {
            is SubredditListing.ListingImage -> {
                holder as SubredditListingHolder.ImageListingHolder
                holder.onBind(currentItem)
            }
            is SubredditListing.ListingPost -> {
                holder as SubredditListingHolder.PostListingHolder
                holder.onBind(currentItem)
            }
            is SubredditListing.ListingVideo -> {
                holder as SubredditListingHolder.VideoListingHolder
                holder.onBind(currentItem)
            }
        }
    }

    abstract class SubredditListingHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        class PostListingHolder(
            private val onItemClicked: (item: SubredditListing, listenerType: ListenerType) -> Unit,
            view: View,
        ) : SubredditListingHolder(view) {

            private val binding = ItemListingPostBinding.bind(view)
            fun onBind(listing: SubredditListing.ListingPost) {
                with(binding) {
                    txtSelfText.text = subSelfText(listing.selfText)
                    showSampleViews(onItemClicked, itemView, listing)
                }
            }

            private fun subSelfText(selfText: String): String {
                if (selfText.length > 150)
                    return "${selfText.subSequence(0, 148)} ..."
                return selfText
            }
        }

        class ImageListingHolder(
            private val onItemClicked: (item: SubredditListing, listenerType: ListenerType) -> Unit,
            view: View,
        ) : SubredditListingHolder(view) {

            private val binding = ItemListingImageBinding.bind(view)
            fun onBind(listing: SubredditListing.ListingImage) {
                with(binding) {
                    ivPlaceHolder.glideImageWithParams(itemView,
                        listing.imageUrl)
                    showSampleViews(onItemClicked, itemView, listing)
                }
            }
        }

        class VideoListingHolder(
            private val onItemClicked: (item: SubredditListing, listenerType: ListenerType) -> Unit,
            view: View,
        ) : SubredditListingHolder(view) {

            private val binding = ItemListingVideoBinding.bind(view)
            fun onBind(listing: SubredditListing.ListingVideo) {
                with(binding) {
                    videoView.setUrlAndMediaPlayer(itemView.context, listing.videoUrl)
                    showSampleViews(onItemClicked, itemView, listing)
                }
            }
        }

        protected fun showSampleViews(
            onItemClicked: (item: SubredditListing, listenerType: ListenerType) -> Unit,
            itemView: View,
            listing: SubredditListing,
        ) {
            with(itemView) {
                findViewById<TextView>(R.id.txt_title).text = listing.title
                findViewById<TextView>(R.id.txt_author_name).text = listing.author
                findViewById<TextView>(R.id.txt_comment_num).text = listing.numComments.toString()
                findViewById<TextView>(R.id.txt_published_at).text =
                    getPublishedAtText(listing.created)
                findViewById<TextView>(R.id.txt_share).setOnClickListener {
                    shareUrl(listing.url)
                }
            }
            saveUnsaveImageView(onItemClicked, itemView, listing.saved, listing)
        }

        private fun saveUnsaveImageView(
            onItemClicked: (item: SubredditListing, listenerType: ListenerType) -> Unit,
            itemView: View,
            saved: Boolean,
            item: SubredditListing,
        ) {
            with(itemView.findViewById<ImageView>(R.id.iv_save)) {
                isVisible = !saved
                setOnClickListener {
                    onItemClicked(item, ListenerType.UNSAVE)
                }
            }
            with(itemView.findViewById<ImageView>(R.id.iv_un_save)) {
                isVisible = saved
                setOnClickListener {
                    onItemClicked(item, ListenerType.SAVE)
                }
            }
        }


        protected fun shareUrl(url: String) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            shareIntent.type = "text/plain"
            itemView.context.startActivity(Intent.createChooser(shareIntent, "send to"))
        }

        protected fun getPublishedAtText(createdTime: Double): String {
            val secondsInMilli: Long = 1000
            var different = Date().time - (createdTime.toLong() * secondsInMilli)
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val monthInMilli = daysInMilli * 30

            val elapsedMonth: Long = different / monthInMilli
            different %= monthInMilli

            val elapsedDays: Long = different / daysInMilli
            different %= daysInMilli

            val elapsedHours: Long = different / hoursInMilli
            different %= hoursInMilli

            val elapsedMinutes: Long = different / minutesInMilli
            different %= minutesInMilli

            val elapsedSeconds: Long = different / secondsInMilli
            return when {
                (elapsedMonth > 0) -> "posted $elapsedMonth month ago"
                (elapsedDays > 0) -> "posted $elapsedDays ${if (elapsedDays == 1L) "day" else "days"} ago"
                (elapsedHours > 0) -> "posted $elapsedHours ${if (elapsedHours == 1L) "hour" else "hours"} ago"
                (elapsedMinutes > 0) -> "posted $elapsedMinutes ${if (elapsedMinutes == 1L) "minute" else "minutes"} ago"
                else -> "posted $elapsedSeconds second ago"
            }
        }
    }

    companion object {
        private const val POST_TYPE = 1
        private const val IMAGE_TYPE = 2
        private const val VIDEO_TYPE = 3
    }
}