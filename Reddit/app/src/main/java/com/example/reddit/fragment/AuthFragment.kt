package com.example.reddit.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.setFragmentResult
import com.example.reddit.databinding.AuthDialogBinding
import com.example.reddit.databinding.FragmentAuthBinding
import com.example.reddit.utils.Utils
import timber.log.Timber
import java.util.*

class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    var DEVICE_ID: String = UUID.randomUUID().toString()
    val OAUTH_URL =
        "https://www.reddit.com/api/v1/authorize"
    val CLIENT_ID = "gnpcyrAKl8k8AMR6fVjeGg"
    val REDIRECT_URI = "http://www.example.com/unused/redirect/uri"
    val OAUTH_SCOPE = "read"
    var authCode = ""
    val AUTH_STATE = "MobileApp"
    var authComplete = false
    lateinit var alertDialog:AlertDialog
    val resultIntent = Intent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initUI() {
        binding.btnAuth.setOnClickListener {
            val dialogBinding = AuthDialogBinding.inflate(layoutInflater)
            val authDialog = AlertDialog.Builder(requireContext())
            authDialog.setView(dialogBinding.root)
            val baseUrl =
                "$OAUTH_URL?client_id=$CLIENT_ID&response_type=code&state=$AUTH_STATE&redirect_uri=$REDIRECT_URI&duration=temporary&scope=identity"
            dialogBinding.webv.also {
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

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Timber.d("errorReceiver: ${error!!.description}")
                        }
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        url?.let { urlPageFinished ->
                            if (urlPageFinished.contains("?code=") || urlPageFinished.contains("&code")) {
                                val uri = Uri.parse(url)
                                authCode = uri.getQueryParameter("code") ?: ""
                                Timber.d("CODE : $authCode")
                                authComplete = true
                                resultIntent.putExtra("code", authCode)
                                requireActivity().setResult(Activity.RESULT_OK, resultIntent)
                                requireActivity().setResult(Activity.RESULT_CANCELED, resultIntent)
                                Utils.setAuthCode(requireContext(), authCode)
                                toast("Authorization Code is: $authCode")
                                alertDialog.dismiss()
                            } else if (urlPageFinished.contains("error=access_denied")) {
                                Timber.d("ACCESS_DENIED_HERE $urlPageFinished")
                                Utils.setAuthCode(requireContext(), authCode)
                                authComplete = true
                                requireActivity().setResult(Activity.RESULT_CANCELED, resultIntent)
                                toast("Error Occured")
                                alertDialog.dismiss()
                            }
                        }
                    }
                }

            }

            alertDialog = authDialog.show()
            authDialog.setTitle("Authorize")
            authDialog.setCancelable(true)
        }
    }
}