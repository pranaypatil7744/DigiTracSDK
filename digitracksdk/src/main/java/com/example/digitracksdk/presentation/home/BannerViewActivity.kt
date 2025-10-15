package com.example.digitracksdk.presentation.home
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityBannerViewBinding
import com.example.digitracksdk.utils.AppUtils
import java.util.*

class BannerViewActivity : BaseActivity() {
    private lateinit var appContext: Context
    lateinit var binding: ActivityBannerViewBinding
    private var link: String? = ""
    private var isForPolicy: Boolean = false
    var uploadMessage: ValueCallback<Array<Uri>>? = null

    var fileRequest = registerForActivityResult(StartActivityForResult()) { result ->

        if (null == uploadMessage) return@registerForActivityResult
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
        uploadMessage = null
//        if (result.resultCode == RESULT_OK && result.data != null) {
//            if (uploadMessage == null)
//                return@registerForActivityResult
//            uploadMessage?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(result.resultCode, intent))
//            uploadMessage = null
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBannerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        appContext = this@BannerViewActivity
        AppUtils.INSTANCE?.setLang(this@BannerViewActivity)
        setUpToolBar()
        getIntentData()
        setUplistener()
        setupUi()

    }

    private fun setUplistener() {
        onBackPressedDispatcher.addCallback(this@BannerViewActivity,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                with(binding) {
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish()
                    }
                }
            }
        })
    }

    private fun setUpToolBar() {
        binding.layoutToolbar.apply {
            tvTitleCenter.text = getString(R.string.app_name)
            tvTitleCenter.visibility= View.VISIBLE
            tvTitleCenter.setTextColor(ContextCompat.getColor(this@BannerViewActivity, R.color.cod_gray))
            tvTitle.visibility= View.GONE
          /*  homeToolbar.setBackgroundColor(
                ContextCompat.getColor(
                    this@BannerViewActivity,
                    R.color.blue_ribbon
                )
            )*/

//            btnBack.drawable.setTint(ContextCompat.getColor(this@BannerViewActivity, R.color.white))

            divider.visibility = VISIBLE
//            tvTitle.text = getString(R.string.rewards)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUi() {
        with(binding) {

            /*    if (isForPolicy){
                    Utility().setUpPaperlessActionBar(this, getString(R.string.view_policy))
                }else{
                    Utility().setUpPaperlessActionBar(this, getString(R.string.app_name))
                }*/

            //Start this timer when you create you task
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


//        val bannerLink = "http://digioneutility.innov.in/AssociateInsurance.aspx?innovid=1383133"
//        val bannerLink = "http://associatesurvey.innov.in/?innovid=1383133"
//        val bannerLink = Constants.TC
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true;
            webview.settings.allowFileAccess = true
            webview.settings.allowContentAccess = true
            webview.settings.allowFileAccessFromFileURLs = true
            webview.settings.allowUniversalAccessFromFileURLs = true
            webview.settings.setGeolocationDatabasePath(filesDir.path)
            webview.settings.pluginState = WebSettings.PluginState.ON

            webview.addJavascriptInterface(
                WebAppBannerInterface(this@BannerViewActivity),
                "Android"
            )

            webview.webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String,
                    callback: GeolocationPermissions.Callback
                ) {
                    callback.invoke(origin, true, false)
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
                            fileRequest.launch(intent)
                        }
                    } catch (e: Exception) {
                        uploadMessage = null
                        Toast.makeText(
                            this@BannerViewActivity,
                            "Cannot Open File Chooser",
                            Toast.LENGTH_LONG
                        ).show()
                        return false
                    }
                    return true
                }

            }
            toggleLoader(true)
            webview.webViewClient = MyWebViewClient(this@BannerViewActivity)

            if (isForPolicy) {
                if (link.isNullOrEmpty()) {
                    Toast.makeText(
                        appContext,
                        getString(R.string.no_policy_found),
                        Toast.LENGTH_LONG
                    ).show()
                    toggleLoader(false)
                } else {

//                "http://docs.google.com/gview?embedded=true&url=$link"
//                webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+link.toString())

                    webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + link.toString())

                }
            } else {

                webview.loadUrl(link ?: "")
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

    private fun getIntentData() {
        intent.extras?.run {
//            isForPolicy = getBoolean(InnovConstant.IS_FOR_POLICY, false)
            link =
//                if (isForPolicy) {
//                getString(InnovConstant.Policy_url)
//            } else {
                intent.getStringExtra("bannerLink")
//            }
        }
    }

    inner class MyWebViewClient internal constructor(private val activity: Activity) :
        WebViewClient() {
@TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }


        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            val e = error?.errorCode
            if (request?.isForMainFrame == true) {
                showSomethingWentWrong()
            }
            toggleLoader(false)
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            // Handle the error For Less Than API M
            toggleLoader(false)
            showSomethingWentWrong()
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            // Handle the error For Greater  Than API M

            super.onReceivedHttpError(view, request, errorResponse)
            toggleLoader(false)
            if (request?.isForMainFrame == true) {
                showSomethingWentWrong()
            }
        }

//        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//            val run = Runnable {
//                if (timeout) {
//                    // do what you want
//                        closeLoadingDialog()
//                    Toast.makeText(appContext,"Connection Timed out",Toast.LENGTH_SHORT).show()
//                }
//            }
//            val myHandler = Looper.myLooper()?.let { Handler(it) }
//            myHandler?.postDelayed(run, 5000)
//        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            toggleLoader(false)
        }


        private fun showSomethingWentWrong() {
            Toast.makeText(
                activity,
                activity.getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun backToRefreshBackScreen() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this.onBackPressed()
        }
        return true
    }

    class WebAppBannerInterface(digiAssistActivity: BannerViewActivity) {

        var mContext: Context? = null

        /** Instantiate the interface and set the context  */
        fun WebAppInterface(c: Context?) {
            mContext = c
        }

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String?) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }

}
