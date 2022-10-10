package com.example.reddit.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddit.R
import com.example.reddit.adapter.SubredditListAdapter
import com.example.reddit.databinding.FragmentSubredditListBinding
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.SubredditListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditListFragment :
    BaseFragment<FragmentSubredditListBinding>(FragmentSubredditListBinding::inflate) {

    private var isNew = true
    private val viewModel: SubredditListViewModel by viewModels()
    private val srListAdapter: SubredditListAdapter by lazy {
        SubredditListAdapter {
            val bundle = Bundle().apply {
                putString(SubredditListingFragment.KEY_SUBREDDIT_NAME,
                    srListAdapter.currentList[it].displayName)
            }
            findNavController().navigate(R.id.action_subredditListFragment_to_subredditListingFragment,
                bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNewSubredditList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.subredditListLiveData.observe(viewLifecycleOwner) {
            srListAdapter.submitList(it)
            showPbLoading(false)
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    private fun initUI() {
        showPbLoading(true)
        initNewPopularListener()
        setAdapter()
    }

    private fun setAdapter() {
        with(binding.rvSubredditList) {
            adapter = srListAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initNewPopularListener() {
        with(binding) {
            val itemClickListener = View.OnClickListener {
                when (it) {
                    txtNewSubreddit -> {
                        if (isNew) return@OnClickListener
                        showPbLoading(true)
                        setUnsetTextApperance(txtNewSubreddit, txtPopularSubreddit)
                        viewModel.getNewSubredditList()
                    }
                    txtPopularSubreddit -> {
                        if (!isNew) return@OnClickListener
                        showPbLoading(true)
                        setUnsetTextApperance(txtPopularSubreddit, txtNewSubreddit)
                        viewModel.getPopularSubredditList()
                    }
                }
                isNew = !isNew
            }
            txtNewSubreddit.setOnClickListener(itemClickListener)
            txtPopularSubreddit.setOnClickListener(itemClickListener)
        }
    }

    private fun setUnsetTextApperance(setTextView: TextView, unsetTextView: TextView) {
        if (Utils.haveM()) {
            setTextView.setTextAppearance(R.style.TextSubredditListWithColor14sp)
            unsetTextView.setTextAppearance(R.style.TextSubredditListWithoutColor14sp)
        }
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            rvSubredditList.isVisible = !show
            pbLoading.isVisible = show
        }
    }

}