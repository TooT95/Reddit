package com.example.reddit.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reddit.R
import com.example.reddit.databinding.ItemListingImageBinding
import com.example.reddit.databinding.ItemListingPostBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.extension.inflateLayout
import com.example.reddit.model.subreddit.SubredditListing

class SubredditListingAdapter :
    ListAdapter<SubredditListing, SubredditListingAdapter.SubredditListingHolder>(ObjectDiffUtil<SubredditListing>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditListingHolder {
        return when (viewType) {
            IMAGE_TYPE -> {
                SubredditListingHolder.ImageListingHolder(parent.inflateLayout(R.layout.item_listing_image))
            }
            POST_TYPE -> {
                SubredditListingHolder.PostListingHolder(parent.inflateLayout(R.layout.item_listing_post))
            }
            VIDEO_TYPE -> {
                SubredditListingHolder.VideoListingHolder(parent.inflateLayout(R.layout.item_listing_image))
            }
            else -> {
                SubredditListingHolder.ImageListingHolder(parent.inflateLayout(R.layout.item_listing_image))
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

    abstract class SubredditListingHolder(view: View) : RecyclerView.ViewHolder(view) {

        class PostListingHolder(view: View) : SubredditListingHolder(view) {

            private val binding = ItemListingPostBinding.bind(view)
            fun onBind(listing: SubredditListing.ListingPost) {
                with(binding) {
                    txtAuthorName.text = listing.author
                    txtCommentNum.text = listing.numComments.toString()
                    txtSelfText.text = subSelfText(listing.selfText)
                    txtLikeNum.text = listing.score.toString()
                }
            }

            private fun subSelfText(selfText: String): String {
                val result = ""
                if (selfText.length > 150)
                    return "${selfText.subSequence(0, 148)} ..."
                return result
            }
        }

        class ImageListingHolder(view: View) : SubredditListingHolder(view) {

            private val binding = ItemListingImageBinding.bind(view)
            fun onBind(listing: SubredditListing.ListingImage) {
                with(binding) {
                    txtAuthorName.text = listing.author
                    txtCommentNum.text = listing.numComments.toString()
                    txtTitle.text = listing.title
                    txtLikeNum.text = listing.score.toString()
                    ivPlaceHolder.glideImageWithParams(itemView,
                        listing.imageUrl,
                        listing.imageWidth,
                        listing.imageHeight)
                }
            }
        }

        class VideoListingHolder(view: View) : SubredditListingHolder(view) {

            private val binding = ItemListingImageBinding.bind(view)
            fun onBind(listing: SubredditListing.ListingVideo) {
                with(binding) {
                    txtAuthorName.text = listing.author
                    txtCommentNum.text = listing.numComments.toString()
                    txtTitle.text = listing.title
                    txtLikeNum.text = listing.score.toString()
                    Glide.with(itemView)
                        .load(listing.videoUrl)
                        .into(ivPlaceHolder)
                }
            }
        }
    }

    companion object {
        private const val POST_TYPE = 1
        private const val IMAGE_TYPE = 2
        private const val VIDEO_TYPE = 3
    }
}