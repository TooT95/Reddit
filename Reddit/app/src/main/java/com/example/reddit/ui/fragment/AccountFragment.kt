package com.example.reddit.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reddit.R
import com.example.reddit.ui.activity.MainActivity
import com.example.reddit.databinding.FragmentAccountBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.model.Account
import com.example.reddit.model.subreddit.SubredditListing
import com.example.reddit.utils.Utils
import com.example.reddit.ui.viewmodel.AccountViewModel
import com.example.reddit.ui.viewmodel.SubredditListingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModels()
    private val viewModelListing: SubredditListingViewModel by viewModels()
    private lateinit var accountMe: Account
    private lateinit var srListing: List<SubredditListing>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAccountInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun initUI() {
        showPbAccountLoading(true)
        with(binding) {
            materialFriendList.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_friendListFragment)
            }
            materialLogout.setOnClickListener {
                val mainActivity = activity as MainActivity
                mainActivity.showMainNavGraph(false, isStart = false)
            }
            materialClearSaved.setOnClickListener {
                showPbAccountLoading(true)
                viewModelListing.getSavedListing(accountMe.name)
            }
        }
    }

    private fun observeViewModels() {
        viewModel.accountLiveData.observe(viewLifecycleOwner) {
            Utils.account = it
            showAccountInfo(Utils.account!!)
        }
        viewModel.accountSubredditCountLiveData.observe(viewLifecycleOwner) {
            binding.txtCommentSubreddit.text = "Сабреддиты: $it"
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModelListing.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModelListing.subredditSavedListingLiveData.observe(viewLifecycleOwner) { subredditListing ->
            srListing = subredditListing
            if (srListing.isEmpty()) {
                toast(resources.getString(R.string.text_clear_saved_successfully))
                showPbAccountLoading(false)
                return@observe
            }
            subredditListing.forEachIndexed { index, listing ->
                viewModelListing.saveUnsaveListing(listing, index)
            }
        }
        viewModelListing.listingSaveLiveData.observe(viewLifecycleOwner) { listingIndex ->
            srListing = srListing.filterIndexed { index, _ ->
                index != listingIndex
            }
            if (srListing.isEmpty()) {
                toast(resources.getString(R.string.text_clear_saved_successfully))
                showPbAccountLoading(false)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAccountInfo(account: Account) {
        accountMe = account
        showPbAccountLoading(false)
        with(binding) {
            txtAccountName.text = account.name
            txtAccountPrefix.text = "@${account.id}"
            ivAvatar.glideImageWithParams(requireView(), account.avatarUrl)
        }
    }

    private fun showPbAccountLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            constraintMain.isVisible = !show
        }
    }
}