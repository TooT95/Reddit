package com.example.reddit.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.reddit.databinding.FragmentSubredditListingBinding
import com.example.reddit.viewmodel.SubredditListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditListingFragment :
    BaseFragment<FragmentSubredditListingBinding>(FragmentSubredditListingBinding::inflate) {

    private val viewModel: SubredditListViewModel by viewModels()
    private var srName = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        arguments?.let {
            srName = it.getString(KEY_SUBREDDIT_NAME, "")
        }
        with(binding.inToolbar.toolbar) {
            title = srName
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        viewModel.getSubredditListing(srName)
    }

    companion object {
        const val KEY_SUBREDDIT_NAME = "subreddit name"
    }

}