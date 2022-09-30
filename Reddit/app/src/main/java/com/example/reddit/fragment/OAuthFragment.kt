package com.example.reddit.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.reddit.databinding.FragmentOauthBinding
import com.example.reddit.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OAuthFragment : BaseFragment<FragmentOauthBinding>(FragmentOauthBinding::inflate) {

    var DEVICE_ID: String = UUID.randomUUID().toString()
    var authCode = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        auth()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun auth2() {
        val baseUrl = Utils.oAuthUrl
        binding.webv.also {
            it.settings.javaScriptEnabled = true
            it.loadUrl(baseUrl)
            it.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    view?.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { urlPageFinished ->
                        if (urlPageFinished.contains("?code=") || urlPageFinished.contains("&code")) {
                            authCode = Uri.parse(url).getQueryParameter("code") ?: ""
                            Utils.setAuthCode(requireContext(), authCode)
                            toast(authCode)
                        } else if (urlPageFinished.contains("error=access_denied")) {
                            Utils.setAuthCode(requireContext(), authCode)
                            toast(authCode)
                        }

                    }
                }
            }

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun auth() {

        val baseUrl = Utils.oAuthUrl
        binding.webv.also {
            it.settings.javaScriptEnabled = true
            it.loadUrl(baseUrl)
            it.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    view?.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { urlPageFinished ->
                        if (urlPageFinished.contains("?code=") || urlPageFinished.contains("&code")) {
                            authCode = Uri.parse(url).getQueryParameter("code") ?: ""
                            Utils.setAuthCode(requireContext(), authCode)

                        } else if (urlPageFinished.contains("error=access_denied")) {
                            Utils.setAuthCode(requireContext(), authCode)

                        }

                    }
                }
            }

        }
    }
}