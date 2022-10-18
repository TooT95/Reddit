package com.example.reddit.ui.fragment

import android.os.Bundle
import android.view.View
import com.example.reddit.R
import com.example.reddit.databinding.FragmentOnboardItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardItemFragment :
    BaseFragment<FragmentOnboardItemBinding>(FragmentOnboardItemBinding::inflate) {

    private val iconArray =
        listOf(R.drawable.ic_onboard_1, R.drawable.ic_onboard_2, R.drawable.ic_onboard_3)

    companion object {
        private const val POSITION_KEY = "position_key"
        fun newInstance(position: Int): OnboardItemFragment {
            val args = Bundle().apply {
                putInt(POSITION_KEY, position)
            }
            return OnboardItemFragment().apply {
                arguments = args
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            binding.apply {
                val position = bundle.getInt(POSITION_KEY, 0)
                txtOnboardText.text = resources.getTextArray(R.array.text_onboard_text)[position]
                txtOnboardDesc.text = resources.getTextArray(R.array.text_onboard_desc)[position]
                ivOnboard.setImageResource(iconArray[position])
            }
        }
    }

}