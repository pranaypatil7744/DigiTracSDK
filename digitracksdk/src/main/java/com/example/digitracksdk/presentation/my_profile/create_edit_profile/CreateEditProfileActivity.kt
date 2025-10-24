package com.example.digitracksdk.presentation.my_profile.create_edit_profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.digitracksdk.domain.model.profile_model.CityListModel
import com.example.digitracksdk.domain.model.profile_model.CityListRequestModel
import com.example.digitracksdk.domain.model.profile_model.StateListModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityCreateEditProfileBinding
import com.example.digitracksdk.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationRequestModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.profile_model.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.login.LoginActivity
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.example.digitracksdk.presentation.my_profile.ProfileViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


enum class MaritalStatus(val value: Int) {

    Single(1),
    Married(2),
    Divorced(3),
    Widow(4),
}

class CreateEditProfileActivity : BaseActivity(), ValidationListener, DialogUtils.DialogManager {
    lateinit var binding: ActivityCreateEditProfileBinding
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val profileViewModel: ProfileViewModel by viewModel()
    private var profileDetailsList: ArrayList<InnovIDCardResponseModel> = ArrayList()
    private var stateList: ArrayList<StateListModel> = ArrayList()
    private var cityList: ArrayList<CityListModel> = ArrayList()
    private var cityListPresent: ArrayList<CityListModel> = ArrayList()
    var isFromEdit: Boolean = false
    var isPresent:Boolean =false
    var profilePic = ""
    var innovId: String = ""
    var personalMobileNo: String = ""
    var selectedStateId: Int = 0
    var selectedStateIdPresent: Int = 0
    var selectedCityId: Int = 0
    var selectedCityIdPresent: Int = 0
    var maritalStatus: Int = 0
    private var imagePath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        profileViewModel.validationListener = this
        getPreferenceData()
        setObserver()
        callStateListApi()
        getIntentData()
        setUpDropDownData()
        setUpToolbar()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(profileViewModel) {
                stateListResponseData.observe(this@CreateEditProfileActivity) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        stateList.clear()
                        stateList.addAll(it.StateList ?: arrayListOf())
                        val list: ArrayList<String> = ArrayList()
                        list.clear()
                        for (i in stateList) {
                            list.add(i.StateName.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@CreateEditProfileActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            list
                        )
                        binding.apply {
                            etState.setAdapter(adapter)
                            etStatePresent.setAdapter(adapter)
                        }
                        if (isFromEdit) {
                            val stateIdList = stateList.find {
                                it.StateName == profileDetailsList[0].stateName
                            }
                            val stateIdPresentList = stateList.find {
                                it.StateName == profileDetailsList[0].PermanentStateName
                            }

                            if (stateIdList != null) {
                                selectedStateId = stateIdList.StateID ?: 0
                                callCityListApi(false)
                            }

                            Handler(mainLooper).postDelayed({
                                if (stateIdPresentList != null) {
                                    selectedStateIdPresent = stateIdPresentList.StateID ?: 0
                                    callCityListApi(true)
                                }
                            }, 1000)

                        }
                    }
                }

