package com.example.digitracksdk.presentation.login.verify_otp_fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseFragment
import com.innov.digitrac.databinding.FragmentVerifyOtpBinding
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpResponseModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsResponseModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.presentation.home.HomeActivity
import com.example.digitracksdk.presentation.login.LoginActivity
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.example.digitracksdk.presentation.login.login_fragment.LoginViewModel
import com.example.digitracksdk.presentation.my_profile.ProfileViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.InnovSingleton
import com.innov.digitrac.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class VerifyOtpFragment : BaseFragment() {

    lateinit var binding: FragmentVerifyOtpBinding
    private val verifyLoginOtpViewModel: VerifyLoginOtpViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()
    private val loginViewModel: LoginViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    var phoneOrEmail: String = ""
    var tokenId = ""
    var insertBasicDetailsRequestModel = InsertBasicDetailsRequestModel()
    private var countDownTimer: CountDownTimer? = null

    companion object {
        fun newInstance(): VerifyOtpFragment {
            return VerifyOtpFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerifyOtpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(this.requireContext())
        getPreferenceData()
        getIntentData()
        setUpListener()
        startResendOtpCountDownTimer()
    }

    private fun getIntentData() {
        (context as LoginActivity).intent.extras?.run {
            insertBasicDetailsRequestModel =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(
                        Constant.BASIC_DETAILS_MODEL,
                        InsertBasicDetailsRequestModel::class.java) as InsertBasicDetailsRequestModel
            }else
                getSerializable(Constant.BASIC_DETAILS_MODEL) as InsertBasicDetailsRequestModel

            phoneOrEmail = insertBasicDetailsRequestModel.Mobile
        }
        setUpView()
    }

    private fun getPreferenceData() {
        tokenId = preferenceUtils.getValue(Constant.PreferenceKeys.TOKEN_ID)
    }

    private fun getVerifyOtpRequestModel(): LoginOtpVerifyRequestModel {
        val request = LoginOtpVerifyRequestModel()
        request.Mobile = phoneOrEmail
        request.OTP = binding.etOtp.text.toString()
        return request
    }

    private fun setUpView() {
        binding.apply {
            tvMob.text = phoneOrEmail
        }
    }

    private fun setUpListener() {

        binding.toolbar.btnBack.setOnClickListener {
            (context as LoginActivity).finish()
        }
        binding.btnVerify.setOnClickListener {
            clearLabel()
            val otp = binding.etOtp.text?.trim()
            if (otp.isNullOrEmpty()) {
                binding.layoutOtp.error = getString(R.string.please_enter_otp)
            } else if (otp.length < 4) {
                binding.layoutOtp.error = getString(R.string.please_enter_4_digit_otp)
            } else {
                binding.layoutOtp.error = ""
                if ((context as LoginActivity).isFromSignUp) {
                    callVerifySignUpOtp()
                } else {
                    callVerifyLoginOtpApi()
                }
            }
        }
        binding.tvResendOtp.setOnClickListener {
            clearLabel()
            binding.tvTimer.visibility = View.VISIBLE
            binding.tvResendOtp.visibility = View.INVISIBLE
            startResendOtpCountDownTimer()
            if ((context as LoginActivity).isFromSignUp) {
                callSignUpResendOtp()
            } else {
                callLoginResendOtpApi()
            }
        }
    }

    private fun getLoginResendOtpRequestModel(): LoginRequestModel {
        val request = LoginRequestModel()
        request.APKVersion = InnovSingleton.singleton.getVersionInfo(this.requireActivity())
        request.AndroidVersion = InnovSingleton.singleton.androidVersion
        request.BuildNo = InnovSingleton.singleton.osBuildNumber
        request.EmployeeCode = ""
        request.Mobile = phoneOrEmail
        request.ModelNo = InnovSingleton.singleton.deviceName
        request.SignupSource = "D"
        return request
    }

    private fun callLoginResendOtpApi() {
        if (this.context?.isNetworkAvailable() == true) {
            loginViewModel.callLoginApi(getLoginResendOtpRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }

        with(loginViewModel) {
            loginResponseData.observe(viewLifecycleOwner) {
                if (it.CandidateStatus.equals(Constant.Valid)) {
                    clearLabel()
                    showToast(getString(R.string.resend_otp_on) + " " + phoneOrEmail)
                } else {
                    showToast(getString(R.string.something_went_wrong))
                }
            }

            messageData.observe(viewLifecycleOwner) {
                showToast(it)
            }
            showProgressbar.observe(requireActivity()) {
                toggleLoader(it)
            }
        }
    }

    private fun callVerifySignUpOtp() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            profileViewModel.callCheckOtpApi(
                request = CheckOtpRequestModel(
                    tokenId,
                    binding.etOtp.text.toString().trim()
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
        with(profileViewModel) {
            checkOtpResponseData.observe(this@VerifyOtpFragment.viewLifecycleOwner,
                object : Observer<CheckOtpResponseModel> {
                    override fun onChanged(it: CheckOtpResponseModel) {
                        checkOtpResponseData.removeObserver(this)
                        toggleLoader(false)
                        when (it.Message) {
                            Constant.Valid -> {
                                val i = Intent(
                                    this@VerifyOtpFragment.requireContext(),
                                    com.example.digitracksdk.presentation.my_profile.create_edit_profile.CreateEditProfileActivity::class.java
                                )
                                i.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                this@VerifyOtpFragment.requireContext().startActivity(i)
                                this@VerifyOtpFragment.requireActivity().finish()
                            }
                            Constant.Invalid -> {
                                binding.etOtp.error = getString(R.string.please_enter_valid_otp)
                            }
                            else -> {
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }
                    }
                })

            messageData.observe(this@VerifyOtpFragment.viewLifecycleOwner,
                object : Observer<String> {
                    override fun onChanged(t: String) {
                        toggleLoader(false)
                        messageData.removeObserver(this)
                        showToast(t.toString())
                    }
                })
        }
    }


    private fun startResendOtpCountDownTimer() {

        countDownTimer = object : CountDownTimer(Constant.THIRTY_SEC, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val timerToUse = (millisUntilFinished / 1000).toString()
                binding.tvTimer.text = AppUtils.INSTANCE?.convertDateFormat(
                    dateFormatToRead = "ss",
                    dateToRead = timerToUse,
                    dateFormatToConvert = "mm:ss",
                    timeZone = TimeZone.getDefault(),
                    localTimeZone = TimeZone.getDefault()
                )
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                binding.tvTimer.visibility = View.GONE
                binding.tvResendOtp.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun toggleLoader(showLoader: Boolean) {
        (context as LoginActivity).toggleLoader(showLoader)
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()

    }

    private fun callVerifyLoginOtpApi() {
        if (context?.isNetworkAvailable() == true) {
            verifyLoginOtpViewModel.callVerifyLoginOtpApi(getVerifyOtpRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
        with(verifyLoginOtpViewModel) {
            verifyLoginOtpViewModel.responseData.observe(viewLifecycleOwner) {
                when {
                    it.OTPStatus.equals(Constant.Valid) -> {
                        clearLabel()
                        preferenceUtils.setValue(Constant.PreferenceKeys.INNOV_ID, it.InnovID)
                        preferenceUtils.setValue(Constant.PreferenceKeys.TOKEN_ID, it.TokenID)
                        preferenceUtils.setValue(Constant.PreferenceKeys.IS_LOGIN, true)
                        startHomeScreen()
                    }
                    it.OTPStatus.equals(Constant.Invalid) -> {
                        clearLabel()
                        binding.layoutOtp.error = getString(R.string.please_enter_valid_otp)
                    }
                    else -> {
                        clearLabel()
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

            }
            verifyLoginOtpViewModel.messageData.observe(viewLifecycleOwner) {
                showToast(it)
            }
            showProgressbar.observe(requireActivity()) {
                toggleLoader(it)
            }
        }

    }

    private fun startHomeScreen() {
        startActivity(Intent(this@VerifyOtpFragment.requireContext(), HomeActivity::class.java))
        this@VerifyOtpFragment.requireActivity().finish()
    }

    private fun clearLabel() {
        binding.layoutOtp.error = ""
    }

    private fun callSignUpResendOtp() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            profileViewModel.callInsertBasicDetailsApi(insertBasicDetailsRequestModel)
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
        with(profileViewModel) {
            insertBasicDetailsResponseData.observe(viewLifecycleOwner,
                object : Observer<InsertBasicDetailsResponseModel> {
                    override fun onChanged(it: InsertBasicDetailsResponseModel) {
                        insertBasicDetailsResponseData.removeObserver(this)
                        toggleLoader(false)
                        if (it.status == Constant.SUCCESS) {
                            if (!it.OTP.isNullOrEmpty()) {
                                preferenceUtils.setValue(
                                    Constant.PreferenceKeys.TOKEN_ID,
                                    it.TokenID
                                )
                                showToast(getString(R.string.resend_otp_on)+" "+insertBasicDetailsRequestModel.Mobile)
                            } else {
                                showToast(it.Message.toString())
                            }

                        } else {
                            showToast(it.Message.toString())
                        }
                    }
                })

            messageData.observe(viewLifecycleOwner, object : Observer<String> {
                override fun onChanged(t: String) {
                    messageData.removeObserver(this)
                    toggleLoader(false)
                    showToast(t.toString())
                }
            })
        }
    }

}