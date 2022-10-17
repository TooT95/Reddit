package com.example.reddit.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reddit.R
import com.example.reddit.adapter.SubredditListAdapter
import com.example.reddit.adapter.SubredditListingAdapter
import com.example.reddit.databinding.FragmentLikedBinding
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.SubredditListViewModel
import com.example.reddit.viewmodel.SubredditListingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikedFragment : BaseFragment<FragmentLikedBinding>(FragmentLikedBinding::inflate) {

    private var isSubreddit = true
    private var isLoading = false
    private val viewModel: SubredditListViewModel by viewModels()
    private val listingViewModel: SubredditListingViewModel by viewModels()
    private val srListAdapter: SubredditListAdapter by lazy {
        SubredditListAdapter(::itemClickListener)
    }
    private val srListingAdapter: SubredditListingAdapter by lazy {
        SubredditListingAdapter(::onItemClickListener)
    }

    private fun onItemClickListener(item: SubredditListing, listenerType: ListenerType) {
        if (listenerType == ListenerType.SAVE
            || listenerType == ListenerType.UNSAVE
        ) {
            listingViewModel.saveUnsaveListing(item, srListingAdapter.currentList.indexOf(item))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSubscribedSubredditList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.subredditListLiveData.observe(viewLifecycleOwner, ::refreshSubreddit)
        viewModel.subredditNewListLiveData.observe(viewLifecycleOwner, ::uploadSubreddit)
        viewModel.subredditSubscribeLiveData.observe(viewLifecycleOwner, ::subscribeSubreddit)
        listingViewModel.subredditSavedListingLiveData.observe(viewLifecycleOwner,
            ::refreshSubredditListing)
        listingViewModel.listingSaveLiveData.observe(viewLifecycleOwner, ::saveUnSaveChanged)
    }

    private fun saveUnSaveChanged(index: Int?) {
        refreshSubredditListing(srListingAdapter.currentList.filterIndexed { i, _ ->
            i != index
        })
    }

    private fun refreshSubredditListing(listing: List<SubredditListing>) {
        srListingAdapter.submitList(listing)
        showPbLoading(false)
        if (listing.isEmpty()) {
            showTextAvailable(true)
            return
        }
        setAdapter()

    }

    private fun subscribeSubreddit(index: Int?) {
        index?.let { it ->
            val item = srListAdapter.currentList[it]
            toast("Unsubscribed to ${item.title}")
            srListAdapter.submitList(srListAdapter.currentList.filter { subreddit ->
                subreddit.id != item.id
            })
        }
        if (srListAdapter.currentList.isEmpty()) {
            showTextAvailable(true)
        }
    }

    private fun refreshSubreddit(srList: List<Subreddit>) {
        showPbLoading(false)
        if (srList.isEmpty()) {
            showTextAvailable(true)
            return
        }
        showTextAvailable(false)
        srListAdapter.submitList(srList)
        setAdapter()
    }

    private fun uploadSubreddit(srList: List<Subreddit>) {
        srListAdapter.submitList(srListAdapter.currentList + srList)
        showBottomLoading(false)
    }

    private fun initUI() {
        showPbLoading(true)
        initSubredditCommentListener()
        setAdapter()
    }

    private fun setAdapter() {
        with(binding.rvSubredditList) {
            if (isSubreddit) {
                adapter = srListAdapter
            } else {
                adapter = srListingAdapter
            }

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            if (isSubreddit) {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val layManager = layoutManager as LinearLayoutManager
                        if (!isLoading && layManager.findLastCompletelyVisibleItemPosition() == (srListAdapter.currentList.size - 1)
                            && srListAdapter.currentList.size > 20
                        ) {
                            loadMore()
                            showBottomLoading(true)
                        }
                    }
                })
            }
        }
    }

    private fun itemClickListener(item: Subreddit, listenerType: ListenerType) {
        when (listenerType) {
            ListenerType.OPEN_SUBREDDIT -> {
                val bundle = Bundle().apply {
                    putString(SubredditListingFragment.KEY_SUBREDDIT_NAME,
                        item.displayName)
                }
                findNavController().navigate(R.id.action_likedSubredditFragment_to_subredditListingFragment,
                    bundle)
            }
            ListenerType.SUBSCRIBE -> {
                viewModel.subUnSubSubreddit(srListAdapter.currentList.indexOf(item), item)
            }
            else -> {}
        }
    }

    private fun loadMore() {
        if (Utils.SUB_AFTER == "null") {
            showBottomLoading(false)
            return
        }
        if (isSubreddit) {
            viewModel.getSubscribedSubredditList(Utils.SUB_AFTER, true)
        }
        Utils.SUB_AFTER = ""
    }

    private fun initSubredditCommentListener() {
        with(binding) {
            val itemClickListener = View.OnClickListener {
                when (it) {
                    txtSubredditList -> {
                        if (isSubreddit) return@OnClickListener
                        showPbLoading(true)
                        viewModel.getSubscribedSubredditList()
                    }
                    txtCommentList -> {
                        if (!isSubreddit) return@OnClickListener
                        showPbLoading(true)
                        listingViewModel.getSavedListing(Utils.account!!.name)
                    }
                }
                isSubreddit = !isSubreddit
                setUnsetTextAppearance()
            }
            txtSubredditList.setOnClickListener(itemClickListener)
            txtCommentList.setOnClickListener(itemClickListener)
        }
    }

    private fun setUnsetTextAppearance() {
        if (Utils.haveM()) {
            with(binding) {
                if (isSubreddit) {
                    txtSubredditList.setTextAppearance(R.style.TextSubredditListWithColor14sp)
                    txtCommentList.setTextAppearance(R.style.TextSubredditListWithoutColor14sp)
                } else {
                    txtCommentList.setTextAppearance(R.style.TextSubredditListWithColor14sp)
                    txtSubredditList.setTextAppearance(R.style.TextSubredditListWithoutColor14sp)
                }
            }
        }
    }

    private fun showPbLoading(show: Boolean) {
        showTextAvailable(!show)
        with(binding) {
            rvSubredditList.isVisible = !show
            pbLoading.isVisible = show
        }
    }

    private fun showTextAvailable(show: Boolean) {
        with(binding) {
            rvSubredditList.isVisible = !show
            txtNotFoundDetail.isVisible = show
        }
    }

    private fun showBottomLoading(show: Boolean) {
        binding.pbLoadingBottom.isVisible = show
        isLoading = show
    }
}