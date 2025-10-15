package com.example.digitracksdk.presentation.onboarding.epf_details

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityEpfdetailsBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEpfResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

enum class EPFItems(val value:Int){
    IS_EPF(0),
    IS_OLD_EPF_MEMBER(1),
    IS_OLD_EMP_PENSION(2),
    UAN(3),
    PREV_PF_ID(4),
    DATE_OF_EXIT(5)
}

class EPFDetailsActivity : BaseActivity(), DetailAdapter.DetailListener, ValidationListener {

    lateinit var binding: ActivityEpfdetailsBinding
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var detailAdapter: DetailAdapter
    var viewType: Constant.ViewType = Constant.ViewType.EDIT
    var isEligible:Boolean =  true
    var list: ArrayList<DetailModel> = ArrayList()
    private val yesNoList: ArrayList<String> = ArrayList()
    lateinit var yesNoAdapter: ArrayAdapter<String>
    var innovId: String = ""

    private val paperlessViewEpfViewModel: PaperlessViewEpfViewModel by viewModel()
    val request = POBInsertEpfModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEpfdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        paperlessViewEpfViewModel.validationListener = this
        setUpToolbar()
        setUpListener()
        getPreferenceData()
        setObserver()
        callViewEpfApi()
    }
    private fun setObserver(){
        binding.apply {
            with(paperlessViewEpfViewModel) {
                viewEpfResponseData.observe(this@EPFDetailsActivity
                ) {
                    if (it.status == Constant.SUCCESS) {
                        setUpRecycler(it)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                insertEpfResponseData.observe(this@EPFDetailsActivity
                ) {
                    if (it.status == Constant.SUCCESS) {
                        if (it.Message.equals(Constant.Data_Saved_Succesfully, true)) {
                            showToast(it.Message.toString())
                            finish()
                        } else {
                            showToast(it.Message.toString())
                        }
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                messageData.observe(this@EPFDetailsActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@EPFDetailsActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpData() {
        yesNoList.clear()
        yesNoList.add(getString(R.string.yes))
        yesNoList.add(getString(R.string.no))

        yesNoAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            yesNoList
        )
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewEpfApi()
            }
        }
    }

    private fun callViewEpfApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toolbar.tvSave.visibility = VISIBLE
                paperlessViewEpfViewModel.callViewEpfApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                toolbar.tvSave.visibility = GONE
                binding.layoutNoInternet.root.visibility = VISIBLE
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

    private fun setUpRecycler(data: PaperlessViewEpfResponseModel)
    {

        setUpData()
        list.clear()
        list.add(
            DetailModel(
                title = getString(R.string.are_you_eligible_for_epf),
                value = if (data.EmployeesPfCheck == "Yes") getString(R.string.yes) else if (data.EmployeesPfCheck == "No") getString(R.string.no) else "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.YESNO,
                isEnabled = viewType == Constant.ViewType.ADD
            )

        )

        list.add(
            DetailModel(
                title = getString(R.string.are_you_an_earlier_member_of_employees_provident_fund_scheme),
                value = if (data.EmployeesPensionSchemeCheck1952 == "Yes") getString(R.string.yes) else if (data.EmployeesPensionSchemeCheck1952 == "No") getString(R.string.no) else "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.YESNO,
                isEnabled = viewType == Constant.ViewType.ADD
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.are_you_an_earlier_member_of_employees_pension_scheme),
                value = if (data.EmployeesPensionSchemeCheck == "Yes") getString(R.string.yes) else if (data.EmployeesPensionSchemeCheck == "No") getString(R.string.no) else "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.YESNO,
                isEnabled = viewType == Constant.ViewType.ADD
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.uan_number),
                value = data.ExistUANNo,
                maxLength = 12,
                itemType = DetailItemType.EDIT_TEXT_NUMBER
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.previous_pf),
                value = data.ExistPFNo,
                itemType = DetailItemType.EDIT_ALPHA_NUMBER
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.date_of_exit),
                value = data.ExitDate?.let {
                    AppUtils.INSTANCE?.convertDateFormat("mm/dd/yyyy hh:mm:ss",
                        it,"dd MMM yyyy")
                },
                itemType = DetailItemType.EDIT_TEXT_DATE
            )
        )
        setUpAdapter()
    }

    private fun setUpAdapter() {

        if (::detailAdapter.isInitialized) {
            detailAdapter.notifyDataSetChanged()
        } else {
            detailAdapter = DetailAdapter(this, list, yesNoAdapter = yesNoAdapter, listener = this)
            binding.recyclerEpf.adapter = detailAdapter
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.epf_details)
            btnBack.setOnClickListener {
                finish()
            }
            tvSave.apply {
                visibility = VISIBLE
                text = getString(R.string.edit)
                setOnClickListener {
                    if (viewType == Constant.ViewType.EDIT) {
                        text = getString(R.string.save)
                        viewType = Constant.ViewType.ADD
                        enableFields()
                    } else {
                        clearError()
                        if (isEligible){
                            paperlessViewEpfViewModel.validateEPFRequestModel(getPOBInsertEpfModel())
                        }else{
                            showToast(context.getString(R.string.you_are_not_eligible_for_epf))
                        }
                    }
                }
            }
        }

    }

    private fun getPOBInsertEpfModel(): POBInsertEpfModel {
        request.EmployeesPensionSchemeCheck = if (list[EPFItems.IS_OLD_EMP_PENSION.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[EPFItems.IS_OLD_EMP_PENSION.value].value.toString() == getString(R.string.no)) Constant.No else ""
        request.EmployeesPensionSchemeCheck1952 = if (list[EPFItems.IS_OLD_EPF_MEMBER.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[EPFItems.IS_OLD_EPF_MEMBER.value].value.toString() == getString(R.string.no)) Constant.No else ""
        request.EmployeesPfCheck = if (list[EPFItems.IS_EPF.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[EPFItems.IS_EPF.value].value.toString() == getString(R.string.no)) Constant.No else ""
        request.ExistPFNo = list[EPFItems.PREV_PF_ID.value].value.toString()
        request.ExistUANNo = list[EPFItems.UAN.value].value.toString()
        request.ExitDate = list[EPFItems.DATE_OF_EXIT.value].value.toString()
        request.InnovID = innovId
        request.NomineeAddress = ""
        request.NomineeName = ""
        request.NomineePercentageAllocation = ""
        return request
    }

    private fun enableFields() {
        for (data in list) {
            data.isEnabled = true
        }
        detailAdapter.refresh(list)
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
                val dateToUpdate =
                    AppUtils.INSTANCE?.convertDateToString(selectedCalendar.time, "dd MMM yyyy")
                list[position].value = dateToUpdate
                detailAdapter.notifyItemChanged(position)
            }, y, m, d
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        callInsertEpfApi()
    }

    private fun callInsertEpfApi() {

        if (isNetworkAvailable()) {
            paperlessViewEpfViewModel.callInsertEpfApi(
                request
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onSpinnerDataSelected(selectedPosition: Int, data: String,itemPosition:Int) {
        if (viewType == Constant.ViewType.ADD){
            if (itemPosition == 0){
                if (data == getString(R.string.no)){
                    isEligible = false
                    for (i in list){
                        i.isEnabled = false
                    }
                    list[itemPosition].isEnabled = true
                    setUpAdapter()
                }else{
                    isEligible = true
                    for (i in list){
                        i.isEnabled = true
                    }
                    setUpAdapter()
                }
            }
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        when(type){
            Constant.ListenerConstants.EPF_ERROR -> {
                list[EPFItems.IS_EPF.value].error = getString(msg)
                list[EPFItems.IS_EPF.value].isFocus = true
            }
            Constant.ListenerConstants.EPF_PROVIDED_FUND_ERROR -> {
                list[EPFItems.IS_OLD_EPF_MEMBER.value].error = getString(msg)
                list[EPFItems.IS_OLD_EPF_MEMBER.value].isFocus = true
            }
            Constant.ListenerConstants.EPF_SCHEME_ERROR -> {
                list[EPFItems.IS_OLD_EMP_PENSION.value].error = getString(msg)
                list[EPFItems.IS_OLD_EMP_PENSION.value].isFocus = true
            }
            Constant.ListenerConstants.UAN_NO_ERROR -> {
                list[EPFItems.UAN.value].error = getString(msg)
                list[EPFItems.UAN.value].isFocus = true
            }
            Constant.ListenerConstants.PF_MEMBER_ID_ERROR -> {
                list[EPFItems.PREV_PF_ID.value].error = getString(msg)
                list[EPFItems.PREV_PF_ID.value].isFocus = true
            }
            Constant.ListenerConstants.EXIT_DATE_ERROR -> {
                list[EPFItems.DATE_OF_EXIT.value].error = getString(msg)
                list[EPFItems.DATE_OF_EXIT.value].isFocus = true
            }

        }
    }

    private fun clearError() {
        for (i in list){
            i.isFocus = false
            i.error = ""
        }
        detailAdapter.notifyDataSetChanged()
    }
}