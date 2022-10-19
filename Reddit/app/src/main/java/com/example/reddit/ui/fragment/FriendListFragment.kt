package com.example.reddit.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reddit.R
import com.example.reddit.ui.adapter.FriendListAdapter
import com.example.reddit.databinding.FragmentFriendListBinding
import com.example.reddit.ui.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendListFragment :
    BaseFragment<FragmentFriendListBinding>(FragmentFriendListBinding::inflate) {

    private val viewModel: AccountViewModel by viewModels()
    private val friendListAdapter: FriendListAdapter by lazy {
        FriendListAdapter { userName ->
            val bundle = Bundle().apply {
                putString(UserFragment.KEY_USER_NAME, userName)
            }
            findNavController().navigate(R.id.action_friendListFragment_to_userFragment,
                bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFriendList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.friendListLiveData.observe(viewLifecycleOwner) { friendList ->
            showPbLoading(false)
            friendListAdapter.submitList(friendList)
            friendList.forEach {
                viewModel.getFriendInfo(it.name)
            }
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.friendInfoLiveData.observe(viewLifecycleOwner) { account ->
            val currentList = friendListAdapter.currentList
            val index = currentList.indexOfFirst {
                it.name == account.name
            }
            currentList[index].avatarUrl = account.avatarUrl
            friendListAdapter.submitList(currentList)
            friendListAdapter.notifyItemChanged(index)
        }
    }

    private fun initUI() {
        showPbLoading(true)
        with(binding.rvFriendList) {
            adapter = friendListAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        with(binding.inToolbar.toolbar) {
            title = resources.getString(R.string.text_friend_list)
            setTitleTextColor(resources.getColor(R.color.primaryTextColor,
                resources.newTheme()))
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            rvFriendList.isVisible = !show
        }
    }
}