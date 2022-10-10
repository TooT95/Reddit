package com.example.reddit.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddit.R
import com.example.reddit.adapter.SubredditListingAdapter
import com.example.reddit.databinding.FragmentSubredditListingBinding
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.SubredditListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditListingFragment :
    BaseFragment<FragmentSubredditListingBinding>(FragmentSubredditListingBinding::inflate) {

    private val viewModel: SubredditListViewModel by viewModels()
    private var srName = ""
    private val listingAdapter: SubredditListingAdapter by lazy {
        SubredditListingAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            srName = it.getString(KEY_SUBREDDIT_NAME, "")
        }
        viewModel.getSubredditListing(srName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.subredditListingLiveData.observe(viewLifecycleOwner, ::refreshSrListing)
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    private fun refreshSrListing(srList: List<SubredditListing>) {
        listingAdapter.submitList(srList)
        showLoading(false)
    }

    private fun initUI() {
        with(binding.inToolbar.toolbar) {
            title = srName
            if (Utils.haveM()) {
                setTitleTextColor(resources.getColor(R.color.primaryTextColor,
                    resources.newTheme()))
            }
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        with(binding.rvListingList) {
            adapter = listingAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            rvListingList.isVisible = !show
        }
    }

    companion object {
        const val KEY_SUBREDDIT_NAME = "subreddit name"
    }

}