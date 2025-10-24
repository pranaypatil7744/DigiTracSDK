package com.example.digitracksdk.presentation.leaves.apply_leave

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.ListAppliedLeaveModel
import com.example.digitracksdk.domain.model.leaves.ListAttnRegularizationType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityApplyLeaveBinding
import com.example.digitracksdk.databinding.BottomSheetNewLeaveRequestBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceResponseModel
import com.example.digitracksdk.domain.model.leaves.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.leaves.LeavesViewModel
import com.example.digitracksdk.presentation.leaves.apply_leave.adapter.ApplyLeavesAdapter
import com.example.digitracksdk.presentation.leaves.apply_leave.model.ApplyLeavesModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesType
import com.example.digitracksdk.presentation.leaves.apply_leave.model.MyLeaveDashboardModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

enum class LeaveTypes(val value: Int) {
    Pl(8),
    ClSlCombined(13),
    BirthdayLeave(9),
    OptionalLeave(10),
}

class ApplyLeaveActivity : BaseActivity(), ValidationListener {
    lateinit var binding: ActivityApplyLeaveBinding
    private val leavesViewModel: LeavesViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var bottomSheetDialog: BottomSheetDialog
    var myLeavesList: ArrayList<MyLeaveDashboardModel> = ArrayList()
    var applyLeavesList: ArrayList<ApplyLeavesModel> = ArrayList()
    var leavesTypeList: ArrayList<ListAttnRegularizationType> = ArrayList()
    var innovId: String? = ""
    var gnetAssociateId: String? = ""
    var selectedLeaveType: Int = 0
    var selectedFromDate: Calendar? = null

