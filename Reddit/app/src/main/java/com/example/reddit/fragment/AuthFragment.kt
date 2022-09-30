package com.example.reddit.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.reddit.R
import com.example.reddit.databinding.FragmentAuthBinding


class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    fun initUI() {
        binding.btnAuth.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_OAuthFragment)
        }

    }
}