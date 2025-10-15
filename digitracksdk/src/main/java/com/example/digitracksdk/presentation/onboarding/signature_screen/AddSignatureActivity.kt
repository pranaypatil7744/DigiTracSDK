package com.example.digitracksdk.presentation.onboarding.signature_screen

import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityAddSignatureBinding
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureRequestModel
import com.example.digitracksdk.presentation.my_letters.offer_letter.OfferLetterViewModel
import com.example.digitracksdk.presentation.my_letters.offer_letter.model.OfferLetterModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class AddSignatureActivity : BaseActivity() {
    lateinit var binding: ActivityAddSignatureBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val signatureViewModel: PaperlessViewGetSignatureViewModel by viewModel()
    private val offerLetterViewModel: OfferLetterViewModel by viewModel()
    var offerLetterModel: OfferLetterModel? = null
    var isFromOfferLetter:Boolean = false
    var gnetAssociateId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppUtils.INSTANCE?.setLang(this)
        binding = ActivityAddSignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpObserver()
        getIntentData()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(offerLetterViewModel){
                offerLetterAcceptRejectResponseData.observe(this@AddSignatureActivity) {
                    if (it.status == Constant.success) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                showProgress.observe(this@AddSignatureActivity) {
                    toggleLoader(it)
                }

                messageData.observe(this@AddSignatureActivity) {
                    showToast(it.toString())
                }
            }

            with(signatureViewModel) {
                insertSignatureResponseData.observe(this@AddSignatureActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@AddSignatureActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun getPreferenceData() {
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromOfferLetter = getBoolean(Constant.IS_FROM_OFFER_LETTER,false)
            offerLetterModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(Constant.DATA, OfferLetterModel::class.java)
            }else
                getSerializable(Constant.DATA) as OfferLetterModel?
        }
        setUpToolbar()
    }

    private fun setUpListener() {
        binding.apply {
            btnClear.setOnClickListener {
                signatureView.clear()
            }
            btnSave.setOnClickListener {
                if (!checkAck.isChecked) {
                    showToast(getString(R.string.please_select_term_condition))
                } else if (signatureView.isEmpty) {
                    showToast(getString(R.string.please_draw_signature))
                } else {
                    val image = ImageUtils.INSTANCE?.bitMapToString(signatureView.signatureBitmap)
                    image?.let { it1 ->
                        if (isFromOfferLetter){
                            callAcceptOfferLetterApi(it1)
                        }else{
                            callInsertSignatureApi(it1)
                        }
                    }
                }
            }
        }
    }

    private fun callAcceptOfferLetterApi(image: String) {
        if (isNetworkAvailable()) {
            offerLetterViewModel.callOfferLetterAcceptRejectApi(
                request = OfferLetterAcceptRejectRequestModel(
                    AcceptStatus = "true",
                    GNETAssociateID = gnetAssociateId,
                    ImageDocArray = image,
                    OfferID = offerLetterModel?.OfferId?.toInt()?:0,
                    OfferPatternID = offerLetterModel?.OfferPatternId?.toInt()?:0,
                    OfferType = offerLetterModel?.CandidateType.toString()
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callInsertSignatureApi(image: String) {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            signatureViewModel.callInsertSignatureApi(
                request = InsertSignatureRequestModel(
                    ImageDocArray = image,
                    preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID), "", getString(R.string.accepted)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            if (isFromOfferLetter){
                tvTitle.text = getString(R.string.signature_upload)

            }else{
                tvTitle.text = getString(R.string.add_signature)
            }
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
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
}