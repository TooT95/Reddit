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
import com.example.reddit.databinding.FragmentSubredditListBinding
import com.example.reddit.extension.changeFlow
import com.example.reddit.model.ListenerType
import com.example.reddit.model.subreddit.Subreddit
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.SubredditListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditListFragment :
    BaseFragment<FragmentSubredditListBinding>(FragmentSubredditListBinding::inflate) {

    private var isNew = true
    private var isLoading = false
    private val viewModel: SubredditListViewModel by viewModels()
    private val srListAdapter: SubredditListAdapter by lazy {
        SubredditListAdapter(::itemClickListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNewSubredditList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
        setUnsetTextAppearance()
    }

    private fun observeViewModels() {
        viewModel.subredditListLiveData.observe(viewLifecycleOwner, ::refreshSubreddit)
        viewModel.subredditNewListLiveData.observe(viewLifecycleOwner, ::uploadSubreddit)
        viewModel.subredditSearchListLiveData.observe(viewLifecycleOwner, ::searchSubreddit)
        viewModel.subredditSubscribeLiveData.observe(viewLifecycleOwner, ::subscribeSubreddit)
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    private fun subscribeSubreddit(index: Int?) {
        index?.let {
            val item = srListAdapter.currentList[it]
            item.userSubscriber =
                !item.userSubscriber
            srListAdapter.notifyItemChanged(it)
            toast("${if (item.userSubscriber) "Subscribed" else "Unsubscribed"} to ${item.title}")
        }

    }

    private fun refreshSubreddit(srList: List<Subreddit>) {
        srListAdapter.submitList(srList)
        showPbLoading(false)
    }

    private fun searchSubreddit(srList: List<Subreddit>?) {
        if (srList == null) {
            if (isNew) {
                viewModel.getNewSubredditList()
            } else {
                viewModel.getPopularSubredditList()
            }
        } else {
            if (srList.isNotEmpty()) {
                srListAdapter.submitList(srList)
                showPbLoading(false)
            }
        }
    }

    private fun uploadSubreddit(srList: List<Subreddit>) {
        srListAdapter.submitList(srListAdapter.currentList + srList)
        showBottomLoading(false)
    }

    private fun initUI() {
        showPbLoading(true)
        initNewPopularListener()
        setAdapter()

        viewModel.searchSubreddit(binding.svSearch.changeFlow())
    }

    private fun setAdapter() {
        with(binding.rvSubredditList) {
            adapter = srListAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layManager = layoutManager as LinearLayoutManager
                    if (!isLoading && layManager.findLastCompletelyVisibleItemPosition() == (srListAdapter.currentList.size - 1)) {
                        loadMore()
                        showBottomLoading(true)
                    }
                }
            })
        }
    }

    private fun loadMore() {
        if (isNew) {
            viewModel.getNewSubredditList(Utils.SUB_AFTER, true)
        } else {
            viewModel.getPopularSubredditList(Utils.SUB_AFTER, true)
        }
        Utils.SUB_AFTER = ""
    }

    private fun initNewPopularListener() {
        with(binding) {
            val itemClickListener = View.OnClickListener {
                when (it) {
                    txtNewSubreddit -> {
                        if (isNew) return@OnClickListener
                        showPbLoading(true)
                        viewModel.getNewSubredditList()
                    }
                    txtPopularSubreddit -> {
                        if (!isNew) return@OnClickListener
                        showPbLoading(true)
                        viewModel.getPopularSubredditList()
                    }
                }
                isNew = !isNew
                setUnsetTextAppearance()
            }
            txtNewSubreddit.setOnClickListener(itemClickListener)
            txtPopularSubreddit.setOnClickListener(itemClickListener)
        }
    }

    private fun setUnsetTextAppearance() {
        if (Utils.haveM()) {
            with(binding) {
                if (isNew) {
                    txtNewSubreddit.setTextAppearance(R.style.TextSubredditListWithColor14sp)
                    txtPopularSubreddit.setTextAppearance(R.style.TextSubredditListWithoutColor14sp)
                } else {
                    txtPopularSubreddit.setTextAppearance(R.style.TextSubredditListWithColor14sp)
                    txtNewSubreddit.setTextAppearance(R.style.TextSubredditListWithoutColor14sp)
                }
            }
        }
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            rvSubredditList.isVisible = !show
            pbLoading.isVisible = show
        }
    }

    private fun showBottomLoading(show: Boolean) {
        binding.pbLoadingBottom.isVisible = show
        isLoading = show
    }

    private fun itemClickListener(item: Subreddit, listenerType: ListenerType) {
        when (listenerType) {
            ListenerType.OPEN_SUBREDDIT -> {
                val bundle = Bundle().apply {
                    putString(SubredditListingFragment.KEY_SUBREDDIT_NAME,
                        item.displayName)
                }
                findNavController().navigate(R.id.action_subredditListFragment_to_subredditListingFragment,
                    bundle)
            }
            ListenerType.SUBSCRIBE -> {
                viewModel.subUnSubSubreddit(srListAdapter.currentList.indexOf(item), item)
            }
            else -> {}
        }
    }

}