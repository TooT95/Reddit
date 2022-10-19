package com.example.reddit.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddit.databinding.FragmentUserCommentListBinding
import com.example.reddit.model.Comment
import com.example.reddit.model.ListenerType
import com.example.reddit.ui.adapter.CommentListAdapter
import com.example.reddit.ui.viewmodel.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserCommentListFragment :
    BaseFragment<FragmentUserCommentListBinding>(FragmentUserCommentListBinding::inflate) {

    private var userName = ""
    private val viewModel: CommentViewModel by viewModels()
    private val commentAdapter: CommentListAdapter by lazy {
        CommentListAdapter(::commentListOnItemClickListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(UserFragment.KEY_USER_NAME, "")
        }
        viewModel.getUserCommentList(userName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun commentListOnItemClickListener(item: Comment, listenerType: ListenerType) {
        when (listenerType) {
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

    private fun observeViewModels() {
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.commentListLiveData.observe(viewLifecycleOwner, ::refreshCommentList)
        viewModel.commentVoteLiveData.observe(viewLifecycleOwner, ::commentVoted)
    }

    private fun refreshCommentList(commentList: List<Comment>) {
        commentAdapter.submitList(commentList)
        showPbLoading(false)
    }

    private fun initUI() {
        with(binding.inToolbar.toolbar) {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        with(binding.rvCommentList) {
            adapter = commentAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
        showPbLoading(true)
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            rvCommentList.isVisible = !show
        }
    }
}