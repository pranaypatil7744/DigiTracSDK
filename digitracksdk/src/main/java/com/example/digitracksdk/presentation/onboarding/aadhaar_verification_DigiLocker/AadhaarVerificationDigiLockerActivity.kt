package com.example.digitracksdk.presentation.onboarding.aadhaar_verification_DigiLocker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAadhaarVerificationBinding
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.Data
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDRequestModel
import com.example.digitracksdk.presentation.my_profile.adapter.MyProfileAdapter
import com.example.digitracksdk.presentation.my_profile.model.ProfileModel
import com.example.digitracksdk.presentation.onboarding.aadhar_verification.AadhaarVerificationViewModel
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerRequestModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


class AadhaarVerificationDigiLockerActivity : BaseActivity(), ValidationListener,
    MyProfileAdapter.ProfileClickManager {
    lateinit var binding: ActivityAadhaarVerificationBinding
    var isView: Boolean = false
    private var aadhaarDetails: ArrayList<Data> = ArrayList()

    lateinit var preferenceUtils: PreferenceUtils
    private var innovId = ""
    var profilePic: String? = ""
    var mobile: String? = ""

    private val aadhaarVerificationViewModel: AadhaarVerificationViewModel by viewModel()

    private val aadhaarDigiLockerViewModel: AadhaarDigiLockerViewModel by viewModel()

    //for view profile
    private var profileDetailsList: ArrayList<InnovIDCardResponseModel> = ArrayList()
    private lateinit var myProfileAdapter: MyProfileAdapter
    private var profileItemsList: ArrayList<ProfileModel> = ArrayList()
    lateinit var validateAadhaarRequestModel: ValidateAadhaarRequestModel
    var requestId: String? = ""
    var aadhaarUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAadhaarVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        aadhaarVerificationViewModel.listener = this
        getPreferenceData()
        getIntentData()
        setUpToolbar()
        setUpObserverData()
        setUpListener()
        setUpViewData()
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                isView = getBoolean(Constant.IS_VIEW, false)
            }
        }
    }

    private fun setUpViewData() {
        profileItemsList.clear()
        profileItemsList.add(
            ProfileModel(
                summaryDetailsType = MyProfileAdapter.SummaryDetailsType.EMP_PROFILE_DETAILS
            )
        )
        profileItemsList.add(
            ProfileModel(
                summaryDetailsType = MyProfileAdapter.SummaryDetailsType.EMP_PROFILE_DETAILS_ITEM
            )
        )
    }

    private fun setUpAdapter() {
        binding.apply {
            myProfileAdapter = MyProfileAdapter(
                this@AadhaarVerificationDigiLockerActivity,
                profileItemsList,
                profileDetailsList,
                this@AadhaarVerificationDigiLockerActivity,
                isFromVerifyAadhaar = true,
                aadhaarDetails = aadhaarDetails
            )
            recyclerMyProfile.adapter = myProfileAdapter
        }
    }

    private fun getPreferenceData() {
        profilePic = preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC)
        mobile = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }


    private fun setUpObserverData() {
        binding.apply {
            with(aadhaarVerificationViewModel) {
                validateAadhaarResponseModel.observe(this@AadhaarVerificationDigiLockerActivity) {
                    if (it.status?.lowercase() == Constant.success && it.Message == Constant.Valid) {
                        callGetAadhaarDataApi()
                    } else {
                        showToast(getString(R.string.aadhaar_number_is_not_valid))
                    }
                }



                messageData.observe(this@AadhaarVerificationDigiLockerActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@AadhaarVerificationDigiLockerActivity) {
                    toggleLoader(it)
                }
            }

            with(aadhaarDigiLockerViewModel)
            {
                aadhaarData.observe(this@AadhaarVerificationDigiLockerActivity) {
                    if (it.status?.lowercase() == Constant.success) {
                        aadhaarDetails.clear()
//                        profileItemsList.removeAt(1)
//                        it.data?.let { it1 -> aadhaarDetails.add(ResultModel(UserFullName = it1)) }
                        it.data?.let { it1 -> aadhaarDetails.add(it1) }
                        setUpViewProfileData()
                    } else {
                        callDigiLockerApi()
                    }
                }


                aadhaarDigiLockerData.observe(this@AadhaarVerificationDigiLockerActivity) {
                    if (it.success == true) {
                        aadhaarUrl = it.sdk_url
                        requestId = it.request_id
                        callRequestIDApi(it.request_id)
                    } else {
                        showToast(it.response_message.toString())
                    }
                }
                saveDigiLockerRequestIdData.observe(this@AadhaarVerificationDigiLockerActivity) {
                    if (it.status?.lowercase() == Constant.success) {
                        val intent = Intent(
                            this@AadhaarVerificationDigiLockerActivity,
                            WebViewActivity::class.java
                        )
//                        intent.putExtra("bannerLink", aadhaarUrl)

                        val b = Bundle()
                        b.putString(Constant.WEB_URL, aadhaarUrl)
                        b.putString(Constant.SCREEN_NAME, getString(R.string.aadhaar_verification))
                        intent.putExtras(b)
                        refreshForResult.launch(intent)
                    } else {
                        showToast(it.Message.toString())
                    }


                }

                messageData.observe(this@AadhaarVerificationDigiLockerActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@AadhaarVerificationDigiLockerActivity) {
                    toggleLoader(it)
                }
            }

        }
    }


    private val refreshForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    callGetAadhaarDataApi()
                }
            }
        }

    private fun callRequestIDApi(requestId: String?) {

        binding.apply {
            if (isNetworkAvailable()) {
                val request = SaveDigiLockerRequestIDRequestModel(
                    InnovId = innovId,
                    RequestID = requestId,
                    AadharNumber = binding.etAadhaarCardNumber.text.toString()
                )
                aadhaarDigiLockerViewModel.callSaveRequestIdApi(
                    request
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callDigiLockerApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                val request = DigiLockerRequestModel(
                    docs = listOf("ADHAR"),
                    purpose = "<<purpose of the request>>, mandatory",
                    response_url = "https://paperlessonboardinglive.innov.in/API/SaveAdharVeificationWithDigiLockerResponse",
                    redirect_url = "https://innov.fmdigione.com/CandidateRegisterNew/DigiLockerRdirectforMobile",
                    fast_track = "Y",
                    pinless = true
                )
                aadhaarDigiLockerViewModel.callAadhaarDigiLockerApi(
                    request
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callGetAadhaarDataApi() {


        binding.apply {
            if (isNetworkAvailable()) {
                val request = GetAadhaarDetailDigiLockerRequestModel(
                    RequestID = requestId,
                    InnovID = innovId,
                    AadhaarNo = etAadhaarCardNumber.text.toString().trim()
                )
                aadhaarDigiLockerViewModel.callGetAadhaarDataApi(
                    request
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }


    }

    private fun setUpViewProfileData() {
        binding.apply {
            recyclerMyProfile.visibility = VISIBLE
            toolbar.tvLeftTitle1.visibility = INVISIBLE
            etAadhaarCardNumber.isEnabled = false
            checkAck.isEnabled = false
            setUpAdapter()
        }
    }


    private fun getAadhaarVerificationSendOtpRequestModel(): AadhaarVerificationSendOtpRequestModel {
        binding.apply {
            val request = AadhaarVerificationSendOtpRequestModel()
            request.AadhaarNumber = etAadhaarCardNumber.text.toString().trim()
            request.Consent = "Y"
            request.ConsentText =
                "I hear by declare my consent agreement for fetching my information for testing purpose."
            request.InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            return request
        }
    }

    private fun setUpListener() {
        binding.apply {
            toolbar.tvLeftTitle1.setOnClickListener {
                clearError()
                hideKeyboard(this@AadhaarVerificationDigiLockerActivity)
                aadhaarVerificationViewModel.verifyAadhaarDetails(
                    getAadhaarVerificationSendOtpRequestModel()
                )
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.aadhaar_verification)
            divider.visibility = VISIBLE
            tvLeftTitle1.text = getString(R.string.save)
            tvLeftTitle1.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvLeftTitle1.setTextColor(
                ContextCompat
                    .getColor(this@AadhaarVerificationDigiLockerActivity, R.color.blue_ribbon)
            )
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

    override fun onValidationSuccess(type: String, msg: Int) {
        if (binding.checkAck.isChecked)
            callValidateAadhaarApi()
        else
            showToast(getString(R.string.please_select_term_condition))
    }

    private fun callValidateAadhaarApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                validateAadhaarRequestModel = ValidateAadhaarRequestModel(
                    InnovID = innovId,
                    AadharNo = etAadhaarCardNumber.text.toString().trim()
                )
                aadhaarVerificationViewModel.callValidateAadhaarApi(
                    validateAadhaarRequestModel
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }


    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.AADHAR_NUMBER_ERROR -> {
                    layoutAadhaarCardNumber.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutAadhaarCardNumber.error = ""
        }
    }

    override fun onEditBtnClick(position: Int) {

    }
}