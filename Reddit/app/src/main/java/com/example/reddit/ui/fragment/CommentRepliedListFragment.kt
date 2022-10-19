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
import com.example.reddit.databinding.FragmentCommentRepliedListBinding
import com.example.reddit.model.Comment
import com.example.reddit.model.CommentReplied
import com.example.reddit.model.ListenerType
import com.example.reddit.ui.viewmodel.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CommentRepliedListFragment :
    BaseFragment<FragmentCommentRepliedListBinding>(FragmentCommentRepliedListBinding::inflate) {
    lateinit var commentLink: String
    lateinit var author: String
    private val viewModel: CommentViewModel by viewModels()
    private val commentAdapter: CommentListAdapter by lazy {
        CommentListAdapter(::onItemCommentClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            commentLink = it.getString(CommentListFragment.KEY_LISTING_COMMENT_LINK, "")
            author = it.getString(KEY_LISTING_COMMENT_AUTHOR, "")
        }
        viewModel.getCommentRepliedInfo(commentLink)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.commentRepliedInfoLiveData.observe(viewLifecycleOwner, ::showCommentInfoData)
        viewModel.commentVoteLiveData.observe(viewLifecycleOwner, ::commentVoted)
    }

    @SuppressLint("InflateParams")
    private fun showCommentInfoData(commentReplied: CommentReplied) {
        val view = layoutInflater.inflate(R.layout.item_comment, null)
        CommentListAdapter.printCommentInfo(::onItemCommentClicked,
            commentReplied.mainComment,
            view, false)
        binding.frameItem.addView(view)
        commentAdapter.submitList(commentReplied.commentList)
        showPbLoading(false)
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

    private fun onItemCommentClicked(item: Comment, listenerType: ListenerType) {
        when (listenerType) {
            ListenerType.COMMENT -> {
                if (item.replyCount == 0) {
                    return
                }
                val bundle = Bundle().apply {
                    putString(CommentListFragment.KEY_LISTING_COMMENT_LINK, item.commentLink)
                    putString(KEY_LISTING_COMMENT_AUTHOR, "$author>${item.author}")
                }
                findNavController().navigate(R.id.action_commentRepliedListFragment_self, bundle)
            }
            ListenerType.VOTE -> {
                val dir = if (item.likes == null)
                    1 else 0
                Timber.d("postlink ${item.linkId}")
                viewModel.voteComment(item, dir)
            }
            ListenerType.UN_VOTE -> {
                val dir = if (item.likes == null)
                    -1 else 0
                Timber.d("postlink ${item.linkId}")
                viewModel.voteComment(item, dir)
            }
            else -> {

            }
        }
    }

    private fun initUI() {
        with(binding.inToolbar.toolbar) {
            setToolbarTitle(author)
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
        const val KEY_LISTING_COMMENT_AUTHOR = "comment_author"
    }

}