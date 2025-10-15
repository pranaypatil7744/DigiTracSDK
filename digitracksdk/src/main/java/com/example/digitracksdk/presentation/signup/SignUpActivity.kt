package com.example.digitracksdk.presentation.signup

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateRequestModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivitySignUpBinding
import com.innov.digitrac.databinding.BottomSheetAddPhotoBinding
import com.innov.digitrac.databinding.BottomSheetVerifyOtpBinding
import com.innov.digitrac.domain.model.profile_model.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.presentation.my_profile.ProfileViewModel
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.example.digitracksdk.receiver.SMSBroadcastReceiver
import com.example.digitracksdk.utils.AppUtils
import com.innov.digitrac.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class SignUpActivity : BaseActivity(), DetailAdapter.DetailListener, ValidationListener {

    lateinit var binding: ActivitySignUpBinding
    lateinit var context: Context
    lateinit var smsBroadcastReceiver: SMSBroadcastReceiver
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    private lateinit var bottomSheetVerifyOtpBinding: BottomSheetVerifyOtpBinding
    lateinit var verifyOtpDialog: BottomSheetDialog
    lateinit var preferenceUtils: PreferenceUtils
    private val profileViewModel: ProfileViewModel by viewModel()
    var list: ArrayList<DetailModel> = ArrayList()
    lateinit var adapter: DetailAdapter
    private var adharImage: String = ""
    private var imageName: String = ""
    private var isValidCandidate: Boolean = false
    private var countDownTimer: CountDownTimer? = null
    var isResend:Boolean = false
    var tokenId:String = ""

    var genderList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        profileViewModel.validationListener = this
        context = this@SignUpActivity
        AppUtils.INSTANCE?.setLang(this)
        setUpToolbar()
        setListener()
        setUpRecyclerList()
        setUpAdapter()
        setUpObserver()
        startSmsUserConsent()
    }

    private fun setUpObserver() {
        binding.apply {
            with(profileViewModel) {
                insertBasicDetailsResponseData.observe(this@SignUpActivity
                ) { it ->
                    if (it.status == Constant.SUCCESS) {
                        if (it.Message == "Aadhar number already exists") {
                            unregisterReceiver(smsBroadcastReceiver)
                            showToast(it.Message.toString())
                        } else if (it.Message == "Mobile number already exists") {
                            preferenceUtils.setValue(
                                Constant.PreferenceKeys.TOKEN_ID,
                                it.TokenID
                            )
                            if (isResend) {
                                isResend = false
                                showToast(getString(R.string.resend_otp_on) + " " + getBasicDetailsRequestModel().Mobile)
                            } else {
                                openVerifyOtpBottomSheet()
                            }
                        } else if (it.Message == "Mobile number already exists. InnovId Created") {
                            showToast(it.Message.toString())
                            startActivity(Intent(this@SignUpActivity, com.example.digitracksdk.presentation.login.LoginActivity::class.java))
                            finish()
                        } else {
                            preferenceUtils.setValue(
                                Constant.PreferenceKeys.TOKEN_ID,
                                it.TokenID
                            )
                            if (isResend) {
                                isResend = false
                                showToast(getString(R.string.resend_otp_on) + " " + getBasicDetailsRequestModel().Mobile)
                            } else {
                                openVerifyOtpBottomSheet()
                            }
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                checkOtpResponseData.observe(this@SignUpActivity
                ) { it ->
                    toggleLoader(false)
                    when (it.Message) {
                        Constant.Valid -> {
                            val i = Intent(
                                this@SignUpActivity,
                                com.example.digitracksdk.presentation.my_profile.create_edit_profile.CreateEditProfileActivity::class.java
                            )
                            verifyOtpDialog.dismiss()
                            val b = Bundle()
                            b.putString(Constant.PreferenceKeys.MobileNo,getBasicDetailsRequestModel().Mobile)
                            b.putBoolean(Constant.IS_FOR_EDIT,false)
                            i.putExtras(b)
//                            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(i)
//                            finish()
                        }
                        Constant.Invalid -> {
                            bottomSheetVerifyOtpBinding.etOtp.error =
                                getString(R.string.please_enter_valid_otp)
                        }
                        else -> {
                            showToast(getString(R.string.something_went_wrong))
                        }
                    }
                }


                validCandidateResponseData.observe(this@SignUpActivity
                ) { it ->
                    if (it.Status != Constant.Valid) {
                        isValidCandidate = false
                        showToast(getString(R.string.candidate_have_below_18_age))
                    } else {
                        isValidCandidate = true
                    }
                }

                messageData.observe(this@SignUpActivity
                ) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }

                showProgress.observe(this@SignUpActivity
                ) { it -> toggleLoader(it) }
            }
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(this).also {
            //We can add user phone number or leave it blank
            it.startSmsUserConsent(null)
                .addOnSuccessListener {
                    AppUtils.INSTANCE?.logMe("SMS_RETRIEVER","Sms Retreiver listening for otp success.")

                }
                .addOnFailureListener {
                    AppUtils.INSTANCE?.logMe("SMS_RETRIEVER","Sms Retreiver listening for otp failed.")

                }
        }

    }

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                imageName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME).toString()
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitmapFile =
                    File(url.toString())
                val bitmap :Bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
                adharImage = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                binding.apply {
                    containerAadharPreview.visibility = View.VISIBLE
                    ImageUtils.INSTANCE?.loadLocalImage(icAadharPreview, bitmapFile)
                    fileName.text = imageName
                    containerAadharUpload.visibility = View.GONE
                }
            }
        }

    private fun openAddPhotoBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_photo, null)
        addPhotoBottomSheetDialogBinding = BottomSheetAddPhotoBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        val intent = Intent(this, AddImageUtils::class.java)
        val b = Bundle()
        addPhotoBottomSheetDialogBinding.apply {
            imgCamera.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, true)
                intent.putExtras(b)
                addImageUtils.launch(intent)
                bottomSheetDialog.dismiss()
            }
            imgGallery.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, false)
                intent.putExtras(b)
                addImageUtils.launch(intent)
                bottomSheetDialog.dismiss()
            }
        }

    }

    private fun setListener() {
        binding.apply {
            icClose.setOnClickListener {
                adharImage = ""
                containerAadharPreview.visibility = View.GONE
                containerAadharUpload.visibility = View.VISIBLE
            }
            btnSubmit.setOnClickListener {
                registerToSmsBroadcastReceiver()
                profileViewModel.validateBasicDetails(getBasicDetailsRequestModel(),list[2].value as? Boolean?:false)
            }
            containerAadharUpload.setOnClickListener {
                openAddPhotoBottomSheet()
            }
        }
    }

    private fun callInsertBasicDetailsApi() {
        if (isNetworkAvailable()) {
            profileViewModel.callInsertBasicDetailsApi(getBasicDetailsRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
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
            tvMob.text = if (getBasicDetailsRequestModel().Mobile.isEmpty() || getBasicDetailsRequestModel().Mobile == "null") "" else getBasicDetailsRequestModel().Mobile

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
                    callVerifySignUpOtp()
                }
            }
            tvResendOtp.setOnClickListener {
                clearLabel()
                tvTimer.visibility = View.VISIBLE
                tvResendOtp.visibility = View.INVISIBLE

                startResendOtpCountDownTimer()
                isResend = true
                callInsertBasicDetailsApi()
            }
        }
    }

    private fun callVerifySignUpOtp() {
        bottomSheetVerifyOtpBinding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                profileViewModel.callCheckOtpApi(
                    request = CheckOtpRequestModel(
                        preferenceUtils.getValue(Constant.PreferenceKeys.TOKEN_ID),
                        etOtp.text.toString().trim()
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun clearLabel() {
        bottomSheetVerifyOtpBinding.layoutOtp.error = ""
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

    private fun getBasicDetailsRequestModel(): InsertBasicDetailsRequestModel {
        val request = InsertBasicDetailsRequestModel()
        request.NameAsPerAadhar = list[0].value.toString()
        request.AadharNo = list[1].value.toString()
        request.Gender = list[3].value.toString()
        request.DOB = list[4].value.toString()
        request.Mobile = list[5].value.toString()
        request.FatherName = list[6].value.toString()
        request.DocType = getString(R.string.aadhaar)
        request.AadharDocImageArr = adharImage
        request.Extn = imageName.split(".").last()
        return request
    }

    private fun setUpAdapter() {
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        } else {
            adapter = DetailAdapter(
                context = this,
                list = list,
                genderRadioList = genderList,
                listener = this
            )
            binding.recyclerSignUp.adapter = adapter
        }
    }

    private fun setUpRecyclerList() {
        genderList.add(getString(R.string.male))
        genderList.add(getString(R.string.female))
        genderList.add(getString(R.string.other))
        list.add(
            DetailModel(
                title = getString(R.string.name_as_per_aadhar_card),
                isEnabled = true,
                itemType = DetailItemType.EDIT_TEXT
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.aadhar_card_number),
                isEnabled = true,
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 12
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.aadhar_card_holder_description),
                isEnabled = true,
                itemType = DetailItemType.CHECKBOX
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.gender),
                itemType = DetailItemType.RADIO_GROUP,
                isEnabled = true
            )
        )

        list.add(
            DetailModel(
                title = getString(R.string.date_of_birth_sign_up),
                isEnabled = true,
                itemType = DetailItemType.EDIT_TEXT_DATE
            )
        )

        list.add(
            DetailModel(
                title = getString(R.string.mobile_number),
                isEnabled = true,
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 10
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.father_husband_name),
                isEnabled = true,
                itemType = DetailItemType.EDIT_TEXT
            )
        )
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            homeToolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_ribbon))
            btnBack.apply {
                setColorFilter(ContextCompat.getColor(context, R.color.white))
                setOnClickListener {
                    finish()
                }
            }

            tvTitle.apply {
                setTextColor(ContextCompat.getColor(context, R.color.white))
                text = getString(R.string.aadhar_card_details)
            }
        }
    }

    override fun openDatePicker(position: Int) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this@SignUpActivity,
            { view, year, monthOfYear, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, monthOfYear)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateToUpdate =
                    AppUtils.INSTANCE?.convertDateToString(selectedCalendar.time, "dd MMM yyyy")
                list[position].value = dateToUpdate
                adapter.notifyItemChanged(position)
                callVerifyUserApi(dateToUpdate.toString())
            }, y, m, d
        )
        val pastYrDate = AppUtils.INSTANCE?.getPrevYearDate(18)
        if (pastYrDate != null) {
            datePickerDialog.datePicker.maxDate = pastYrDate
        }
        datePickerDialog.show()

    }

    private fun callVerifyUserApi(DOB: String) {
        if (isNetworkAvailable()) {
            profileViewModel.callValidCandidateApi(request = ValidCandidateRequestModel(DateOfBirth = DOB))
        } else {
            showToast(getString(R.string.no_internet_connection))
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
    private fun fetchVerificationCode(message: String?): String? {
        return Regex("(\\d{4})").find(message ?: "")?.value
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
        }else{
            registerReceiver(smsBroadcastReceiver, intentFilter)
        }

    }

    override fun onValidationSuccess(type: String, msg: Int) {
        if (isValidCandidate) {
            callInsertBasicDetailsApi()
        } else {
            showToast(getString(R.string.candidate_have_below_18_age))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        showToast(getString(msg))
    }

    override fun onStart() {
        super.onStart()
        registerToSmsBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }
}