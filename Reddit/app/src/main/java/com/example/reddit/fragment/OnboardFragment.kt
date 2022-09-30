package com.example.reddit.fragment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.reddit.R
import com.example.reddit.adapter.OnboardStateAdapter
import com.example.reddit.databinding.FragmentOnboardingBinding
import com.example.reddit.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnboardFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.apply {
            val animDrawable = constraintOnboard.background as AnimationDrawable
            animDrawable.setEnterFadeDuration(2500)
            animDrawable.setExitFadeDuration(1000)
            animDrawable.start()

            val adapter = OnboardStateAdapter(requireActivity())
            viewpagerOnboard.adapter = adapter
            includeToolbar.toolbar.apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item_skip -> {
                            viewpagerOnboard.currentItem = viewpagerOnboard.currentItem + 1
                            if (viewpagerOnboard.currentItem == 2) {
                                findNavController().navigate(R.id.action_loginFragment_to_employerMainFragment)
                                Utils.saveOnboardPassed(requireContext(), true)
                            }
                            return@setOnMenuItemClickListener false
                        }
                        else -> {
                            return@setOnMenuItemClickListener false
                        }
                    }
                }
            }

            viewpagerOnboard.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val item = includeToolbar.toolbar.menu.findItem(R.id.item_skip)
                    when (position) {
                        2 -> {
                            item.setTitle(R.string.text_ready)
                        }
                        else -> {
                            item.setTitle(R.string.text_skip)
                        }
                    }

                }
            })

            springDotsIndicator.attachTo(viewpagerOnboard)
        }
    }
}