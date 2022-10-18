package com.example.reddit.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.reddit.databinding.FragmentUserBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.model.Account
import com.example.reddit.ui.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>(FragmentUserBinding::inflate) {

    private var userName: String = ""
    private lateinit var defUser: Account
    private val viewModel: AccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(KEY_USER_NAME, "")
        }
        viewModel.getFriendInfo(userName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.friendInfoLiveData.observe(viewLifecycleOwner, ::userInfo)
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.accountFollowLiveData.observe(viewLifecycleOwner, ::accountFollow)
    }

    private fun accountFollow(followed: Boolean) {
        defUser.isFriend = followed
        showFollowUnFollow(followed)
        showFollowLoading(false)
    }

    private fun userInfo(user: Account) {
        defUser = user
        with(binding) {
            txtAccountName.text = defUser.name
            txtAccountPrefix.text = defUser.id
            txtCommentNum.text = defUser.karma.toString()

            materialFriendFollowed.setOnClickListener {
                viewModel.unFollow(userName, !defUser.isFriend)
                showFollowLoading(true)
            }
            materialFriendUnfollowed.setOnClickListener {
                viewModel.follow(userName, !defUser.isFriend)
                showFollowLoading(true)
            }
            ivAvatar.glideImageWithParams(requireView(), defUser.avatarUrl)
        }
        showPbLoading(false)
        showFollowUnFollow(defUser.isFriend)
    }

    private fun initUI() {
        with(binding) {
            txtAccountName.text = userName
            ivBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        showPbLoading(true)
        showFollowLoading(false)
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            constraintMain.isVisible = !show
        }
    }

    private fun showFollowLoading(show: Boolean) {
        with(binding) {
            pbFollowLoading.isVisible = show
            linearFollowUnfollow.isVisible = !show
        }
    }

    private fun showFollowUnFollow(followed: Boolean) {
        with(binding) {
            materialFriendFollowed.isVisible = followed
            materialFriendUnfollowed.isVisible = !followed
        }
    }

    companion object {
        const val KEY_USER_NAME = "user_name_to_follow"
    }
}