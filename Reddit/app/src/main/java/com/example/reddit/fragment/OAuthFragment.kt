package com.example.reddit.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reddit.R
import com.example.reddit.databinding.FragmentOauthBinding
import com.example.reddit.utils.Utils
import com.example.reddit.viewmodel.NetworkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OAuthFragment : BaseFragment<FragmentOauthBinding>(FragmentOauthBinding::inflate) {

    private val viewModel: NetworkViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        auth()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.accessTokenGotLiveData.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_OAuthFragment_to_mainFragment)
            }
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun auth() {

        val baseUrl = Utils.oAuthUrl
        binding.webv.also { webView ->
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(baseUrl)
            webView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    val url = request?.url.toString()
                    view?.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { urlPageFinished ->
                        if (urlPageFinished.contains("?code=") || urlPageFinished.contains("&code")) {
                            val authCode = Uri.parse(url).getQueryParameter("code") ?: ""
                            Utils.setAuthCode(requireContext(), authCode)
                            webView.isVisible = false
                            viewModel.getAuthToken()
                        } else if (urlPageFinished.contains("error=access_denied")) {
                            Utils.setAuthCode(requireContext(), "")
                        }
                    }
                }
            }

        }
    }
}