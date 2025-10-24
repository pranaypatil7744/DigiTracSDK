package com.example.digitracksdk.presentation.home.innov_id_card

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityInnovIdCardBinding
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeRequestModel
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class InnovIdCardActivity : BaseActivity() {
    lateinit var binding: ActivityInnovIdCardBinding
    private val innovIDCardViewModel: InnovIDCardViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInnovIdCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setObserver()
        callQrCodeApi()
        callInnovIDCardApi()
    }

    private fun callQrCodeApi() {

        if (isNetworkAvailable()){
            toggleLoader(true)
            innovIDCardViewModel.callQrCodeApi(
                QrCodeRequestModel(PreferenceUtils(this).getValue(
                    Constant.PreferenceKeys.GnetAssociateID))
            )
        }else{
            showToast(getString(R.string.no_internet_connection))
        }

    }

    private fun setObserver() {
        binding.apply {
            with(innovIDCardViewModel) {

                innovIDCardResponseData.observe(this@InnovIdCardActivity) {
                    toggleLoader(false)
                    setUpUserData(it)
                }
                qrCodeResponseData.observe(this@InnovIdCardActivity)
                {
                    toggleLoader(false)
                    if(it.status.lowercase().equals(Constant.success ,true))
                    {
                        ImageUtils.INSTANCE?.loadBannerImage(imgQrCode,it.qRCodeImagePath)
                    }else{
                        showToast(it.message)
                    }

                }


                messageData.observe(this@InnovIdCardActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUserData(data: InnovIDCardResponseModel) {
        binding.apply {
            tvBloodGroupValue.text = ": ${data.bloodGroup}"
            tvDojValue.text = ": ${AppUtils.INSTANCE?.convertDateFormat("dd-MMM-yyyy",data.dateofJoining.toString(),"dd MMM yyyy")}"
            tvLocationValue.text = ": ${data.locationName}"
            tvClientNameValue.text = ": ${data.clientName}"
            tvEmergencyContactValue.text = ": ${data.emergencyNumber}"
            tvName.text = "${data.associateFirstName} ${data.associateMiddleName} ${data.associateLastName}"
            tvDesignation.text = data.designation
//            ImageUtils.INSTANCE?.loadRemoteImage(imgProfile, data.picture)
            val profilePic = ImageUtils.INSTANCE?.stringToBitMap(preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC))
            ImageUtils.INSTANCE?.loadBitMap(imgCustomerProfile,profilePic)
        }
    }

    private fun callInnovIDCardApi() {
        if (isNetworkAvailable()){
            toggleLoader(true)
            innovIDCardViewModel.callInnovIDCardApi(
                InnovIDCardRequestModel(PreferenceUtils(this).getValue(
                    Constant.PreferenceKeys.INNOV_ID))
            )
        }else{
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(binding.root, loader = binding.contentLoading.root, binding.contentLoading.imageLoading, showLoader)
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }

            divider.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.innov_id_card)
        }
    }
}