                cityListResponseData.observe(this@CreateEditProfileActivity) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        if (isPresent) {
                            cityListPresent.clear()
                            cityListPresent.addAll(it.Citylist ?: arrayListOf())
                            val list: ArrayList<String> = ArrayList()
                            list.clear()
                            for (i in cityListPresent) {
                                list.add(i.CityName.toString())
                            }
                            val adapter = ArrayAdapter(
                                this@CreateEditProfileActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                list
                            )
                            binding.apply {
                                etCityPresent.setAdapter(adapter)
                            }
                        } else {
                            cityList.clear()
                            cityList.addAll(it.Citylist ?: arrayListOf())
                            val list1: ArrayList<String> = ArrayList()
                            list1.clear()
                            for (i in cityList) {
                                list1.add(i.CityName.toString())
                            }
                            val adapter = ArrayAdapter(
                                this@CreateEditProfileActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                list1
                            )
                            binding.apply {
                                etCity.setAdapter(adapter)
                            }
                        }
                        if (isFromEdit) {
                            val cityIdList = cityList.find {
                                it.CityName == profileDetailsList[0].cityName
                            }
                            val cityIdPresentList = cityListPresent.find {
                                it.CityName == profileDetailsList[0].PermanentCityName
                            }
                            if (cityIdList != null) {
                                selectedCityId = cityIdList.CityID ?: 0
                            }
                            if (cityIdPresentList != null) {
                                selectedCityIdPresent = cityIdPresentList.CityID ?: 0
                            }
                        }
                    }
                }

                updateProfileResponseData.observe(this@CreateEditProfileActivity) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        preferenceUtils.setValue(
                            Constant.PreferenceKeys.MobileNo,
                            getUpdateProfileRequestModel().Mobile
                        )
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                newInnovIdCreationResponseData.observe(this@CreateEditProfileActivity) {
                    toggleLoader(false)
                    val name = binding.etFirstName.text.toString()
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        preferenceUtils.setValue(Constant.PreferenceKeys.INNOV_ID, it.InnovID)
                        DialogUtils.showSignUpCompletedDialog(
                            this@CreateEditProfileActivity,
                            this@CreateEditProfileActivity,
                            name,
                            it.InnovID.toString()
                        )
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@CreateEditProfileActivity) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {

            etState.setOnClickListener {
                callStateListApi()
            }
            etStatePresent.setOnClickListener {
                callStateListApi()
            }
            etState.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    selectedStateId = stateList[position].StateID ?: 0
                    etCity.setText(getString(R.string.Select))
                    selectedCityId = 0
                    callCityListApi(false)
                }

            etStatePresent.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    selectedStateIdPresent = stateList[position].StateID ?: 0
                    etCityPresent.setText(getString(R.string.Select))
                    selectedCityIdPresent = 0
                    callCityListApi(true)
                }

            etCity.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    selectedCityId = cityList[position].CityID ?: 0
                }

            etCityPresent.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    selectedCityIdPresent = cityListPresent[position].CityID ?: 0
                }

            checkSameAddress.setOnClickListener {
                if (checkSameAddress.isChecked) {
                    etHouseNamePresent.setText(etHouseName.text.toString())
                    etStreetPresent.setText(etStreet.text.toString())
                    etLandmarkPresent.setText(etLandmark.text.toString())
                    etPinCodePresent.setText(etPinCode.text.toString())
                    etStatePresent.setText(etState.text.toString())
                    etCityPresent.setText(etCity.text.toString())
                    selectedStateIdPresent = selectedStateId
                    selectedCityIdPresent = selectedCityId
                } else {
                    etHouseNamePresent.setText("")
                    etStreetPresent.setText("")
                    etLandmarkPresent.setText("")
                    etPinCodePresent.setText("")
                    etStatePresent.setText(getString(R.string.Select))
                    etCityPresent.setText(getString(R.string.Select))
                    selectedStateId = 0
                    selectedStateIdPresent = 0
                }
            }

            imgUserProfile.setOnClickListener {
                if (!isFromEdit){
                  openAddPhotoBottomSheet()
                }

            }

            btnSubmit.setOnClickListener {
                clearError()
                profileViewModel.validateNewInnovIdRequest(getNewInnovIdCreationRequestModel(),this@CreateEditProfileActivity,isFromEdit, personalMobileNo = personalMobileNo)
            }

            radioSingle.setOnClickListener {
                maritalStatus = MaritalStatus.Single.value
            }
            radioMarried.setOnClickListener {
                maritalStatus = MaritalStatus.Married.value
            }
            radioDivorced.setOnClickListener {
                maritalStatus = MaritalStatus.Divorced.value
            }
            radioWidow.setOnClickListener {
                maritalStatus = MaritalStatus.Widow.value
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutFirstName.error =""
            layoutMiddleName.error =""
            layoutLastName.error =""
            layoutMobileNo.error =""
            layoutEmail.error =""
            layoutBloodGroup.error =""
            layoutRoleForApply.error =""
            layoutHouseName.error =""
            layoutStreet.error =""
            layoutLandmark.error =""
            layoutPinCode.error =""
            layoutState.error =""
            layoutCity.error =""
            layoutHouseNamePresent.error =""
            layoutStreetPresent.error =""
            layoutLandmarkPresent.error =""
            layoutPinCodePresent.error =""
            layoutStatePresent.error =""
            layoutCityPresent.error =""
        }
    }

    private fun getNewInnovIdCreationRequestModel(): NewInnovIdCreationRequestModel {
        binding.apply {
            val request = NewInnovIdCreationRequestModel()
            request.FirstName = etFirstName.text.toString().trim()
            request.MiddleName = etMiddleName.text.toString().trim()
            request.LastName = etLastName.text.toString().trim()
            request.Skill = etRoleForApply.text.toString().trim()
            request.EmergencyContactNo = etMobileNo.text.toString().trim()
            request.EmergencyContactName = etEmail.text.toString().trim()
            request.BloodGroup = etBloodGroup.text.toString().trim()
            request.Address1 = etHouseName.text.toString().trim()
            request.Address2 = etStreet.text.toString().trim()
            request.Address3 = etLandmark.text.toString().trim()
            request.StateID = selectedStateId
            request.CityID = selectedCityId
            request.PIN = etPinCode.text.toString().trim()
            request.PermanentAddress1 = etHouseNamePresent.text.toString().trim()
            request.PermanentAddress2 = etStreetPresent.text.toString().trim()
            request.PermanentAddress3 = etLandmarkPresent.text.toString().trim()
            request.PermanentStateID = selectedStateIdPresent
            request.PermanentCityID = selectedCityIdPresent
            request.PermanentAddressPIN = etPinCodePresent.text.toString().trim()
            request.RecruiterID = "Digitrac_0"
            request.TokenID = preferenceUtils.getValue(Constant.PreferenceKeys.TOKEN_ID)
            request.MaritalStatus = maritalStatus
            request.Picture = imagePath
            return request
        }
    }

    private fun callCreateProfileApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            profileViewModel.callNewInnovIdCreationApi(getNewInnovIdCreationRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onOkClick() {
        val intent = Intent(this@CreateEditProfileActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun getUpdateProfileRequestModel(): UpdateProfileRequestModel {
        binding.apply {
            val request = UpdateProfileRequestModel()
            request.InnovID = innovId
            request.Mobile = etMobileNo.text.toString().trim()
            request.Email = etEmail.text.toString().trim()
            request.MaritalStatus = maritalStatus
            request.Address1 = etHouseName.text.toString().trim()
            request.Address2 = etStreet.text.toString().trim()
            request.Address3 = etLandmark.text.toString().trim()
            request.CityID = selectedCityId
            request.StateID = selectedStateId
            request.PIN = etPinCode.text.toString().trim()
            request.PermanentAddress1 = etHouseNamePresent.text.toString().trim()
            request.PermanentAddress2 = etStreetPresent.text.toString().trim()
            request.PermanentAddress3 = etLandmarkPresent.text.toString().trim()
            request.PermanentStateID = selectedStateIdPresent
            request.PermanentCityID = selectedCityIdPresent
            request.PermanentAddressPIN = etPinCodePresent.text.toString().trim()
            request.BloodGroup = etBloodGroup.text.toString().trim()
            request.BloodGroupId = 0
            return request
        }
    }

    private fun callUpdateProfileApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            profileViewModel.callUpdateProfileApi(getUpdateProfileRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpDropDownData() {
        binding.apply {
            val bloodGroupList = arrayListOf(
                getString(R.string.blood_group),
                "A +",
                "A -",
                "B +",
                "B -",
                "AB +",
                "AB -",
                "O +",
                "O -",
                getString(
                    R.string.na
                )
            )
            val bloodGroupAdapter = ArrayAdapter(
                this@CreateEditProfileActivity,
                android.R.layout.simple_dropdown_item_1line,
                bloodGroupList
            )
            etBloodGroup.setAdapter(bloodGroupAdapter)
        }
    }

    private fun callStateListApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            profileViewModel.callStateListApi(
                request = CommonRequestModel(
                    InnovId = innovId ?: "1383076"
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callCityListApi(isPresent: Boolean) {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            this.isPresent = isPresent
            if (isPresent) {
                profileViewModel.callCityListApi(request = CityListRequestModel(StateID = selectedStateIdPresent))
            } else {
                profileViewModel.callCityListApi(request = CityListRequestModel(StateID = selectedStateId))
            }
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getPreferenceData() {
        profilePic = preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        if (innovId.isEmpty()) {
            //TODO remove this..
            innovId = ""
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromEdit = getBoolean(Constant.IS_FOR_EDIT,false)
            if (isFromEdit) {
                val type: Class<out ArrayList<InnovIDCardResponseModel>?> = ArrayList<InnovIDCardResponseModel>().javaClass

                profileDetailsList=
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        getSerializable(Constant.PROFILE_MODEL,type) as ArrayList<InnovIDCardResponseModel>
                    else
                        getSerializable(Constant.PROFILE_MODEL) as ArrayList<InnovIDCardResponseModel>

                setUpProfileData()
            } else {
                personalMobileNo = getString(Constant.PreferenceKeys.MobileNo).toString()
                setUpSignUpView()
            }
        }
    }

    private fun setUpSignUpView() {
        binding.apply {
            etEmail.apply {
                val digist = "qwertzuiopasdfghjklyxcvbnm,_,-, ,QWERTYUIOPLKJHGFDSAZXCVBNM"
                keyListener = DigitsKeyListener.getInstance(digist)
                setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
            }
        }
    }

    private fun setUpProfileData() {
        binding.apply {
            if (profileDetailsList.size != 0) {
                val data = profileDetailsList[0]
                val pic = ImageUtils.INSTANCE?.stringToBitMap(profilePic)
                personalMobileNo = data.mobile.toString()
                layoutMobileNo.hint = getString(R.string.mobile_number)
                layoutEmail.hint = getString(R.string.email_id)
                layoutFirstName.hint = getString(R.string.name)
                layoutMiddleName.visibility = GONE
                layoutLastName.visibility = GONE
                layoutRoleForApply.visibility = GONE
                etFirstName.isEnabled = false
                ImageUtils.INSTANCE?.loadBitMap(imgUserProfile, pic)
                val fullName = "${data.associateFirstName} ${data.associateMiddleName} ${data.associateLastName}"
                etFirstName.setText(fullName)
                etMiddleName.setText("")
                etLastName.setText("")
                etMobileNo.setText(data.mobile)
                etEmail.setText(data.email)
                etBloodGroup.setText(data.bloodGroup)
                etRoleForApply.setText("")
                when (data.maritalStatus) {
                    Constant.Single -> {
                        radioSingle.isChecked = true
                        maritalStatus = MaritalStatus.Single.value
                    }
                    Constant.Married -> {
                        radioMarried.isChecked = true
                        maritalStatus = MaritalStatus.Married.value
                    }
                    Constant.Divorced -> {
                        radioDivorced.isChecked = true
                        maritalStatus = MaritalStatus.Divorced.value
                    }
                    Constant.Widow -> {
                        radioWidow.isChecked = true
                        maritalStatus = MaritalStatus.Widow.value
                    }
                }
                etBloodGroup.setText(data.bloodGroup)
                etState.setText(data.stateName)
                etCity.setText(data.cityName)
                etStatePresent.setText(data.PermanentStateName)
                etCityPresent.setText(data.PermanentCityName)
                etHouseName.setText(data.address1)
                etHouseNamePresent.setText(data.permanentAddress1)
                etStreet.setText(data.address2)
                etStreetPresent.setText(data.permanentAddress2)
                etLandmark.setText(data.address3)
                etLandmarkPresent.setText(data.permanentAddress3)
                etPinCode.setText(data.pincode)
                etPinCodePresent.setText(data.PermanentAddressPIN)
                checkSameAddress.isChecked = false
            }

        }
    }

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitmapFile =
                    File(url.toString())
                val bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
                imagePath = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                binding.apply {
                    ImageUtils.INSTANCE?.loadBitMap(imgUserProfile, bitmap)
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

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.personal_info)
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

    override fun onValidationSuccess(type: String, msg: Int) {
        if (isFromEdit) {
            callUpdateProfileApi()
        } else {
            callCreateProfileApi()
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.NAME_ERROR -> {
                    layoutFirstName.error = getString(msg)
                }
                Constant.ListenerConstants.LAST_NAME_ERROR -> {
                    layoutLastName.error = getString(msg)
                }
                Constant.ListenerConstants.EMERGENCY_CON_NUMBER_ERROR -> {
                    layoutMobileNo.error = getString(msg)
                }
                Constant.ListenerConstants.EMERGENCY_CON_NAME_ERROR -> {
                    layoutEmail.error = getString(msg)
                }
                Constant.ListenerConstants.BLOOD_GROUP_ERROR -> {
                    layoutBloodGroup.error = getString(msg)
                }
                Constant.ListenerConstants.SKILL_ERROR -> {
                    layoutRoleForApply.error = getString(msg)
                }
                Constant.ListenerConstants.MARITAL_STATUS_ERROR,
                Constant.ListenerConstants.IMAGE_ERROR -> {
                    showToast(getString(msg))
                }
                Constant.ListenerConstants.ADDRESS_1_ERROR -> {
                    layoutHouseName.error = getString(msg)
                }
                Constant.ListenerConstants.ADDRESS_2_ERROR -> {
                    layoutStreet.error = getString(msg)
                }
                Constant.ListenerConstants.ADDRESS_3_ERROR -> {
                    layoutLandmark.error = getString(msg)
                }
                Constant.ListenerConstants.PIN_ERROR -> {
                    layoutPinCode.error = getString(msg)
                }
                Constant.ListenerConstants.STATE_ERROR -> {
                    layoutState.error = getString(msg)
                }
                Constant.ListenerConstants.CITY_ERROR -> {
                    layoutCity.error = getString(msg)
                }
                Constant.ListenerConstants.PER_ADDRESS_1_ERROR -> {
                    layoutHouseNamePresent.error = getString(msg)
                }
                Constant.ListenerConstants.PER_ADDRESS_2_ERROR -> {
                    layoutStreetPresent.error = getString(msg)
                }
                Constant.ListenerConstants.PER_ADDRESS_3_ERROR -> {
                    layoutLandmarkPresent.error = getString(msg)
                }
                Constant.ListenerConstants.PER_PIN_ERROR -> {
                    layoutPinCodePresent.error = getString(msg)
                }
                Constant.ListenerConstants.PER_STATE_ERROR -> {
                    layoutStatePresent.error = getString(msg)
                }
                Constant.ListenerConstants.PER_CITY_ERROR -> {
                    layoutCityPresent.error = getString(msg)
                }

            }
        }
    }
}