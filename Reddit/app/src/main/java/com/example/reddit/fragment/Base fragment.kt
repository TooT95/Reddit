package com.example.reddit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import timber.log.Timber

open class BaseFragment<T : ViewBinding>(private val bindingInflater: (layoutInflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> T) :
    Fragment() {

    private var _binding: T? = null
    val binding: T
        get() = _binding!!

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = bindingInflater.invoke(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun toast(text: String) {
        Timber.d("toast $text")
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}