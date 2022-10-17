package com.example.reddit.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.reddit.R
import com.example.reddit.adapter.SubredditListingAdapter
import com.example.reddit.databinding.FragmentCommentListBinding
import com.example.reddit.model.CommentListing
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentListFragment :
    BaseFragment<FragmentCommentListBinding>(FragmentCommentListBinding::inflate) {

    lateinit var commentLink: String
    private val viewModel: CommentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            commentLink = it.getString(KEY_LISTING_COMMENT_LINK, "")
        }
        viewModel.getCommentInfo(commentLink)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.commentInfoLiveData.observe(viewLifecycleOwner, ::showCommentInfoData)
    }

    @SuppressLint("InflateParams")
    private fun showCommentInfoData(commentListing: CommentListing) {
        val view = when (commentListing.listing) {
            is SubredditListing.ListingImage -> layoutInflater.inflate(R.layout.item_listing_image,
                null)
            is SubredditListing.ListingVideo -> layoutInflater.inflate(R.layout.item_listing_video,
                null)
            is SubredditListing.ListingPost -> layoutInflater.inflate(R.layout.item_listing_post,
                null)
        }

        SubredditListingAdapter.showSampleViews(::onItemClicked,
            view,
            commentListing.listing)
        binding.frameItem.addView(view)
    }

    private fun onItemClicked(item: SubredditListing, listenerType: ListenerType) {

    }

    private fun initUI() {
        with(binding.inToolbar.toolbar) {
            title = commentLink
            if (Utils.haveM()) {
                setTitleTextColor(resources.getColor(R.color.primaryTextColor,
                    resources.newTheme()))
            }
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    companion object {
        const val KEY_LISTING_COMMENT_LINK = "comment_perma_link"
    }

}