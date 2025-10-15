package com.example.digitracksdk.presentation.onboarding.educational_details

import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityAddEducationalDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.ListEducationDetails
import com.example.digitracksdk.domain.model.onboarding.LstCategory
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamRequestModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.LstResignationReason
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

enum class EducationDetailsItems(val value: Int) {
    CATEGORY_NAME(0),
    EDUCATIONAL_STREAM(1),
    PASSING_YEAR(2),
    BOARD(3),
    INSTITUTE(4),
    PERCENTAGE(5),

}

class AddEducationalDetailsActivity : BaseActivity(), ValidationListener,
    DetailAdapter.DetailListener, DialogUtils.DialogManager {

    var viewType: Constant.ViewType = Constant.ViewType.ADD
    lateinit var binding: ActivityAddEducationalDetailsBinding
    lateinit var adapter: DetailAdapter
    private var educationDetailModel: ListEducationDetails? = null
    var list: ArrayList<DetailModel> = ArrayList()
    lateinit var preferenceUtils: PreferenceUtils
    private var educationalCategoryList: ArrayList<String> = ArrayList()
    private var educationalStreamList: ArrayList<String> = ArrayList()
    private var educationalStreamListId: ArrayList<LstResignationReason> = ArrayList()
    private var educationalCategoryListId: ArrayList<LstCategory> = ArrayList()
    private lateinit var educationalCategoryAdapter: ArrayAdapter<String>
    private lateinit var educationalStreamAdapter: ArrayAdapter<String>

    private val educationDetailsViewModel: PaperlessViewEducationDetailsViewModel by viewModel()
    private var isFromAdd: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEducationalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        educationDetailsViewModel.validationListener = this
        preferenceUtils = PreferenceUtils(this)
        setUpAdapter()
        setObserver()
        getIntentData()
    }

    private fun setObserver() {
        binding.apply {
            with(educationDetailsViewModel) {
                getEducationCategoryResponseData.observe(this@AddEducationalDetailsActivity) {
                    if (it.lstCategory.isNotEmpty()) {
                        educationalCategoryList.clear()
                        for (i in it.lstCategory) {
                            educationalCategoryList.add(i.CategoryName)
                        }
                        educationalCategoryListId = it.lstCategory
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                getEducationalStreamResponseData.observe(this@AddEducationalDetailsActivity)
                {
                    if (it.status?.lowercase() == Constant.success) {
                        if (it.lstResignationReason.isNotEmpty()) {
                            educationalStreamList.clear()
                            for (i in it.lstResignationReason) {
                                i.streamName?.let { it1 -> educationalStreamList.add(it1) }

                            }
                            educationalStreamListId = it.lstResignationReason

                        } else {
                            showToast(it.message.toString())
                        }

                    } else {
                        showToast(it.message.toString())
                    }
                }
                getPOBInsertEducationInfoResponseData.observe(this@AddEducationalDetailsActivity) {
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@AddEducationalDetailsActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@AddEducationalDetailsActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun callEducationCategoryApi() {
        if (isNetworkAvailable()) {
            educationDetailsViewModel.callGetEducationCategoryApi()
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromAdd = getBoolean(Constant.IS_FROM_ADD, false)
            if (!isFromAdd) {
                educationDetailModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(
                        Constant.DATA,
                        ListEducationDetails::class.java
                    ) as ListEducationDetails
                } else
                    getSerializable(Constant.DATA) as ListEducationDetails

            } else {
                callEducationCategoryApi()
            }
        }
        setUpToolbar()
        setUpRecyclerList()
    }

    private fun setUpAdapter() {

        educationalCategoryAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            educationalCategoryList
        )

        educationalStreamAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            educationalStreamList
        )

        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        } else {
            adapter =
                DetailAdapter(
                    this,
                    list,
                    educationalCategoryAdapter = educationalCategoryAdapter,
                    educationalStreamAdapter = educationalStreamAdapter,
                    listener = this
                )
            binding.recyclerAddEducationDetails.adapter = adapter
        }

    }

    private fun setUpRecyclerList() {
        list.add(
            DetailModel(
                title = getString(R.string.highest_education),
                itemType = DetailItemType.SPINNER,
                isEnabled = isFromAdd,
                spinnerType = SpinnerType.EDUCATION,
                value = if (!isFromAdd) educationDetailModel?.CategoryName else ""
            )
        )

        if (isFromAdd) {
            list.add(
                DetailModel(
                    title = getString(R.string.qualification),
                    itemType = DetailItemType.SPINNER,
                    isEnabled = isFromAdd,
                    spinnerType = SpinnerType.EDUCATIONAL_STREAM,
                    value = educationalStreamList
                )
            )
        }
        list.add(
            DetailModel(
                title = getString(R.string.passing_year),
                itemType = DetailItemType.EDIT_TEXT_DATE,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) educationDetailModel?.PassYear else "",
                maxLength = 4
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.board_university_name),
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) educationDetailModel?.BoardName else ""
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.institute_school_name),
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) educationDetailModel?.InstituteName else ""
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.percentage_obtained),
                itemType = DetailItemType.EDIT_TEXT_DECIMAL,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) educationDetailModel?.Percentage?.replace(
                    "%",
                    ""
                ) else "",
                maxLength = if (isFromAdd) 3 else 10
            )
        )

        adapter.notifyDataSetChanged()


    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.educational_details)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            if (isFromAdd) {
                tvSave.apply {
                    visibility = VISIBLE
                    text = getString(R.string.save)
                    setOnClickListener {
                        educationDetailsViewModel.validatePOBInsertEducationInfoModel(
                            this@AddEducationalDetailsActivity, getPOBInsertEducationInfoModel()
                        )
                    }
                }
            }
        }
    }


    private fun getPOBInsertEducationInfoModel(): InsertEducationInfoRequestModel {
        val listEducationCategoryId =
            educationalCategoryListId.find { it.CategoryName == list[EducationDetailsItems.CATEGORY_NAME.value].value.toString() }
        val tt = (listEducationCategoryId?.EducationCategoryID ?: 0)

        val request = InsertEducationInfoRequestModel()
        request.EducationCategoryName =
            list[EducationDetailsItems.CATEGORY_NAME.value].value.toString()
        request.QualificationType =
            list[EducationDetailsItems.EDUCATIONAL_STREAM.value].value.toString().trim()
        request.EducationCategoryID = tt.toString().trim()
        request.PassYear = list[EducationDetailsItems.PASSING_YEAR.value].value.toString().trim()
        request.BoardName = list[EducationDetailsItems.BOARD.value].value.toString().trim()
        request.InstituteName = list[EducationDetailsItems.INSTITUTE.value].value.toString().trim()
        request.Percentage = list[EducationDetailsItems.PERCENTAGE.value].value.toString().trim()
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        return request

    }

    override fun onValidationSuccess(type: String, msg: Int) {
        clearError()
        callInsertEducationDetailsApi()
    }

    private fun callInsertEducationDetailsApi() {
        if (isNetworkAvailable()) {
            educationDetailsViewModel.callPOBInsertEducationInfoApi(
                getPOBInsertEducationInfoModel()
            )
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

    override fun onValidationFailure(type: String, msg: Int) {
        clearError()
        binding.apply {
            when (type) {

                Constant.ListenerConstants.EDUCATION_CATEGORY_NAME_ERROR -> {
                    list[EducationDetailsItems.CATEGORY_NAME.value].apply {
                        error = getString(msg)
                        isFocus = true
                    }
                }

                Constant.ListenerConstants.EDUCATIONAL_STREAM_NAME_ERROR -> {
                    list[EducationDetailsItems.EDUCATIONAL_STREAM.value].apply {
                        error = getString(msg)
                        isFocus = true
                    }
                }

                Constant.ListenerConstants.EDUCATIONAL_STREAM_ERROR -> {
                    list[EducationDetailsItems.EDUCATIONAL_STREAM.value].apply {
                        error = getString(msg)
                        isFocus = true

                    }
                }

                Constant.ListenerConstants.EDUCATION_PASSING_YEAR_ERROR -> {
                    list[EducationDetailsItems.PASSING_YEAR.value].apply {
                        error = getString(msg)
                        isFocus = true
                    }
                }

                Constant.ListenerConstants.EDUCATION_BOARD_NAME_ERROR -> {
                    list[EducationDetailsItems.BOARD.value].apply {
                        error = getString(msg)
                        isFocus = true
                    }
                }

                Constant.ListenerConstants.EDUCATION_SCHOOL_NAME_ERROR -> {
                    list[EducationDetailsItems.INSTITUTE.value].apply {
                        error = getString(msg)
                        isFocus = true
                    }
                }

                Constant.ListenerConstants.EDUCATION_MARKS_OBTAINED_ERROR -> {
                    list[EducationDetailsItems.PERCENTAGE.value].apply {
                        error = getString(msg)
                        isFocus = true
                    }
                }
            }
        }
    }

    private fun clearError() {
        for (i in list) {
            i.error = ""
            i.isFocus = false
        }
        adapter.notifyDataSetChanged()
    }

    override fun openDatePicker(position: Int) {
        DialogUtils.showYearPickerDialog(this, this)
    }

    override fun onSelectYear(Year: String) {
        list[2].value = Year
        adapter.notifyDataSetChanged()
    }

    override fun onSpinnerDataSelected(selectedPosition: Int, data: String, itemPosition: Int) {
        clearError()
        if (data == "Illiterate") {
            for (i in list) {
                i.isEnabled = false
            }
            list[0].isEnabled = true
            adapter.notifyDataSetChanged()
        } else {
            for (i in list) {
                i.isEnabled = true
            }
            if (itemPosition == 0) {
                callEducationStreamApi()
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun callEducationStreamApi() {
        if (isNetworkAvailable()) {

            val listEducationCategoryId =
                educationalCategoryListId.find { it.CategoryName == list[EducationDetailsItems.CATEGORY_NAME.value].value.toString() }
            val tt = (listEducationCategoryId?.EducationCategoryID ?: 0)

            educationDetailsViewModel.callEducationalStreamApi(
                request = EducationalStreamRequestModel(
                    EducationCategoryID
                    = tt.toString()
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }
}