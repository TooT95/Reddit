package com.example.reddit.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.reddit.R
import com.example.reddit.databinding.FragmentSubredditListBinding
import com.example.reddit.utils.Utils

class SubredditListFragment :
    BaseFragment<FragmentSubredditListBinding>(FragmentSubredditListBinding::inflate) {

    private var isNew = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initNewPopularListener()
    }

    private fun initNewPopularListener() {
        with(binding) {
            val itemClickListener = View.OnClickListener {
                when (it) {
                    txtNewSubreddit -> {
                        if (isNew) return@OnClickListener
                        setUnsetTextApperance(txtNewSubreddit, txtPopularSubreddit)
                    }
                    txtPopularSubreddit -> {
                        if (!isNew) return@OnClickListener
                        setUnsetTextApperance(txtPopularSubreddit, txtNewSubreddit)
                    }
                }
                isNew = !isNew
            }
            txtNewSubreddit.setOnClickListener(itemClickListener)
            txtPopularSubreddit.setOnClickListener(itemClickListener)
        }
    }

    private fun setUnsetTextApperance(setTextView: TextView, unsetTextView: TextView) {
        if (Utils.haveM()) {
            setTextView.setTextAppearance(R.style.TextOnboardWithColor14sp)
            unsetTextView.setTextAppearance(R.style.TextOnboardWithoutColor14sp)
        }
    }

    private fun showPbLoading(show: Boolean) {
        with(binding) {
            linearSubredditSearch.isVisible = !show
            pbLoading.isVisible = show
        }
    }

}