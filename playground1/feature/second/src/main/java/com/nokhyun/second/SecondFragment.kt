package com.nokhyun.second

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.nokhyun.second.databinding.FragmentSecondBinding


/** TossPayment Webview 날 것의 테스트. */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding: FragmentSecondBinding get() = _binding!!
    private var money: Int = 0
    private val imm : InputMethodManager by lazy {
        requireContext().getSystemService(InputMethodManager::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.run {
            lifecycleOwner = this@SecondFragment.viewLifecycleOwner

            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etMoney.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrBlank()) return@doOnTextChanged
            money = text.toString().toInt()
        }


        binding.btnPay.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            binding.wvToss.loadUrl("javascript:pay(${money.let { if(it.toString().isEmpty()) 0 else it }})")
        }

        binding.wvToss.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    logger { request?.url.toString() }
                    return request?.url?.let {
                        val url = it
                        val isPaymentFail = if (url.getQueryParameter("isTossApp").isNullOrEmpty()) {
                            false
                        } else {
                            !url.getQueryParameter("isTossApp")!!.toBoolean()
                        }

                        if (isPaymentFail) {
                            Toast.makeText(requireContext(), "실패했지롱.", Toast.LENGTH_SHORT).show()
                        }

                        isPaymentFail
                    } ?: false
                }
            }
        }.loadUrl("file:///android_asset/tossPayments.html")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

internal fun logger(msg: () -> String) {
    Log.e("Log.e", msg())
}
