package com.example.digitracksdk.presentation.web_view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.webkit.WebViewAssetLoader
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityWebViewBinding
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeRequestModel
import com.example.digitracksdk.presentation.home.client_policy.ClientPoliciesViewModel
import com.example.digitracksdk.presentation.home.client_policy.model.PolicyStatusType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel


class WebViewActivity : BaseActivity(), DialogUtils.DialogManager {
    lateinit var binding: ActivityWebViewBinding
    private var isPdf: Boolean = false
    var url: String = ""
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    var policyId: String? = ""
    var screenName: String? = ""
    private val clientPoliciesViewModel: ClientPoliciesViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    val documentPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (uploadMessage == null)
                return@registerForActivityResult
            val res = if (intent == null || result.resultCode != RESULT_OK) {
                null
            } else {
                result.data?.data
            }
            if (res != null) {
                uploadMessage?.onReceiveValue(arrayOf(res))
            } else {
                uploadMessage?.onReceiveValue(arrayOf())
            }

            uploadMessage = null;
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
        setUpWebView()
        setUpObserver()
        setUpListener()
    }

    private fun setUpObserver() {

        with(clientPoliciesViewModel)
        {
            insertAcknowledgeResponseData.observe(this@WebViewActivity) {
                toggleLoader(false)
                if (it.status == Constant.SUCCESS) {
                    DialogUtils.closeAcknowledgeDialog()
                    showToast(it.Message.toString())
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    DialogUtils.closeAcknowledgeDialog()
                    showToast(it.Message.toString())
                }

                messageData.observe(this@WebViewActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }

                showProgressBar.observe(this@WebViewActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                setUpWebView()
            }
            btnAccept.setOnClickListener {
                showTermConditionDialog()
            }
            onBackPressedDispatcher.addCallback(this@WebViewActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (binding.webView.canGoBack()) {
                            binding.webView.goBack();
                        } else if (screenName == getString(R.string.induction_training) || screenName == getString(
                                R.string.coc
                            )
                        ) {
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            finish()
                        }
                    }
                })
        }
    }

    private fun showTermConditionDialog() {
        DialogUtils.showAcknowledgeDialog(this, this)
    }

    override fun onConfirmClick(isCheck: Boolean) {
        super.onConfirmClick(isCheck)
        if (isCheck) {
            callInsertAcknowledgePolicyApi()
        } else {
            showToast(getString(R.string.please_select_term_and_condition))
        }
    }

    private fun callInsertAcknowledgePolicyApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            clientPoliciesViewModel.callInsertAcknowledgeApi(getInsertAcknowledgeRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getInsertAcknowledgeRequestModel(): AcknowledgeRequestModel {
        val request = AcknowledgeRequestModel()
        request.ClientId = preferenceUtils.getValue(Constant.PreferenceKeys.ClientID)
        request.GNETAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        request.PolicyId = policyId?.toInt() ?: 0
        return request
    }

    private fun getIntentData() {
        intent.extras?.run {
            isPdf = getBoolean(Constant.IS_PDF, false)

            screenName = getString(Constant.SCREEN_NAME)
            val link = getString(Constant.WEB_URL)

            url = if (isPdf) {
                "http://docs.google.com/gview?embedded=true&url=$link"
            } else {
                link.toString()
            }
            setUpToolbar(screenName ?: getString(R.string.app_name))
            if (screenName == getString(R.string.view_policy)) {
                policyId = getString(Constant.POLICY_ID)
                val policyStatus = getString(Constant.POLICY_STATUS)
                when (policyStatus?.toInt()) {
                    PolicyStatusType.ACKNOWLEDGED.value -> {  //yes
                        binding.btnAccept.visibility = GONE
                    }

                    PolicyStatusType.PENDING.value -> { //no
                        binding.btnAccept.visibility = VISIBLE
                    }
                }
            }

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                webView.apply {
                    webViewClient = MyWebViewClient(this@WebViewActivity)
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.javaScriptEnabled = true
                    settings.pluginState = WebSettings.PluginState.ON //Always >Kitkat
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.allowFileAccessFromFileURLs = true
                    settings.allowUniversalAccessFromFileURLs = true
                    settings.setGeolocationDatabasePath(filesDir.path)
                    val assetLoader: WebViewAssetLoader = WebViewAssetLoader.Builder()
                        .addPathHandler(
                            "/assets/",
                            WebViewAssetLoader.AssetsPathHandler(this@WebViewActivity)
                        )
                        .build()
                    webChromeClient = object : WebChromeClient() {

                        override fun onGeolocationPermissionsShowPrompt(
                            origin: String,
                            callback: GeolocationPermissions.Callback
                        ) {
                            callback.invoke(origin, true, false)
                        }

                        fun shouldInterceptRequest(
                            webView: WebView?,
                            request: WebResourceRequest
                        ): WebResourceResponse? {
                            return assetLoader.shouldInterceptRequest(request.url)
                        }


                        override fun onPermissionRequest(request: PermissionRequest) {
                            request.grant(request.resources)
                        }

                        override fun onShowFileChooser(
                            webView: WebView?,
                            filePathCallback: ValueCallback<Array<Uri>>?,
                            fileChooserParams: FileChooserParams?
                        ): Boolean {
                            try {
                                uploadMessage = filePathCallback
                                val intent = fileChooserParams?.createIntent()
                                if (intent != null) {
                                    documentPicker.launch(intent)
                                }

                            } catch (e: Exception) {
                                uploadMessage = null
                                showToast("Cannot Open File Chooser")
                                return false

                            }

                            return true
                        }
                    }
                }
                webView.loadUrl(url)
            } else {
                layoutNoInternet.root.visibility = VISIBLE
            }
        }
    }

    private fun setUpToolbar(screenName: String) {
        binding.toolbar.apply {
            tvTitle.text = screenName
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                if (screenName == getString(R.string.induction_training) || screenName == getString(
                        R.string.coc
                    )
                ) {
                    setResult(Activity.RESULT_OK)
                }
                finish()
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    inner class MyWebViewClient(val activity: Activity) : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url.toString()
            view?.loadUrl(url)
            toggleLoader(true)
            return true
        }


        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                view?.loadUrl(url)
                toggleLoader(true)
            }
            return true
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            showToast(error.toString())
            toggleLoader(false)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            toggleLoader(false)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            toggleLoader(false)
                view?.evaluateJavascript(
                    "(function() { return document.body.innerText; })();"
                ) { htmlText ->
                    if (htmlText.contains("digilocker-success")) {
                        Log.e("PageFinished", "Success");
                        setResult(Activity.RESULT_OK)
                        finish()

                    }
                }
        }
    }

}