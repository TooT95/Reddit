package com.example.reddit.extension

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.addReplaceFragment(
    layoutId: Int,
    fragment: Fragment,
    fragmentTag: String,
    replace: Boolean = false,
) {
    beginTransaction().apply {
        if (replace) replace(layoutId, fragment, fragmentTag)
        else add(layoutId, fragment, fragmentTag)
    }.commit()
}