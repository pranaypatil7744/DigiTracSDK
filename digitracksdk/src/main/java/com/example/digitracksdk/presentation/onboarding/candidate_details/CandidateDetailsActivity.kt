package com.example.digitracksdk.presentation.onboarding.candidate_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityCandidateDetailsBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewCandidateDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.StateListModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.my_profile.ProfileViewModel
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

enum class CandidateItems(val value: Int) {
    AADHAR_CARD(1),
    CONTACT_NAME(2),
    CONTACT_NUM(3),
    EMAIL(4),
    BLOOD_GROUP(5),
    GENDER(6),
    MARITAL_STATUS(7),
    NO_OF_CHILDREN(8),
    HOUSE(10),
    STREET(11),
    LANDMARK(12),
    LOCATION(13),
    PIN(14),
    STATE(15),
    ALTERNATE_NO_FLAG(16)
}

class CandidateDetailsActivity : BaseActivity(), ValidationListener, DetailAdapter.DetailListener {

    lateinit var binding: ActivityCandidateDetailsBinding
    lateinit var preferenceUtils: PreferenceUtils
    private lateinit var genderAdapter: ArrayAdapter<String>
    private lateinit var maritalStatusAdapter: ArrayAdapter<String>
    private lateinit var bloodGroupAdapter: ArrayAdapter<String>
    private lateinit var yesNoAdapter: ArrayAdapter<String>

    private val paperlessViewCandidateDetailsViewModel: PaperlessViewCandidateDetailsViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()
    private var genderList: ArrayList<String> = ArrayList()
    private val yesNoList: ArrayList<String> = ArrayList()
    private var bloodGroupList: ArrayList<String> = ArrayList()
    private var maritalStatusList: ArrayList<String> = ArrayList()

    var viewType: Constant.ViewType = Constant.ViewType.EDIT

