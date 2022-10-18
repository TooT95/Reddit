package com.example.reddit.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.reddit.R
import com.example.reddit.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.btnAuth.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_OAuthFragment)
        }

    }
}