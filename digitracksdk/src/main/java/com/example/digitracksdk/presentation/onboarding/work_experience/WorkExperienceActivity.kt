package com.example.digitracksdk.presentation.onboarding.work_experience

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityWorkExperienceBinding
import com.example.digitracksdk.domain.model.onboarding.ListWorkExpModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.MultiEditTextModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

enum class WorkExpItems(val value: Int) {
    IS_FRESHER(0),
    TOTAL_EXP(2),
    RELEVANT_EXP(4),
    CURRENTLY_EMP(5),
    COMPANY_DETAILS_LABEL(6),
    COMPANY_NAME(7),
    DATES(8),
    LAST_CTC(9),
    DESIGNATION(10)
}

class WorkExperienceActivity : BaseActivity(), ValidationListener, DetailAdapter.DetailListener {

    lateinit var binding: ActivityWorkExperienceBinding
    var viewType = Constant.ViewType.EDIT
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var detailAdapter: DetailAdapter
    private var currentlyEmployedList: ArrayList<String> = ArrayList()
    var list: ArrayList<DetailModel> = ArrayList()
    var listWorkExpModel = ListWorkExpModel()
    var isFromAdd: Boolean = false
    var isFirstTime:Boolean = false
    var selectedMinDate: Calendar? = null
    var totalExpYear = ""
    var totalExpMonth = ""
    var relevantExpYear = ""
    var relevantExpMonth = ""
    var isCurrentlyEmployed = ""
    private lateinit var currentlyEmployedAdapter: ArrayAdapter<String>
    private val paperlessViewWorkExpDetailsViewModel: PaperlessViewWorkExpDetailsViewModel by viewModel()
    var innovId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWorkExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        paperlessViewWorkExpDetailsViewModel.validationListener = this
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpAdapter()
        setUpObserver()
        getIntentData()
    }

    private fun setUpObserver() {
        binding.apply {
            with(paperlessViewWorkExpDetailsViewModel) {
                insertWorkExpResponseData.observe(this@WorkExperienceActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@WorkExperienceActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromAdd = getBoolean(Constant.IS_FROM_ADD, false)
            isFirstTime = getBoolean(Constant.IS_FIRST_TIME,false)
            if (!isFromAdd) {
                listWorkExpModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(
                        Constant.WORK_EXP_MODEL,
                        ListWorkExpModel::class.java) as ListWorkExpModel
                }else
                    getSerializable(Constant.WORK_EXP_MODEL) as ListWorkExpModel
            }
            if (!isFirstTime){
                totalExpYear = getString(Constant.TOTAL_EXP_YEAR).toString()
                totalExpMonth = getString(Constant.TOTAL_EXP_MONTH).toString()
                relevantExpYear = getString(Constant.RELEVANT_EXP_YEAR).toString()
                relevantExpMonth = getString(Constant.RELEVANT_EXP_MONTH).toString()
                if (!isFromAdd){
                    isCurrentlyEmployed = getString(Constant.IS_CURRENTLY_EMPLOYED).toString()
                }
            }
        }
        setUpData()
        setUpToolbar()
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpAdapter() {
        currentlyEmployedList.add(getString(R.string.yes))
        currentlyEmployedList.add(getString(R.string.no))
        currentlyEmployedAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            currentlyEmployedList
        )

        detailAdapter =
            DetailAdapter(
                this,
                list,
                currentlyEmployeedAdapter = currentlyEmployedAdapter,
                listener = this
            )
        binding.recyclerWorkExperience.adapter = detailAdapter
    }

    private fun callInsertExperienceApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paperlessViewWorkExpDetailsViewModel.callInsertWorkExpApi(getInsertWorkExpRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getInsertWorkExpRequestModel(): InsertWorkExpRequestModel {
        binding.apply {
            val isFresher: Boolean = list[WorkExpItems.IS_FRESHER.value].value.toString() == getString(R.string.yes)
            val request = InsertWorkExpRequestModel()
            request.InnovID = innovId
            request.TotalExpInYear =
                if (isFresher) "" else (list[WorkExpItems.TOTAL_EXP.value].value as? MultiEditTextModel)?.value1.toString()
            request.TotalExpMonth =
                if (isFresher) "" else (list[WorkExpItems.TOTAL_EXP.value].value as? MultiEditTextModel)?.value2.toString()
            request.totalRelevantExpYear =
                if (isFresher) "" else (list[WorkExpItems.RELEVANT_EXP.value].value as? MultiEditTextModel)?.value1.toString()
            request.totalRelevantExpMonth =
                if (isFresher) "" else (list[WorkExpItems.RELEVANT_EXP.value].value as? MultiEditTextModel)?.value2.toString()
            request.CompanyName =
                if (isFresher) "" else list[WorkExpItems.COMPANY_NAME.value].value.toString().trim()
            request.DateOfJoining =
                if (isFresher) "" else (list[WorkExpItems.DATES.value].value as? MultiEditTextModel)?.value1.toString()
            request.LastWorkingDate =
                if (isFresher) "" else (list[WorkExpItems.DATES.value].value as? MultiEditTextModel)?.value2.toString()
            request.Designation =
                if (isFresher) "" else list[WorkExpItems.DESIGNATION.value].value.toString().trim()
            request.LastCTC =
                if (isFresher) "" else list[WorkExpItems.LAST_CTC.value].value.toString()
            request.CurrentlyEmployed =
                if (isFresher) "" else if (list[WorkExpItems.CURRENTLY_EMP.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[WorkExpItems.CURRENTLY_EMP.value].value.toString() == getString(R.string.no)) Constant.No else ""
            request.isFresher = if (list[WorkExpItems.IS_FRESHER.value].value.toString()
                    .isEmpty() || list[WorkExpItems.IS_FRESHER.value].value.toString() == "null"
            ) {
                ""
            } else {
                if (isFresher) "True" else "False"
            }
            return request
        }
    }

    private fun setUpData() {


        list.add(
            DetailModel(
                title = getString(R.string.is_fresher),
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.CURRENTLY_EMPLOYEED,
                value = AppUtils.INSTANCE?.getYesNoFromBoolean(listWorkExpModel.IsFresher.toString())
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.total_experience),
                itemType = DetailItemType.LABEL
            )
        )
        list.add(
            DetailModel(
                title = "", value = MultiEditTextModel(
                    hint1 = getString(R.string.year),
                    hint2 = getString(R.string.months),
                    value1 = totalExpYear,
                    value2 = totalExpMonth,
                    maxLength1 = 2,
                    maxLength2 = 2
                ), itemType = DetailItemType.MULTI_EDITTEXT_WITH_NUMBER,
                isEnabled = isFirstTime
            )
        )

        list.add(
            DetailModel(
                title = getString(R.string.relevant_experience),
                itemType = DetailItemType.LABEL
            )
        )
        list.add(
            DetailModel(
                title = "", value = MultiEditTextModel(
                    hint1 = getString(R.string.year),
                    hint2 = getString(R.string.months),
                    value1 = relevantExpYear,
                    value2 = relevantExpMonth,
                    maxLength1 = 2,
                    maxLength2 = 2
                ), itemType = DetailItemType.MULTI_EDITTEXT_WITH_NUMBER,
                isEnabled = isFirstTime

            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.currently_employed),
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.CURRENTLY_EMPLOYEED,
                value = isCurrentlyEmployed
            )
        )

        list.add(
            DetailModel(
                title = getString(R.string.company_details),
                itemType = DetailItemType.LABEL
            )
        )

        list.add(
            DetailModel(
                title = getString(R.string.company_name),
                itemType = DetailItemType.EDIT_TEXT,
                value = listWorkExpModel.CompanyName ?: ""
            )
        )

        list.add(
            DetailModel(
                title = "", value = MultiEditTextModel(
                    hint1 = getString(R.string.date_of_joining),
                    hint2 = getString(R.string.last_working_date),
                    value1 = listWorkExpModel.StartDate ?: "",
                    value2 = listWorkExpModel.EndDate ?: ""
                ), itemType = DetailItemType.MULTI_EDITTEXT_WITH_DATE
            )
        )

        list.add(
            DetailModel(
                title = getString(R.string.last_ctc),
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                value = listWorkExpModel.Payment ?: ""
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.designation),
                itemType = DetailItemType.EDIT_TEXT,
                value = listWorkExpModel.Designation ?: ""
            )
        )
        detailAdapter.notifyDataSetChanged()
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }


    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.work_experience)
            divider.visibility = VISIBLE
            tvSave.apply {
                visibility = if (isFromAdd) {
                    enableFields()
                    VISIBLE
                } else {
                    GONE
                }
                text = getString(R.string.save)
                btnBack.setOnClickListener {
                    finish()
                }
                setOnClickListener {
                    paperlessViewWorkExpDetailsViewModel.validateWorkExpRequestModel(
                        getInsertWorkExpRequestModel(),isFirstTime
                    )
                }
            }
        }
    }

    private fun enableFields() {
        for (data in list) {
            data.isEnabled = true
        }
        if (!isFirstTime){
            list[WorkExpItems.TOTAL_EXP.value].isEnabled = false
            list[WorkExpItems.RELEVANT_EXP.value].isEnabled = false
        }
        detailAdapter.refresh(list)
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        clearError()
        callInsertExperienceApi()
    }

    override fun onValidationFailure(type: String, msg: Int) {
        clearError()
        when (type) {
            Constant.ListenerConstants.IS_FRESHER_ERROR -> {
                list[WorkExpItems.IS_FRESHER.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.TOTAL_YEAR_ERROR -> {
                (list[WorkExpItems.TOTAL_EXP.value].value as? MultiEditTextModel)?.apply {
                    error1 = getString(msg)
                    isFocus1 = true
                }
            }
            Constant.ListenerConstants.TOTAL_MONTH_ERROR -> {
                (list[WorkExpItems.TOTAL_EXP.value].value as? MultiEditTextModel)?.apply {
                    error2 = getString(msg)
                    isFocus2 = true
                }
            }
            Constant.ListenerConstants.RELEVANT_YEAR_ERROR -> {
                (list[WorkExpItems.RELEVANT_EXP.value].value as? MultiEditTextModel)?.apply {
                    error1 = getString(msg)
                    isFocus1 = true
                }
            }
            Constant.ListenerConstants.RELEVANT_MONTH_ERROR -> {
                (list[WorkExpItems.RELEVANT_EXP.value].value as? MultiEditTextModel)?.apply {
                    error2 = getString(msg)
                    isFocus2 = true
                }
            }
            Constant.ListenerConstants.RELEVANT_EXP_ERROR -> {
                (list[WorkExpItems.RELEVANT_EXP.value].value as? MultiEditTextModel)?.apply {
                    error1 = getString(msg)
                    isFocus1 = true
                    error2 = getString(msg)
                }
            }
            Constant.ListenerConstants.CURRENTLY_EMPLOYED_ERROR -> {
                list[WorkExpItems.CURRENTLY_EMP.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.COMPANY_NAME_ERROR -> {
                list[WorkExpItems.COMPANY_NAME.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.JOINING_DATE_ERROR -> {
                (list[WorkExpItems.DATES.value].value as? MultiEditTextModel)?.error1 =
                    getString(msg)
            }
            Constant.ListenerConstants.LAST_WORKING_DATE_ERROR -> {
                (list[WorkExpItems.DATES.value].value as? MultiEditTextModel)?.error2 =
                    getString(msg)
            }
            Constant.ListenerConstants.LAST_CTC_ERROR -> {
                list[WorkExpItems.LAST_CTC.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.DESIGNATION_ERROR -> {
                list[WorkExpItems.DESIGNATION.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
        }
        detailAdapter.notifyDataSetChanged()
    }

    private fun clearError() {
        for (i in list) {
            i.error = ""
            i.isFocus = false
        }
        (list[WorkExpItems.TOTAL_EXP.value].value as? MultiEditTextModel)?.apply {
            error1 = ""
            isFocus1 = false
            error2 = ""
            isFocus2 = false
        }
        (list[WorkExpItems.RELEVANT_EXP.value].value as? MultiEditTextModel)?.apply {
            error1 = ""
            isFocus1 = false
            error2 = ""
            isFocus2 = false
        }
        (list[WorkExpItems.DATES.value].value as? MultiEditTextModel)?.apply {
            error1 = ""
            isFocus1 = false
            error2 = ""
            isFocus2 = false
        }
        detailAdapter.notifyDataSetChanged()
    }

    override fun openDatePicker(position: Int) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, monthOfYear)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectedMinDate = selectedCalendar
                val dateToUpdate =
                    AppUtils.INSTANCE?.convertDateToString(selectedCalendar.time, "dd MMM yyyy")
                (list[position].value as? MultiEditTextModel)?.value1 = dateToUpdate
                (list[position].value as? MultiEditTextModel)?.value2 = ""
                detailAdapter.notifyItemChanged(position)
            }, y, m, d
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun openDatePicker2(position: Int) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, monthOfYear)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateToUpdate =
                    AppUtils.INSTANCE?.convertDateToString(selectedCalendar.time, "dd MMM yyyy")
                val dateOfJoining = (list[position].value as? MultiEditTextModel)?.value1
                if (dateOfJoining.equals(dateToUpdate)) {
                    showToast(getString(R.string.date_of_joining_and_last_working_cannot_be_same))
                } else {
                    (list[position].value as? MultiEditTextModel)?.value2 = dateToUpdate
                    detailAdapter.notifyItemChanged(position)
                }

            }, y, m, d
        )
        if (selectedMinDate != null) {
            datePickerDialog.datePicker.minDate = selectedMinDate?.time?.time!!
        }
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onSpinnerDataSelected(selectedPosition: Int, data: String, itemPosition: Int) {
        if (itemPosition == WorkExpItems.IS_FRESHER.value) {
            if (data == getString(R.string.yes)) {
                for (i in list) {
                    i.isEnabled = false
                    i.value = ""
                    i.error = ""
                }
                list[itemPosition].value = data
                list[itemPosition].isEnabled = true

                list[WorkExpItems.TOTAL_EXP.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.year),
                    hint2 = getString(R.string.months)
                )
                list[WorkExpItems.RELEVANT_EXP.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.year),
                    hint2 = getString(R.string.months)
                )
                list[WorkExpItems.DATES.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.date_of_joining),
                    hint2 = getString(R.string.last_working_date)
                )
                detailAdapter.notifyDataSetChanged()
            } else {
                binding.toolbar.tvSave.visibility = VISIBLE
                list[WorkExpItems.TOTAL_EXP.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.year),
                    hint2 = getString(R.string.months),
                    value1 = totalExpYear,
                    value2 = totalExpMonth,
                    maxLength1 = 2,
                    maxLength2 = 2
                )
                list[WorkExpItems.RELEVANT_EXP.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.year),
                    hint2 = getString(R.string.months),
                    value1 = relevantExpYear,
                    value2 = relevantExpMonth,
                    maxLength1 = 2,
                    maxLength2 = 2
                )
                list[WorkExpItems.DATES.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.date_of_joining),
                    hint2 = getString(R.string.last_working_date),
                    value1 = listWorkExpModel.StartDate ?: "",
                    value2 = listWorkExpModel.EndDate ?: ""
                )
                enableFields()
            }
        } else if (itemPosition == WorkExpItems.CURRENTLY_EMP.value) {
            if (data == getString(R.string.yes)) {
                list[WorkExpItems.COMPANY_NAME.value].title =
                    getString(R.string.current_company_name)
                list[WorkExpItems.DATES.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.date_of_joining),
                    hint2 = getString(R.string.last_working_date),
                    value1 = listWorkExpModel.StartDate ?: "",
                    value2 = listWorkExpModel.EndDate ?: "",
                    isEnable1 = true,
                    isEnable2 = false
                )
            } else {
                list[WorkExpItems.COMPANY_NAME.value].title =
                    getString(R.string.previous_company_name)
                list[WorkExpItems.DATES.value].value = MultiEditTextModel(
                    hint1 = getString(R.string.date_of_joining),
                    hint2 = getString(R.string.last_working_date),
                    value1 = listWorkExpModel.StartDate ?: "",
                    value2 = listWorkExpModel.EndDate ?: "",
                    isEnable1 = true,
                    isEnable2 = true
                )
            }
            detailAdapter.notifyDataSetChanged()
        }
    }

}