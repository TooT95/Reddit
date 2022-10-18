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
    }

    private fun userInfo(user: Account) {
        with(binding) {
            txtAccountName.text = user.name
            txtAccountPrefix.text = user.id
            txtCommentNum.text = user.karma.toString()

            materialFriendFollowed.setOnClickListener {

            }
            materialFriendUnfollowed.setOnClickListener {
            }
            ivAvatar.glideImageWithParams(requireView(), user.avatarUrl)
        }
        showPbLoading(false)
        showFollowUnFollow(user)
    }

    private fun initUI() {
        with(binding) {
            txtAccountName.text = userName
            ivBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }
        showPbLoading(true)
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            pbLoading.isVisible = show
            constraintMain.isVisible = !show
        }
    }

    private fun showFollowUnFollow(user: Account) {
        with(binding) {
            materialFriendFollowed.isVisible = user.isFriend
            materialFriendFollowed.isVisible = user.isFriend
        }
    }

    companion object {
        const val KEY_USER_NAME = "user_name_to_follow"
    }
}