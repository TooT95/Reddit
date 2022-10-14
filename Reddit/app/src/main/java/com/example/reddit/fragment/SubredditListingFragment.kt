package com.example.reddit.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.adapter.SubredditListingAdapter
import com.example.reddit.databinding.FragmentSubredditListingBinding
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.SubredditListingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditListingFragment :
    BaseFragment<FragmentSubredditListingBinding>(FragmentSubredditListingBinding::inflate) {

    private val viewModel: SubredditListingViewModel by viewModels()
    private var srName = ""
    private var isLoading = false
    private val listingAdapter: SubredditListingAdapter by lazy {
        SubredditListingAdapter(::onItemClickListener)
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
        viewModel.subredditNewListingLiveData.observe(viewLifecycleOwner, ::refreshNewSrListing)
        viewModel.listingSaveLiveData.observe(viewLifecycleOwner, ::saveListing)
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    private fun saveListing(index: Int?) {
        if (index != null) {
            listingAdapter.currentList[index].saved = !listingAdapter.currentList[index].saved
            listingAdapter.notifyItemChanged(index)
        }
    }

    private fun refreshSrListing(srList: List<SubredditListing>) {
        listingAdapter.submitList(srList)
        showLoading(false)
    }

    private fun refreshNewSrListing(srList: List<SubredditListing>) {
        listingAdapter.submitList(listingAdapter.currentList + srList)
        showBottomLoading(false)
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
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layManager = layoutManager as LinearLayoutManager
                    if (!isLoading && layManager.findLastCompletelyVisibleItemPosition() == (listingAdapter.currentList.size - 1)
                        && listingAdapter.currentList.size > 10
                    ) {
                        loadMore()
                        showBottomLoading(true)
                    }
                }

            })
        }
    }

    private fun loadMore() {
        viewModel.getSubredditListing(srName, Utils.SUB_LISTING_AFTER, true)
        Utils.SUB_LISTING_AFTER = ""
    }

    private fun showLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            rvListingList.isVisible = !show
            if (!show) {
                rvListingList.isVisible = listingAdapter.currentList.isNotEmpty()
                txtNotFoundDetail.isVisible = listingAdapter.currentList.isEmpty()
            }
        }
    }

    private fun showBottomLoading(show: Boolean) {
        binding.pbLoadingBottom.isVisible = show
        isLoading = show
    }

    companion object {
        const val KEY_SUBREDDIT_NAME = "subreddit name"
    }

    private fun onItemClickListener(item: SubredditListing, listenerType: ListenerType) {
        if (listenerType == ListenerType.SAVE
            || listenerType == ListenerType.UNSAVE
        ) {
            viewModel.saveUnsaveListing(item, listingAdapter.currentList.indexOf(item))
        }
    }

}