    private lateinit var detailAdapter: DetailAdapter
    var list: ArrayList<DetailModel> = ArrayList()
    private var stateListId: ArrayList<StateListModel> = ArrayList()
    private var stateNameList: ArrayList<String> = ArrayList()
    private lateinit var stateNameAdapter: ArrayAdapter<String>
    var dataItem: PaperlessViewCandidateDetailsResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCandidateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        paperlessViewCandidateDetailsViewModel.validationListener = this
        setUpToolbar()
        setUpAdapter()
        setUpRecycler()
        setObserver()
        callViewCandidateDetailsApi()
        callStateListApi()
        setUpListener()
    }

    private fun callStateListApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            val innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            profileViewModel.callStateListApi(
                request = CommonRequestModel(
                    InnovId = innovId ?: "1383076"
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setObserver() {
        binding.apply {
            with(profileViewModel)
            {
                stateListResponseData.observe(this@CandidateDetailsActivity) {
                    toggleLoader(false)
                    if (it.Status?.lowercase() == Constant.success) {
                        stateListId.clear()
                        stateNameList.clear()
                        if (it.StateList?.isNotEmpty() == true) {
                            for (i in it.StateList) {
                                stateNameList.add(i.StateName.toString())
                            }
                            stateListId = it.StateList
                        }

                    }
                }
            }
            with(paperlessViewCandidateDetailsViewModel) {
                viewCandidateDetailsResponseData.observe(
                    this@CandidateDetailsActivity
                ) {
                    if (it.status.equals("Success", true)) {
                        if (it.Message.equals("Success", true)) {
                            setUpRecycler(it)
                        } else {
                            showToast(it.Message.toString())
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                updateCandidateBasicDetailsResponseData.observe(this@CandidateDetailsActivity) {
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@CandidateDetailsActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@CandidateDetailsActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewCandidateDetailsApi()
            }
        }
    }

    private fun callViewCandidateDetailsApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                recyclerCandidateDetails.visibility = VISIBLE
                toolbar.tvSave.visibility = VISIBLE
                paperlessViewCandidateDetailsViewModel.callViewCandidateDetailsApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerCandidateDetails.visibility = GONE
                toolbar.tvSave.visibility = GONE
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpAdapter() {
        if (::detailAdapter.isInitialized) {
            detailAdapter.notifyDataSetChanged()
        } else {
            genderList.clear()
            genderList.add(getString(R.string.male))
            genderList.add(getString(R.string.female))
            genderAdapter = ArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text_information,
                genderList
            )
            maritalStatusList.clear()
            maritalStatusList.add(getString(R.string.single))
            maritalStatusList.add(getString(R.string.married))
            maritalStatusList.add(getString(R.string.divorced))
            maritalStatusList.add(getString(R.string.widow))
            maritalStatusAdapter = ArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text_information,
                maritalStatusList
            )

            bloodGroupList.clear()
            bloodGroupList.add(getString(R.string._A_positive))
            bloodGroupList.add(getString(R.string._A_negative))
            bloodGroupList.add(getString(R.string._AB_positive))
            bloodGroupList.add(getString(R.string._AB_negative))
            bloodGroupList.add(getString(R.string._B_positive))
            bloodGroupList.add(getString(R.string._B_negative))
            bloodGroupList.add(getString(R.string._O_positive))
            bloodGroupList.add(getString(R.string._O_negative))
            bloodGroupAdapter = ArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text_information,
                bloodGroupList
            )

            yesNoList.clear()
            yesNoList.add(getString(R.string.yes))
            yesNoList.add(getString(R.string.no))
            yesNoAdapter = ArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text_information,
                yesNoList
            )
            stateNameAdapter = ArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                R.id.text_information,
                stateNameList
            )

            detailAdapter = DetailAdapter(
                this,
                list = list,
                genderAdapter = genderAdapter,
                bloodGroupAdapter = bloodGroupAdapter,
                maritalStatusAdapter = maritalStatusAdapter,
                stateNameAdapter = stateNameAdapter,
                yesNoAdapter = yesNoAdapter,
                listener = this
            )
            binding.recyclerCandidateDetails.adapter = detailAdapter
        }
    }

    private fun setUpRecycler(data: PaperlessViewCandidateDetailsResponseModel? = null) {
        list.clear()
        list.add(
            DetailModel(
                title = getString(R.string.personal_information),
                itemType = DetailItemType.LABEL
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.aadhar_card_number),
                value = data?.AadharNo ?: "",
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 12
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.emergency_contact_name),
                value = data?.EmergencyContactName ?: "",
                itemType = DetailItemType.EDIT_TEXT
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.emergency_contact_number),
                value = data?.EmergencyContactNo ?: "",
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 10
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.email_id),
                value = data?.EmailId ?: "",
                itemType = DetailItemType.EDIT_TEXT_EMAIL
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.blood_group),
                value = data?.BloodGroup ?: "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.BLOOD_GROUP
            )
        )
        val gender = data?.Gender
        var selectedGender = ""
        when (gender) {
            Constant.Male -> {
                selectedGender = getString(R.string.male)
            }

            Constant.Female -> {
                selectedGender = getString(R.string.female)
            }
        }
        list.add(
            DetailModel(
                title = getString(R.string.gender),
                value = selectedGender,
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.GENDER
            )
        )
        val mStatus = data?.MaritalStatus
        var selectedMStatus = ""
        when (mStatus) {
            Constant.Single -> {
                selectedMStatus = getString(R.string.single)
            }

            Constant.Married -> {
                selectedMStatus = getString(R.string.married)
            }

            Constant.Divorced -> {
                selectedMStatus = getString(R.string.divorced)
            }

            Constant.Widow -> {
                selectedMStatus = getString(R.string.widow)
            }
        }

        list.add(
            DetailModel(
                title = getString(R.string.marital_status),
                value = selectedMStatus,
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.MARITAL_STATUS
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.number_of_children),
                value = data?.NoOfChildren ?: "",
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 2
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.address_information),
                itemType = DetailItemType.LABEL
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.house_building_name),
                value = data?.Address1 ?: "",
                itemType = DetailItemType.EDIT_TEXT
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.street),
                value = data?.Address2 ?: "",
                itemType = DetailItemType.EDIT_TEXT
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.landmark),
                value = data?.Address3 ?: "",
                itemType = DetailItemType.EDIT_TEXT
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.location),
                value = data?.Location ?: "",
                itemType = DetailItemType.EDIT_TEXT
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.pin_code),
                value = data?.Pincode ?: "",
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 6
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.state),
                value = data?.StateName ?: "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.STATE,
            )
        )

        val alternateNoFlag = data?.AlternateNoFlag.toString().lowercase()

        val selectedAlternateNo = if (alternateNoFlag == Constant.Yes.lowercase()) {
            getString(R.string.yes)
        } else {
            getString(R.string.no)
        }

        list.add(
            DetailModel(
                title = getString(R.string.alternate_mobile_no_title),
                value = selectedAlternateNo,
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.YESNO
            )
        )
        val alternateNo = data?.AlternateNo
        if (!alternateNo.isNullOrEmpty()) {
            val number = alternateNo.split(",").toTypedArray()
            if (number.isNotEmpty()) {
                list.add(
                    DetailModel(
                        title = getString(R.string.add_contact),
                        itemType = DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD,
                    )
                )
            }
            for (i in number) {
                list.add(
                    DetailModel(
                        title = getString(R.string.alternate_mobile_no),
                        value = i,
                        itemType = DetailItemType.EDIT_TEXT_NUMBER,
                        maxLength = 10,
                        isExtraNumberField = true
                    )
                )
            }
        }
        setUpAdapter()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.candidate_details)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvSave.apply {
                text = getString(R.string.edit)
                visibility = VISIBLE
                setOnClickListener {
                    if (viewType == Constant.ViewType.EDIT) {
                        viewType = Constant.ViewType.ADD
                        text = getString(R.string.save)
                        enableFields()
                    } else {
                        clearError()
                        paperlessViewCandidateDetailsViewModel.validateCandidateDetailsModel(
                            getInsertCandidateDetailsModel(),
                            personalMobileNo = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
                        )

                    }

                }
            }
        }
    }

    private fun isAlternateNoValid(valid: () -> Unit) {
        val filterList = list.filter { it.isExtraNumberField == true }
        filterList.forEach {
            if (AppUtils.INSTANCE?.isValidMobileNumber(it.value.toString()) != true) {
                showToast(getString(R.string.please_enter_all_valid_alternate_no))
                return
            }
        }
        valid()
    }

    private fun getInsertCandidateDetailsModel(): PaperlessUpdateCandidateBasicDetailsRequestModel {

        val request = PaperlessUpdateCandidateBasicDetailsRequestModel()
        request.AadharNo = list[CandidateItems.AADHAR_CARD.value].value.toString().trim()
        request.EmergencyContactName =
            list[CandidateItems.CONTACT_NAME.value].value.toString().trim()
        request.EmergencyContactNo = list[CandidateItems.CONTACT_NUM.value].value.toString().trim()
        request.EmailId = list[CandidateItems.EMAIL.value].value.toString().trim()
        request.BloodGroup = list[CandidateItems.BLOOD_GROUP.value].value.toString()
        var maritalStatusId: String? = ""
        val mStatus: String = list[CandidateItems.MARITAL_STATUS.value].value.toString()
        when {
            mStatus.equals(getString(R.string.single), ignoreCase = true) -> {
                maritalStatusId = "1"
            }

            mStatus.equals(getString(R.string.married), ignoreCase = true) -> {
                maritalStatusId = "2"
            }

            mStatus.equals(getString(R.string.divorced), ignoreCase = true) -> {
                maritalStatusId = "3"
            }

            mStatus.equals(getString(R.string.widow), ignoreCase = true) -> {
                maritalStatusId = "4"
            }
        }
        val gender = list[CandidateItems.GENDER.value].value.toString()
        var selectedGender = ""
        when (gender) {
            getString(R.string.male) -> {
                selectedGender = Constant.Male
            }

            getString(R.string.female) -> {
                selectedGender = Constant.Female
            }
        }

        request.Gender = selectedGender
        request.MartialStatusID = maritalStatusId ?: "0"
        request.MartialStatus = mStatus
        request.NoOfChildren = list[CandidateItems.NO_OF_CHILDREN.value].value.toString().trim()
        request.Address1 = list[CandidateItems.HOUSE.value].value.toString().trim()
        request.Address2 = list[CandidateItems.STREET.value].value.toString().trim()
        request.Address3 = list[CandidateItems.LANDMARK.value].value.toString().trim()
        request.Location = list[CandidateItems.LOCATION.value].value.toString().trim()
        request.Pincode = list[CandidateItems.PIN.value].value.toString().trim()
        val stateIdData =
            stateListId.find { it.StateName == list[CandidateItems.STATE.value].value.toString() }
        val stateId = (stateIdData?.StateID ?: 0)
        request.StateId = stateId
        request.StateName = list[CandidateItems.STATE.value].value.toString()
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.AlternateNoFlag = list[CandidateItems.ALTERNATE_NO_FLAG.value].value.toString()
        val filterList = list.filter { it.isExtraNumberField == true }
        val numberString = filterList.map { it.value }.joinToString(",")
        request.AlternateNo = numberString
        return request
    }

    private fun getItemPosition(fieldType: DetailItemType): Int? {
        var index: Int? = null
        for (i in list.indices) {
            if (list[i].itemType == fieldType) {
                index = i
                break
            }
        }
        return index
    }

    private fun enableFields() {

        for (data in list) {
            data.isEnabled = true
        }

        getItemPosition(DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD)?.let {
            list.filter { it.isExtraNumberField == true }.forEach {
                it.isVisibleIcon = true
            }
        }
        if (list[CandidateItems.MARITAL_STATUS.value].value == getString(R.string.single)) {
            list[CandidateItems.NO_OF_CHILDREN.value].isEnabled = false
            list[CandidateItems.NO_OF_CHILDREN.value].value = ""
        } else {
            list[CandidateItems.NO_OF_CHILDREN.value].isEnabled = true
        }
        list[CandidateItems.AADHAR_CARD.value].isEnabled =
            list[CandidateItems.AADHAR_CARD.value].value.toString().trim().isEmpty()

        detailAdapter.refresh(list)
        setUpAdapter()

    }

    override fun onValidationSuccess(type: String, msg: Int) {
        isAlternateNoValid {
            callUpdateCandidateDetailsApi()
        }
    }

    private fun callUpdateCandidateDetailsApi() {
        if (isNetworkAvailable()) {
            paperlessViewCandidateDetailsViewModel.callUpdateCandidateBasicDetailsApi(
                getInsertCandidateDetailsModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        when (type) {
            Constant.ListenerConstants.CANDIDATE_AADHAAR_ERROR -> {
                list[CandidateItems.AADHAR_CARD.value].apply {
                    error = getString(msg)
                    isFocus = true
                    isEnabled = true
                }
            }

            Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NAME_ERROR -> {
                list[CandidateItems.CONTACT_NAME.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NO_ERROR -> {
                list[CandidateItems.CONTACT_NUM.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR -> {
                list[CandidateItems.EMAIL.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.CANDIDATE_BLOOD_GROUP_ERROR -> {
                list[CandidateItems.BLOOD_GROUP.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.MARITAL_STATUS_ERROR -> {
                list[CandidateItems.MARITAL_STATUS.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.CANDIDATE_NO_OF_CHILDREN_ERROR -> {
                list[CandidateItems.NO_OF_CHILDREN.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.ADDRESS_1_ERROR -> {
                list[CandidateItems.HOUSE.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.ADDRESS_2_ERROR -> {
                list[CandidateItems.STREET.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.ADDRESS_3_ERROR -> {
                list[CandidateItems.LANDMARK.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.LOCATION_ERROR -> {
                list[CandidateItems.LOCATION.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.PIN_ERROR -> {
                list[CandidateItems.PIN.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.STATE_ERROR -> {
                list[CandidateItems.STATE.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
        }


    }

    private fun clearError() {
        for (i in list) {
            i.error = ""
            i.isFocus = false
        }
        detailAdapter.notifyDataSetChanged()
    }

    override fun onTextChanged(position: Int, text: String) {
        list[position].value = text
    }


    override fun onSpinnerDataSelected(selectedPosition: Int, data: String, itemPosition: Int) {
        if (itemPosition == CandidateItems.MARITAL_STATUS.value) {
            if (selectedPosition == 0) {
                list[CandidateItems.NO_OF_CHILDREN.value].isEnabled = false
                list[CandidateItems.NO_OF_CHILDREN.value].value = ""
            } else {
                list[CandidateItems.NO_OF_CHILDREN.value].isEnabled = true
            }
        }
        when (list[itemPosition].spinnerType) {
            SpinnerType.YESNO -> {
                val position = getItemPosition(DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD)
                list[itemPosition].value = if (selectedPosition == 0) {
                    if (position == null) {
                        addOdHideExtraField(show = true)
                    }
                    getString(R.string.yes)
                } else {
                    position?.let {
                        addOdHideExtraField(show = false)
                    }
                    getString(R.string.no)
                }
            }

            else -> {}
        }
        setUpAdapter()

    }

    private fun addOdHideExtraField(show: Boolean) {
        if (show) {
            list.add(
                DetailModel(
                    title = getString(R.string.add_contact),
                    itemType = DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD,
                    isEnabled = true
                )
            )
        } else {
            val a = list.partition {
                it.itemType == DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD ||
                        it.isExtraNumberField == true
            }
            list.clear()
            list.addAll(a.second)

        }
    }

    override fun clearText(position: Int) {
        list.removeAt(position)
        detailAdapter.notifyDataSetChanged()
    }

    override fun clickAddNumber(position: Int) {
        list.add(
            DetailModel(
                title = getString(R.string.alternate_mobile_no),
                value = Int,
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                maxLength = 10,
                isEnabled = true,
                isExtraNumberField = true,
                isVisibleIcon = true
            )
        )

        setUpAdapter()
        binding.recyclerCandidateDetails.scrollToPosition(list.size - 1)
    }

    override fun onRemoveExtraFieldClick(position: Int) {
        list.removeAt(position)
        setUpAdapter()
    }
}