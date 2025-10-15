package com.example.digitracksdk.presentation.onboarding.aadhar_verification

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityAadhaarVerificationBinding
import com.innov.digitrac.databinding.BottomSheetVerifyOtpBinding
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ResultModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.my_profile.adapter.MyProfileAdapter
import com.example.digitracksdk.presentation.my_profile.model.ProfileModel
import com.example.digitracksdk.receiver.SMSBroadcastReceiver
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class AadhaarVerificationActivity : BaseActivity(), ValidationListener,
    MyProfileAdapter.ProfileClickManager {
    lateinit var binding: ActivityAadhaarVerificationBinding
    private lateinit var verifyOtpDialog: BottomSheetDialog
    private var countDownTimer: CountDownTimer? = null
    var isResend: Boolean = false
    var isView: Boolean = false
    private var aadhaarDetails: ArrayList<ResultModel> = ArrayList()

    lateinit var smsBroadcastReceiver: SMSBroadcastReceiver
    lateinit var preferenceUtils: PreferenceUtils
    private lateinit var bottomSheetVerifyOtpBinding: BottomSheetVerifyOtpBinding
    private var innovId = ""
    var profilePic: String? = ""
    var mobile: String? = ""
    private var aadhaarVerifyRequestId = ""

    private val aadhaarVerificationViewModel: AadhaarVerificationViewModel by viewModel()

    //for view profile
    private var profileDetailsList: ArrayList<InnovIDCardResponseModel> = ArrayList()
    private lateinit var myProfileAdapter: MyProfileAdapter
    private var profileItemsList: ArrayList<ProfileModel> = ArrayList()
    lateinit var validateAadhaarRequestModel: ValidateAadhaarRequestModel


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
                this@AadhaarVerificationActivity,
                profileItemsList,
                profileDetailsList,
                this@AadhaarVerificationActivity,
                isFromVerifyAadhaar = true,

//                aadhaarDetails
            )
            recyclerMyProfile.adapter = myProfileAdapter
        }
    }

    private fun getPreferenceData() {
        profilePic = preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC)
        mobile = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SMSBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener =
            object : SMSBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    if (intent != null) {
                        smsIntent.launch(intent)
                    }
                }

                override fun onFailure() {
                }
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(smsBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(smsBroadcastReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        }


    }

    private fun fetchVerificationCode(message: String?): String? {
        return Regex("(\\d{4})").find(message ?: "")?.value
    }

    val smsIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val message: String = it.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE) ?: ""
            if (!TextUtils.isEmpty(message)) {
                val code = fetchVerificationCode(message)
                if (!code.isNullOrEmpty()) {
                    bottomSheetVerifyOtpBinding.etOtp.setText(code)
                    bottomSheetVerifyOtpBinding.btnVerify.callOnClick()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerToSmsBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    private fun setUpObserverData() {
        binding.apply {
            with(aadhaarVerificationViewModel) {
                validateAadhaarResponseModel.observe(this@AadhaarVerificationActivity) {
                    if (it.status?.lowercase() == Constant.success && it.Message == Constant.Valid) {

                        callGetAadhaarVerificationDetailsApi(validateAadhaarRequestModel)
                    } else {
                        showToast(getString(R.string.aadhaar_number_is_not_valid))
                    }
                }
                getAadhaarVerificationDetailsResponseModel.observe(this@AadhaarVerificationActivity) {
                    if (it.status?.lowercase() == Constant.success) {
                        aadhaarDetails.clear()
                        profileItemsList.removeAt(1)
                        it.UserFullName?.let { it1 -> aadhaarDetails.add(ResultModel(UserFullName = it1)) }
                        setUpViewProfileData()
                    } else {
                        callAadhaarVerificationSendOtpApi()
                    }
                }
                aadhaarVerificationSendOtpResponseData.observe(this@AadhaarVerificationActivity) {
                    if (it.Status?.lowercase() == Constant.success) {
                        aadhaarVerifyRequestId = it.RequestId.toString()
                        if (it.IsOtpSent == true) {
                            if (!isResend) {
                                openVerifyOtpBottomSheet()
                            }
                        } else {
                            showToast(it.ResponseMessage.toString())
                        }
                    } else {
                        showToast(it.ResponseMessage.toString())
                    }
                }

                aadhaarVerificationOtpValidationResponseData.observe(
                    this@AadhaarVerificationActivity
                ) {
                    if (it.Success == "True") {
                        verifyOtpDialog.dismiss()
                        showToast(it.ResponseMessage.toString())
                        aadhaarDetails.clear()
                        it.Result?.let { it1 -> aadhaarDetails.add(it1) }
                        setUpViewProfileData()
                    } else {
                        showToast(it.ResponseMessage.toString())
                    }
                }

                messageData.observe(this@AadhaarVerificationActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@AadhaarVerificationActivity) {
                    toggleLoader(it)
                }
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

    private fun startResendOtpCountDownTimer() {

        countDownTimer = object : CountDownTimer(Constant.THIRTY_SEC, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val timerToUse = (millisUntilFinished / 1000).toString()
                bottomSheetVerifyOtpBinding.tvTimer.text = AppUtils.INSTANCE?.convertDateFormat(
                    dateFormatToRead = "ss",
                    dateToRead = timerToUse,
                    dateFormatToConvert = "mm:ss",
                    timeZone = TimeZone.getDefault(),
                    localTimeZone = TimeZone.getDefault()
                )
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                bottomSheetVerifyOtpBinding.tvTimer.visibility = View.GONE
                bottomSheetVerifyOtpBinding.tvResendOtp.visibility = VISIBLE
            }
        }.start()
    }


    @SuppressLint("SetTextI18n")
    private fun openVerifyOtpBottomSheet() {
        verifyOtpDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_verify_otp, null)
        bottomSheetVerifyOtpBinding = BottomSheetVerifyOtpBinding.bind(view)
        verifyOtpDialog.apply {
            setContentView(view)
            setCancelable(false)
            startResendOtpCountDownTimer()
            show()
        }
        bottomSheetVerifyOtpBinding.apply {
            btnClose.visibility = VISIBLE
            etOtp.filters = arrayOf<InputFilter>(LengthFilter(6))
            tvEnterOtp.text =
                getString(R.string.enter_6_digit_otp_sent_to) + " " + getString(R.string.your_register_mobile_no)

            btnClose.setOnClickListener {
                verifyOtpDialog.dismiss()
            }
            btnVerify.setOnClickListener {

                val otp = etOtp.text?.trim()
                if (otp.isNullOrEmpty()) {
                    layoutOtp.error = getString(R.string.please_enter_otp)
                } else if (otp.length < 6) {
                    layoutOtp.error = getString(R.string.please_enter_6_digit_otp)
                } else {
                    layoutOtp.error = ""
                    callAadhaarOtpValidationApi()
                }
            }
            tvResendOtp.setOnClickListener {
                tvTimer.visibility = VISIBLE
                tvResendOtp.visibility = INVISIBLE
                startResendOtpCountDownTimer()
                isResend = true
                callAadhaarVerificationSendOtpApi()
            }
        }
    }

    private fun getAadhaarOtpValidationRequestModel(): AadhaarVerificationOtpValidationRequestModel {
        val request = AadhaarVerificationOtpValidationRequestModel()
        request.Consent = "Y"
        request.ConsentText =
            "I hear by declare my consent agreement for fetching my information for testing purpose."
        request.InnovId = innovId
        request.OTP = bottomSheetVerifyOtpBinding.etOtp.text.toString().trim()
        request.RequestId = aadhaarVerifyRequestId
        return request
    }

    private fun callAadhaarOtpValidationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                aadhaarVerificationViewModel.callAadhaarVerificationOtpValidationApi(
                    getAadhaarOtpValidationRequestModel()
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
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
                hideKeyboard(this@AadhaarVerificationActivity)
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
                    .getColor(this@AadhaarVerificationActivity, R.color.blue_ribbon)
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
//            callAadhaarVerificationSendOtpApi()
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

    private fun callAadhaarVerificationSendOtpApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                aadhaarVerificationViewModel.callAadhaarVerificationSendOtpApi(
                    getAadhaarVerificationSendOtpRequestModel()
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