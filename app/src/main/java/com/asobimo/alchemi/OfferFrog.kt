package com.asobimo.alchemi

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.asobimo.alchemi.sourcedata.MyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OfferFrog : Fragment() {
    private lateinit var wv: WebView
    private lateinit var asdf: ValueCallback<Array<Uri?>>
    val fdsa = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        asdf.onReceiveValue(it.toTypedArray())
    }
    private val myViewModel:MyViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val frogview = inflater.inflate(R.layout.fragment_offer_frog, container, false)

        wv = frogview.findViewById(R.id.webView)
        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true)
        CookieManager.getInstance().setAcceptCookie(true)

        wv.loadUrl(getYrl())
        wv.settings.domStorageEnabled = true
        wv.settings.javaScriptEnabled = true
        wv.settings.loadWithOverviewMode = false
        val ss = WebView(requireContext()).settings.userAgentString.replace("wv", "")
        wv.settings.userAgentString = ss

        wv.webChromeClient = object : WebChromeClient(){
            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>>, fileChooserParams: FileChooserParams?): Boolean {
                asdf = filePathCallback
                val x = "image"
                val y = "/*"
                fdsa.launch(x+y)
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                val nwv = WebView(requireContext())
                val statusTrue = true
                nwv.webChromeClient = this
                nwv.settings.javaScriptCanOpenWindowsAutomatically = statusTrue
                nwv.settings.javaScriptEnabled = statusTrue
                nwv.settings.domStorageEnabled = statusTrue
                nwv.settings.setSupportMultipleWindows(statusTrue)
                val avtomobil = resultMsg.obj as WebView.WebViewTransport
                avtomobil.webView = nwv
                resultMsg.sendToTarget()
                return true
            }
        }

        wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val containsToChtoNeNuzno = "https://catslovefood.today/nibiru.php"
                CookieManager.getInstance().flush()
                    if (!myViewModel.fileManipulation.file.exists() && !url?.contains(containsToChtoNeNuzno)!!) {
                        myViewModel.polozhitVFile(url)
                    }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (wv.canGoBack()) {
                    wv.goBack()
                }
            }
        })

        return frogview
    }

    private fun getYrl():String{
        return  requireArguments().getString(YRL_VALUE)!!
    }

    companion object {
        private val YRL_VALUE = "YRL_VALUE"
        @JvmStatic
        fun newInstance(yrlValue:String):OfferFrog{
            val args = Bundle()
            args.putString(YRL_VALUE, yrlValue)
            val frog = OfferFrog()
            frog.arguments = args
            return frog
        }
    }
}