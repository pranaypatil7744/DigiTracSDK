package com.example.digitracksdk.presentation.login

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityLoginBinding
import com.innov.digitrac.databinding.BottomSheetVerifyOtpBinding
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.presentation.login.login_fragment.LoginViewModel
import com.example.digitracksdk.presentation.login.verify_otp_fragment.VerifyLoginOtpViewModel
import com.example.digitracksdk.presentation.signup.SignUpActivity
import com.example.digitracksdk.receiver.SMSBroadcastReceiver
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.InnovSingleton
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    var isFromSignUp: Boolean = false
    var isResend: Boolean = false
    private val loginViewModel: LoginViewModel by viewModel()
    private val verifyLoginOtpViewModel: VerifyLoginOtpViewModel by viewModel()
    lateinit var smsBroadcastReceiver: SMSBroadcastReceiver
    var isSmsReceiverOn: Boolean = false
    private lateinit var verifyOtpDialog: BottomSheetDialog
    private lateinit var bottomSheetVerifyOtpBinding: BottomSheetVerifyOtpBinding
    private var countDownTimer: CountDownTimer? = null
    lateinit var preferenceUtils: PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        AppUtils.INSTANCE?.setLang(this)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpListener()
        setObserver()
        startSmsUserConsent()
    }

    private fun setObserver() {
        binding.apply {
            with(loginViewModel) {

                loginResponseData.observe(
                    this@LoginActivity
                ) { it ->
                    if (it.CandidateStatus.equals(Constant.Valid)) {
                        clearLabel()
                        if (isResend) {
                            showToast(getString(R.string.resend_otp_on) + " " + getLoginMobileRequestModel().Mobile)
                        } else {
                            openVerifyOtpBottomSheet()
                        }
                    } else {
                        binding.layoutPhoneEmail.error = getString(R.string.not_a_register_number)
                    }
                }

                firebaseTokenUpdateResponseData.observe(this@LoginActivity) {
                    if (it.Status != Constant.SUCCESS) {
                        showToast(it.Message.toString())
                    }
                }

                verifyLoginOtpViewModel.responseData.observe(this@LoginActivity) {
                    toggleLoaderOtp(false)
                    when {
                        it.OTPStatus.equals(Constant.Valid) -> {
                            clearLabel()
                            preferenceUtils.setValue(Constant.PreferenceKeys.INNOV_ID, it.InnovID)
                            preferenceUtils.setValue(Constant.PreferenceKeys.TOKEN_ID, it.TokenID)
                            preferenceUtils.setValue(Constant.PreferenceKeys.IS_LOGIN, true)
                            if (!preferenceUtils.getValue(Constant.PreferenceKeys.FIREBASE_TOKEN)
                                    .isNullOrEmpty()
                            ) {
                                callUpdateFirebaseTokenApi()
                            }
                            verifyOtpDialog.dismiss()
                            val i = Intent(this@LoginActivity, com.example.digitracksdk.presentation.home.HomeActivity::class.java)
                            i.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(i)
                            finish()
                        }

                        it.OTPStatus.equals(Constant.Invalid) -> {
                            clearLabel()
                            bottomSheetVerifyOtpBinding.layoutOtp.error =
                                getString(R.string.please_enter_valid_otp)
                        }

                        else -> {
                            clearLabel()
                            showToast(getString(R.string.something_went_wrong))
                        }
                    }

                }

                messageData.observe(this@LoginActivity) {
                    showToast(it)
                }
                showProgressbar.observe(this@LoginActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(this).also {
            //We can add user phone number or leave it blank
            it.startSmsUserConsent(null)
                .addOnSuccessListener {
                    AppUtils.INSTANCE?.logMe(
                        "SMS_RETRIEVER",
                        "Sms Retreiver listening for otp success."
                    )

                }
                .addOnFailureListener {
                    AppUtils.INSTANCE?.logMe(
                        "SMS_RETRIEVER",
                        "Sms Retreiver listening for otp failed."
                    )

                }
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.apply {
                setOnClickListener {
                    finish()
                }
            }
        }
    }

    private fun setUpListener() {

        binding.btnLogin.setOnClickListener {
//            isSmsReceiverOn = true
//            registerToSmsBroadcastReceiver()
            if (isValidateMobileNO()) {
                clearLabel()
                callLoginApi()
            } else {
                binding.layoutPhoneEmail.error =
                    getString(R.string.please_enter_valid_phone)
            }
        }
        binding.tvRegister.setOnClickListener {
            startSignUpScreen()
        }
    }

    private fun startSignUpScreen() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun clearLabel() {
        binding.layoutPhoneEmail.error = ""
    }

    private fun isValidateMobileNO(): Boolean {
        val mobileNo = binding.etEmailMob.text.toString().trim()
        return !(mobileNo.isEmpty() || mobileNo.length < 10)
    }

    private fun callLoginApi() {

        if (isNetworkAvailable()) {
            loginViewModel.callLoginApi(getLoginMobileRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getLoginMobileRequestModel(): LoginRequestModel {
        val request = LoginRequestModel()
        request.APKVersion = InnovSingleton.singleton.getVersionInfo(this)
        request.AndroidVersion = InnovSingleton.singleton.androidVersion
        request.BuildNo = InnovSingleton.singleton.osBuildNumber
        request.EmployeeCode = ""
        request.Mobile = binding.etEmailMob.text.toString().trim()
        request.ModelNo = InnovSingleton.singleton.deviceName
        request.SignupSource = "D"
        return request
    }

    fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun toggleLoaderOtp(showLoader: Boolean) {
        toggleFadeView(
            bottomSheetVerifyOtpBinding.root,
            bottomSheetVerifyOtpBinding.contentLoading.root,
            bottomSheetVerifyOtpBinding.contentLoading.imageLoading,
            showLoader
        )
    }

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
            tvMob.text = if (getLoginMobileRequestModel().Mobile.toString()
                    .isEmpty() || getLoginMobileRequestModel().Mobile == "null"
            ) "" else getLoginMobileRequestModel().Mobile

            btnClose.setOnClickListener {
                verifyOtpDialog.dismiss()
            }
            btnVerify.setOnClickListener {
                clearLabel()
                val otp = etOtp.text?.trim()
                if (otp.isNullOrEmpty()) {
                    layoutOtp.error = getString(R.string.please_enter_otp)
                } else if (otp.length < 4) {
                    layoutOtp.error = getString(R.string.please_enter_4_digit_otp)
                } else {
                    layoutOtp.error = ""
                    callVerifyLoginOtpApi()
                }
            }
            tvResendOtp.setOnClickListener {
                clearLabel()
                tvTimer.visibility = View.VISIBLE
                tvResendOtp.visibility = View.INVISIBLE
                startResendOtpCountDownTimer()
                isResend = true
                callLoginApi()
            }
        }
    }

    private fun getVerifyOtpRequestModel(): LoginOtpVerifyRequestModel {
        val request = LoginOtpVerifyRequestModel()
        request.Mobile = binding.etEmailMob.text.toString().trim()
        request.OTP = bottomSheetVerifyOtpBinding.etOtp.text.toString()
        return request
    }

    private fun callVerifyLoginOtpApi() {
        if (isNetworkAvailable()) {
            toggleLoaderOtp(true)
            verifyLoginOtpViewModel.callVerifyLoginOtpApi(getVerifyOtpRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }

    }

    private fun callUpdateFirebaseTokenApi() {
        if (isNetworkAvailable()) {
            loginViewModel.callFirebaseTokenUpdateApi(
                request = FirebaseTokenUpdateRequestModel(
                    FirebaseToken = preferenceUtils.getValue(Constant.PreferenceKeys.FIREBASE_TOKEN),
                    InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
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
                bottomSheetVerifyOtpBinding.tvResendOtp.visibility = View.VISIBLE
            }
        }.start()
    }

    val smsIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val message: String = it.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE) ?: ""
            if (!TextUtils.isEmpty(message)) {
                val code = fetchVerificationCode(message)
                if (!code.isNullOrEmpty()) {
                    if (::bottomSheetVerifyOtpBinding.isInitialized) {
                        bottomSheetVerifyOtpBinding.etOtp.setText(code)
                        bottomSheetVerifyOtpBinding.btnVerify.callOnClick()
                    }
                }
            }
        }
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
        }else {
            registerReceiver(smsBroadcastReceiver, intentFilter)
        }

    }

    private fun fetchVerificationCode(message: String?): String? {
        return Regex("(\\d{4})").find(message ?: "")?.value
    }

    override fun onStart() {
        super.onStart()
        isSmsReceiverOn = true
        registerToSmsBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        if (isSmsReceiverOn) {
            unregisterReceiver(smsBroadcastReceiver)
        }
    }

}