package com.example.reddit.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddit.R
import com.example.reddit.ui.adapter.CommentListAdapter
import com.example.reddit.ui.adapter.SubredditListingAdapter
import com.example.reddit.databinding.FragmentCommentListBinding
import com.example.reddit.model.Comment
import com.example.reddit.model.CommentListing
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.ui.viewmodel.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentListFragment :
    BaseFragment<FragmentCommentListBinding>(FragmentCommentListBinding::inflate) {

    lateinit var commentLink: String
    private val viewModel: CommentViewModel by viewModels()
    private val commentAdapter: CommentListAdapter by lazy {
        CommentListAdapter(::onItemCommentClicked)
    }

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
        viewModel.commentVoteLiveData.observe(viewLifecycleOwner, ::commentVoted)
    }

    private fun commentVoted(params: Pair<Comment, Int>) {
        val comment = params.first
        val likes = when (params.second) {
            1 -> true
            -1 -> false
            else -> null
        }
        val commentList = commentAdapter.currentList
        commentList.filter {
            it.id == comment.id
        }.forEach {
            it.likes = likes
            it.score += params.second
        }
        commentAdapter.submitList(commentList)
        commentAdapter.notifyItemChanged(commentAdapter.currentList.indexOf(comment))
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
        setToolbarTitle(commentListing.listing.title)
        SubredditListingAdapter.showSampleViews(::onItemClicked,
            view,
            commentListing.listing)
        binding.frameItem.addView(view)
        commentAdapter.submitList(commentListing.commentList)
        showPbLoading(false)
    }

    private fun onItemClicked(item: SubredditListing, listenerType: ListenerType) {
        when (listenerType) {
            ListenerType.FRIEND -> {
                val bundle = Bundle().apply {
                    putString(UserFragment.KEY_USER_NAME, item.author)
                }
                findNavController().navigate(R.id.action_commentListFragment_to_userFragment,
                    bundle)
            }
            else -> {

            }
        }
    }

    private fun onItemCommentClicked(item: Comment, listenerType: ListenerType) {
        when (listenerType) {
            ListenerType.COMMENT -> {
                if (item.replyCount == 0) {
                    return
                }
                val bundle = Bundle().apply {
                    putString(KEY_LISTING_COMMENT_LINK, item.commentLink)
                    putString(CommentRepliedListFragment.KEY_LISTING_COMMENT_AUTHOR,
                        item.author)
                }
                findNavController().navigate(R.id.action_commentListFragment_to_commentRepliedListFragment,
                    bundle)
            }
            ListenerType.FRIEND -> {
                val bundle = Bundle().apply {
                    putString(UserFragment.KEY_USER_NAME, item.author)
                }
                findNavController().navigate(R.id.action_commentListFragment_to_userFragment,
                    bundle)
            }
            ListenerType.VOTE -> {
                val dir = if (item.likes == null)
                    1 else 0
                viewModel.voteComment(item, dir)
            }
            ListenerType.UN_VOTE -> {
                val dir = if (item.likes == null)
                    -1 else 0
                viewModel.voteComment(item, dir)
            }
            else -> {

            }
        }
    }

    private fun initUI() {
        with(binding.inToolbar.toolbar) {
            setToolbarTitle(commentLink)
            setTitleTextColor(resources.getColor(R.color.primaryTextColor,
                resources.newTheme()))
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        with(binding.rvCommentList) {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        showPbLoading(true)
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            rvCommentList.isVisible = !show
            pbLoading.isVisible = show
            frameItem.isVisible = !show
        }
    }

    private fun setToolbarTitle(text: String) {
        binding.inToolbar.toolbar.title = text
    }

    companion object {
        const val KEY_LISTING_COMMENT_LINK = "comment_perma_link"
    }

}