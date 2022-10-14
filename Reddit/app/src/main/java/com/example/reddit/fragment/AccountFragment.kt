package com.example.reddit.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.reddit.databinding.FragmentAccountBinding
import com.example.reddit.extension.glideImageWithParams
import com.example.reddit.model.Account
import com.example.reddit.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModels()

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
    }

    private fun observeViewModels() {
        viewModel.accountLiveData.observe(viewLifecycleOwner, ::showAccountInfo)
        viewModel.accountSubredditCountLiveData.observe(viewLifecycleOwner) {
            binding.txtCommentSubreddit.text = "Сабреддиты: $it"
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    @SuppressLint("SetTextI18n")
    private fun showAccountInfo(account: Account) {
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
            materialAccSub.isVisible = !show
        }
    }
}