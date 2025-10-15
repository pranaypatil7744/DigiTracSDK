package com.example.digitracksdk.presentation.pf_esic_insurance.pf_esic_insurance_view_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.Constant.PermissionRequestCodes.Companion.STORAGE_PERMISSION_CODE
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityEsicCardBinding
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PermissionUtils

class PfEsicInsuranceViewActivity : BaseActivity(), DialogUtils.DialogManager {
    lateinit var binding: ActivityEsicCardBinding
    var toolbarTitle: String = ""
    var docImage: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEsicCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        getIntentData()
        setUpListener()
    }

    private fun setUpListener() {
        onBackPressedDispatcher.addCallback(this@PfEsicInsuranceViewActivity,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun setUpView() {
        //TODO solve image view issue
        if (PermissionUtils.getStoragePermission(this)) {
            val pdfFile = ImageUtils.INSTANCE?.writePDFToFile(docImage,toolbarTitle)
            if (pdfFile != null) {
                ImageUtils.INSTANCE?.displayFile(pdfFile, binding.imgDoc, this)
            }
        } else {
            PermissionUtils.requestStoragePermissions(this)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults.first() == PackageManager.PERMISSION_GRANTED){
            setUpView()
        }else{
            DialogUtils.showPermissionDialog(
                this,getString(R.string.please_grant_the_storage_permission_to_continue),getString(R.string.allow_permission),getString(R.string.go_to_settings),getString(R.string.deny),true,listener = this
            )
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            toolbarTitle = getString(Constant.ITEM_NAME).toString()
            docImage = getString(Constant.SHARE_IMAGE).toString()
            setUpToolbar()
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = toolbarTitle
            divider.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            btnOne.apply {
                visibility = VISIBLE
                setImageResource(R.drawable.ic_download)
                setOnClickListener{
                    val file = docImage.let { it1 ->
                        ImageUtils.INSTANCE?.writePDFToFile(it1,toolbarTitle.toString())
                    }
                    ImageUtils.INSTANCE?.openPdfFile(
                        this@PfEsicInsuranceViewActivity,
                        file?.absolutePath.toString()
                    )
                }
            }
        }
//        setUpWebView()
        setUpView()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.apply {
            webView.webViewClient = MyWebViewClient(this@PfEsicInsuranceViewActivity)
            webView.settings.javaScriptEnabled = true
//            val pdfFile = "https://file-examples-com.github.io/uploads/2017/10/file-sample_150kB.pdf"
            val pdfFile = ImageUtils.INSTANCE?.writePDFToFile(docImage,toolbarTitle)
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=$pdfFile")
        }

    }

    class MyWebViewClient(val activity: Activity) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request?.url.toString()
            view?.loadUrl(url)
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                view?.loadUrl(url)
            }
            return true
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {

        }
    }
}