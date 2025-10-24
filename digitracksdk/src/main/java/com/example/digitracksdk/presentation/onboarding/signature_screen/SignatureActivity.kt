package com.example.digitracksdk.presentation.onboarding.signature_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivitySignatureBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.presentation.image_view.ImageViewActivity
import com.example.digitracksdk.presentation.onboarding.document.DocumentDetailsActivity.Companion.viewImage
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class SignatureActivity : BaseActivity() {
    lateinit var binding: ActivitySignatureBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val paperlessViewGetSignatureViewModel: PaperlessViewGetSignatureViewModel by viewModel()
    var isDocument: Boolean = false
    private var signImage: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        setUpListener()
    }

    override fun onResume() {
        super.onResume()
        callViewSignatureApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(paperlessViewGetSignatureViewModel) {
                viewGetSignatureResponseData.observe(this@SignatureActivity
                ) {
                    if (it.Signature.isNotEmpty() || it.Signature != "null" || it.Signature != null) {
                        isDocument = true
                        signImage = it.Signature
                        btnView.text = getString(R.string.view)
                        etUploadImage.setText(getString(R.string.signature_pdf))
                    } else {
                        isDocument = false
                        btnView.text = getString(R.string.add)
                        etUploadImage.setText("")
                    }
                }

                messageData.observe(this@SignatureActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@SignatureActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewSignatureApi()
            }

            btnView.setOnClickListener {
                if (isDocument) {
                    val b = Bundle()
                    val i = Intent(this@SignatureActivity, ImageViewActivity::class.java)
                    viewImage = signImage
                    b.putBoolean(Constant.IS_IMAGE, true)
//                    b.putString(Constant.IMAGE_PATH, signImage)
                    b.putString(Constant.SCREEN_NAME, getString(R.string.view_signature))
                    i.putExtras(b)
                    startActivity(i)
                } else {
                    startActivity(Intent(this@SignatureActivity, AddSignatureActivity::class.java))
                }
            }
        }
    }

    private fun callViewSignatureApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                layoutDocument.visibility= VISIBLE
                btnView.visibility= VISIBLE
                paperlessViewGetSignatureViewModel.callViewGetSignatureApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutDocument.visibility= GONE
                btnView.visibility= GONE
                binding.layoutNoInternet.root.visibility = VISIBLE
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

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.signature)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }
}