    var cl: String? = ""
    var pl: String? = ""
    var sl: String? = ""
    lateinit var bottomSheetNewLeavesRequestBinding: BottomSheetNewLeaveRequestBinding
    lateinit var applyLeavesAdapter: ApplyLeavesAdapter
    private var  fromScreen : String? =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityApplyLeaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        leavesViewModel.validationListener = this
        setUpToolbar()
        setObserver()
        getIntentData()
        getPreferenceData()
        setUpApplyLeavesAdapter()
        callBalanceLeavesApi()
        callViewLeaveRequestListApi()
        setUpListener()
    }

    private fun getIntentData() {
        intent.extras?.run {
            fromScreen  = getString(Constant.SCREEN_NAME)
        }
    }

    private fun setObserver() {
        with(leavesViewModel) {
            viewLeavesRequestListResponse.observe(this@ApplyLeaveActivity) {
                setUpListData(it.lstAppliedLeave ?: ArrayList())
            }
            leavesBalanceResponse.observe(
                this@ApplyLeaveActivity
            ) {
                setUpDashboardLeaves(it)
            }

            applyLeavesResponse.observe(
                this@ApplyLeaveActivity
            ) {
                if (it.Status == Constant.SUCCESS) {
                    bottomSheetDialog.dismiss()
                    selectedLeaveType = 0
                    showToast(it.Message.toString())
                    callBalanceLeavesApi()
                    callViewLeaveRequestListApi()
                } else {
                    showToast(it.Message.toString())
                }
            }
            leavesTypeResponse.observe(this@ApplyLeaveActivity) {
                if (!it.LstAttnRegularizationType.isNullOrEmpty()) {
                    leavesTypeList.clear()
                    leavesTypeList.addAll(it.LstAttnRegularizationType ?: arrayListOf())

                    val issueList: ArrayList<String> = ArrayList()
                    issueList.clear()
                    for (i in leavesTypeList) {
                        issueList.add(i.RegularizationType.toString())
                    }
                    val adapter = ArrayAdapter(
                        this@ApplyLeaveActivity,
                        android.R.layout.simple_list_item_1, issueList
                    )
                    bottomSheetNewLeavesRequestBinding.etLeaveType.setAdapter(adapter)
                } else {
                    showToast(it.Message.toString())
                }
            }
            messageData.observe(
                this@ApplyLeaveActivity
            ) { it ->
                it?.let { it1 -> showToast(it1) }
            }
            showProgressBar.observe(this@ApplyLeaveActivity) {
                toggleLoader(it)
            }
        }
    }

    private fun getPreferenceData() {
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun callViewLeaveRequestListApi() {
        if (isNetworkAvailable()) {
            binding.layoutNoInternet.root.visibility = GONE
            binding.recyclerApplyLeave.visibility = VISIBLE
            leavesViewModel.callViewLeaveRequestListApi(getLeaveListRequestModel())
        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
            binding.recyclerApplyLeave.visibility = GONE
        }
        binding.layoutNoData.root.visibility = GONE
    }

    private fun getLeaveListRequestModel(): LeaveRequestViewRequestModel {
        val request = LeaveRequestViewRequestModel()
        request.GNETAssociateID = gnetAssociateId
        request.InnovID = innovId
        request.FromDate = ""
        request.ToDate = ""
        return request
    }

    private fun getBalanceLeavesRequestModel(): LeaveBalanceRequestModel {
        val request = LeaveBalanceRequestModel()
        request.GNETAssociateID = gnetAssociateId
        request.InnovID = innovId
        request.Month = AppUtils.INSTANCE?.getCurrentMonth()
        request.Year = AppUtils.INSTANCE?.getCurrentYear()
        return request
    }

    private fun callBalanceLeavesApi() {
        if (isNetworkAvailable()) {
            binding.layoutNoInternet.root.visibility = GONE
            binding.recyclerApplyLeave.visibility = VISIBLE
            leavesViewModel.callLeaveBalanceApi(getBalanceLeavesRequestModel())
        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
            binding.recyclerApplyLeave.visibility = GONE
        }
        binding.layoutNoData.root.visibility = GONE
    }

    private fun setUpDashboardLeaves(data: LeaveBalanceResponseModel) {
        cl = data.CL
        sl = data.SL
        pl = data.PL
        myLeavesList.clear()
        myLeavesList.add(
            MyLeaveDashboardModel(
                leaveMsg = data.PLText,
                remainingLeaves = data.PL ?: "0",
                leavesIcon = R.drawable.ic_annual_leaves
            )
        )
        val cl = data.CL?.toDouble() ?: 0.0
        val sl = data.SL?.toDouble() ?: 0.0
        val combined = cl.plus(sl)
        myLeavesList.add(
            MyLeaveDashboardModel(
                leaveMsg = data.CLText,
                remainingLeaves = data.CL ?: "0",
                leavesIcon = R.drawable.ic_casual_leave
            )
        )
        myLeavesList.add(
            MyLeaveDashboardModel(
                leaveMsg = data.SLText,
                remainingLeaves = data.SL ?: "0",
                leavesIcon = R.drawable.ic_sick_leave
            )
        )
//        myLeavesList.add(
//            MyLeaveDashboardModel(
//                leaveMsg = getString(R.string.maternity_paternity_leave),
//                remainingLeaves = "00",
//                leavesIcon = R.drawable.ic_marriage_leave
//            )
//        )
//        myLeavesList.add(
//            MyLeaveDashboardModel(
//                leaveMsg = getString(R.string.unpaid_leave_absent),
//                remainingLeaves = "00",
//                leavesIcon = R.drawable.ic_unpaid_leave
//            )
//        )
//        myLeavesList.add(
//            MyLeaveDashboardModel(
//                leaveMsg = "Paternity Leave",
//                remainingLeaves = "03",
//                totalLeaves = "20",
//                leavesIcon = R.drawable.ic_paternity_leave
//            )
//        )

        applyLeavesAdapter.notifyDataSetChanged()
    }

    private fun setUpListener() {

        onBackPressedDispatcher.addCallback(this@ApplyLeaveActivity , object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {

                if (fromScreen==getString(R.string.attendance_calendar))
                {
                    setResult(RESULT_OK)
                    finish()
                }else{
                    finish()
                }
            }

        })
        binding.apply {
            fabApplyLeave.setOnClickListener {
                openNewLeaveRequestBottomSheet()
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callBalanceLeavesApi()
                callViewLeaveRequestListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callBalanceLeavesApi()
                callViewLeaveRequestListApi()
            }
        }

    }

    private fun openNewLeaveRequestBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_new_leave_request, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetNewLeavesRequestBinding = BottomSheetNewLeaveRequestBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        setupLeaveDropdown()
        setupSubLeaveDropdown()
        bottomSheetNewLeavesRequestBinding.apply {
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnApply.setOnClickListener {
                clearError()
                leavesViewModel.validateApplyLeaveRequest(
                    getApplyLeaveRequestModel(),
                    this@ApplyLeaveActivity
                )
            }
            etLeaveType.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    selectedLeaveType = leavesTypeList[position].RegularizationTypeID ?: 0
                    if (selectedLeaveType == LeaveTypes.Pl.value) {
                        tvBalanceLeaveValue.text = pl
                    } else if (selectedLeaveType == 12) {
                        tvBalanceLeaveValue.text = sl
                    } else if (selectedLeaveType == LeaveTypes.ClSlCombined.value) {
                        val cl = cl?.toDouble() ?: 0.0
                        val sl = sl?.toDouble() ?: 0.0
                        val combined = cl.plus(sl)
                        tvBalanceLeaveValue.text = cl.toString()
                    } else {
                        tvBalanceLeaveValue.text = "0.0"
                    }
                    if (selectedLeaveType == LeaveTypes.BirthdayLeave.value || selectedLeaveType == LeaveTypes.OptionalLeave.value || bottomSheetNewLeavesRequestBinding.etSubLeaveType.text.toString() == getString(
                            R.string.half_day
                        )
                    ) {
                        bottomSheetNewLeavesRequestBinding.apply {
                            layoutToDate.visibility = GONE
                            etToDate.setText("")
                        }
                    } else {
                        bottomSheetNewLeavesRequestBinding.layoutToDate.visibility = VISIBLE
                    }
                }

            etSubLeaveType.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    if (selectedLeaveType == LeaveTypes.BirthdayLeave.value || selectedLeaveType == LeaveTypes.OptionalLeave.value || bottomSheetNewLeavesRequestBinding.etSubLeaveType.text.toString() == getString(
                            R.string.half_day
                        )
                    ) {
                        bottomSheetNewLeavesRequestBinding.apply {
                            layoutToDate.visibility = GONE
                            etToDate.setText("")
                        }
                    } else {
                        bottomSheetNewLeavesRequestBinding.layoutToDate.visibility = VISIBLE
                    }
                }
            etFromDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    this@ApplyLeaveActivity,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedFromDate = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd MMM yyyy"
                            )
                        etFromDate.setText(selectedDate)
                        etToDate.setText("")
                    },
                    y,
                    m,
                    d
                )
                val minDateCalendar = Calendar.getInstance()
                minDateCalendar.set(y, Calendar.JANUARY, 1)
                datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis

                val maxDateCalendar = Calendar.getInstance()
                maxDateCalendar.set(y, Calendar.DECEMBER, 31)
                datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis

                datePickerDialog.show()
            }
            etToDate.setOnClickListener {
                val cal = Calendar.getInstance()
                var y = cal.get(Calendar.YEAR)
                var m = cal.get(Calendar.MONTH)
                var d = cal.get(Calendar.DAY_OF_MONTH)


                if (selectedFromDate != null) {
                    y = selectedFromDate!!.get(Calendar.YEAR)
                    m = selectedFromDate!!.get(Calendar.MONTH)
                    d = selectedFromDate!!.get(Calendar.DAY_OF_MONTH)
                }

                val datePickerDialog = DatePickerDialog(
                    this@ApplyLeaveActivity,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd MMM yyyy"
                            )
                        etToDate.setText(selectedDate)
                    },
                    y,
                    m,
                    d
                )
                if (selectedFromDate == null) {
//                    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                    val minDateCalendar = Calendar.getInstance()
                    minDateCalendar.set(y, Calendar.JANUARY, 1)
                    datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis
                } else {
                    datePickerDialog.datePicker.minDate = selectedFromDate?.time?.time!!


                }
                val maxDateCalendar = Calendar.getInstance()
                maxDateCalendar.set(y, Calendar.DECEMBER, 31)
                datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis
                datePickerDialog.show()
            }
        }
    }

    private fun getApplyLeaveRequestModel(): ApplyLeaveRequestModel {
        val request = ApplyLeaveRequestModel()
        request.Empcode = gnetAssociateId
        request.InTime = ""
        request.LeaveAppliedFor =
            bottomSheetNewLeavesRequestBinding.etSubLeaveType.text.toString().trim()
        request.Location = ""
        request.OutTime = ""
        request.RegularizationDate =
            bottomSheetNewLeavesRequestBinding.etFromDate.text.toString().trim()
        request.ToDate = bottomSheetNewLeavesRequestBinding.etToDate.text.toString().trim()
        request.RequestTypeId = selectedLeaveType
        request.Remarks = bottomSheetNewLeavesRequestBinding.etReason.text.toString().trim()
        return request
    }

    private fun callApplyLeaveApi() {
        if (isNetworkAvailable()) {
            leavesViewModel.callApplyLeaveApi(getApplyLeaveRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }


    private fun setupLeaveDropdown() {
        if (isNetworkAvailable()) {
            leavesViewModel.callLeavesTypeApi(request = CommonRequestModel(InnovId = innovId))
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setupSubLeaveDropdown() {
        val issueList = listOf<String>(
            getString(R.string.Select), getString(R.string.full_day), getString(
                R.string.half_day
            )
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, issueList
        )
        bottomSheetNewLeavesRequestBinding.etSubLeaveType.setAdapter(adapter)
    }

    private fun setUpListData(data: ArrayList<ListAppliedLeaveModel>) {
        applyLeavesList.clear()
        applyLeavesList.add(
            ApplyLeavesModel(
                myLeaveDashboardList = myLeavesList, leavesDisplayType = LeavesType.MY_LEAVES
            )
        )
        applyLeavesList.add(
            ApplyLeavesModel(
                leaveMsg = getString(R.string.leave_history), leavesDisplayType = LeavesType.TITLE
            )
        )
        if (data.isNotEmpty()) {
            for (i in data) {
                //TODO update this
                applyLeavesList.add(
                    ApplyLeavesModel(
                        leaveTitle = i.RegularizationType,
                        leaveMsg = i.Remarks,
                        startDate = AppUtils.INSTANCE?.convertDateFormat(
                            "MM/dd/yyyy hh:mm:ss",
                            i.RegularizationDate.toString(),
                            "dd MMM yyyy"
                        ),
                        endDate = "",
                        totalDays = AppUtils.INSTANCE?.convertDateFormat(
                            "MM/dd/yyyy hh:mm:ss",
                            i.ApprovedDate.toString(),
                            "dd MMM yyyy"
                        ),
                        leaveStatus = AppUtils.INSTANCE?.getLeaveStatus(i.Status ?: ""),
                        leavesDisplayType = LeavesType.LEAVES_HISTORY
                    )
                )
            }

        } else {
            binding.layoutNoData.root.visibility = VISIBLE
            showToast(getString(R.string.no_leave_history_found))
        }
        applyLeavesAdapter.notifyDataSetChanged()


    }

    private fun setUpApplyLeavesAdapter() {
        applyLeavesAdapter = ApplyLeavesAdapter(this, applyLeavesList, myLeavesList)
        binding.recyclerApplyLeave.adapter = applyLeavesAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.apply_leave)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                if (fromScreen == getString(R.string.attendance_calendar))
                {
                    setResult(RESULT_OK)
                    finish()
                }else{
                    finish()
                }
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
        callApplyLeaveApi()
    }

    override fun onValidationFailure(type: String, msg: Int) {
        bottomSheetNewLeavesRequestBinding.apply {
            when (type) {
                Constant.ListenerConstants.LEAVE_TYPE_ERROR -> {
                    layoutLeaveType.error = getString(msg)
                }
                Constant.ListenerConstants.LEAVE_SUB_TYPE_ERROR -> {
                    layoutSubLeaveType.error = getString(msg)
                }
                Constant.ListenerConstants.FROM_DATE_ERROR -> {
                    layoutFromDate.error = getString(msg)
                }
                Constant.ListenerConstants.TO_DATE_ERROR -> {
                    layoutToDate.error = getString(msg)
                }
                Constant.ListenerConstants.REASON_ERROR -> {
                    layoutReason.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        bottomSheetNewLeavesRequestBinding.apply {
            layoutLeaveType.error = ""
            layoutSubLeaveType.error = ""
            layoutFromDate.error = ""
            layoutToDate.error = ""
            layoutReason.error = ""
        }
    }
}