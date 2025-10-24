package com.example.digitracksdk.presentation.attendance.attendance_regularization

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAttendanceRegularizationBinding
import com.example.digitracksdk.databinding.BottomSheetNewLeaveRequestBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.ListAttnRegularizationTypeModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.attendance_regularization.adapter.AttendanceRegularizationAdapter
import com.example.digitracksdk.presentation.attendance.attendance_regularization.model.AttendanceRegularizationModel
import com.example.digitracksdk.presentation.leaves.LeavesViewModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

enum class RequestType(val value: Int) {
    OD(1), ATTENDANCE(2), LEAVE(3), WO(4)
}

class AttendanceRegularizationActivity : BaseActivity(), ValidationListener {
    lateinit var binding: ActivityAttendanceRegularizationBinding
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetNewAttendanceRegularizationBinding: BottomSheetNewLeaveRequestBinding
    private val attendanceRegularizationViewModel: AttendanceRegularizationViewModel by viewModel()
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val leavesViewModel: LeavesViewModel by viewModel()

    lateinit var attendanceRegularizationAdapter: AttendanceRegularizationAdapter
    private var regularizationList: ArrayList<AttendanceRegularizationModel> = ArrayList()
    private var regularizationTypeList: ArrayList<ListAttnRegularizationTypeModel> = ArrayList()
    var innovId: String = ""
    var gnetAssociateId: String = ""
    var selectedFromDate: Calendar? = null
    var selectedRequestTypeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceRegularizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        attendanceRegularizationViewModel.validationListener = this
        setUpToolbar()
        setUpObserver()
        getPreferenceData()
        setUpRegularizationAdapter()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {

            with(attendanceRegularizationViewModel) {
                attendanceRegularizationListResponseData.observe(
                    this@AttendanceRegularizationActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.success) {
                        if (!it.lstAttendanceRegularize.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            regularizationList.clear()
                            for (i in it.lstAttendanceRegularize ?: arrayListOf()) {
                                regularizationList.add(
                                    AttendanceRegularizationModel(
                                        requestMsg = i.RegularizationType,
                                        requestType = i.Remarks,
                                        requestDate = AppUtils.INSTANCE?.convertDateFormat(
                                            "MM/dd/yyyy hh:mm:ss",
                                            i.RegularizationDate.toString(),
                                            "dd MMM yyyy"
                                        ),
                                        requestStatus = AppUtils.INSTANCE?.getLeaveStatus(i.Status.toString())
                                            ?: LeavesStatus.PENDING,
                                        responseDate = AppUtils.INSTANCE?.convertDateFormat(
                                            "MM/dd/yyyy hh:mm:ss",
                                            i.ApprovedDate.toString(),
                                            "dd MMM yyyy"
                                        )
                                    )
                                )
                            }
                            attendanceRegularizationAdapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                attendanceRegularizationTypeResponseData.observe(
                    this@AttendanceRegularizationActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        if (it.LstAttnRegularizationType?.size != 0) {
                            val list: ArrayList<String> = ArrayList()
                            list.clear()
                            regularizationTypeList.clear()
                            regularizationTypeList.addAll(
                                it.LstAttnRegularizationType ?: arrayListOf()
                            )
                            for (i in regularizationTypeList) {
                                list.add(i.RegularizationType.toString())
                            }
                            val adapter = ArrayAdapter(
                                this@AttendanceRegularizationActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                list
                            )
                            bottomSheetNewAttendanceRegularizationBinding.etLeaveType.setAdapter(
                                adapter
                            )
                        } else {
                            showToast(it.Message.toString())
                        }
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                insertAttendanceRegularizationResponseData.observe(
                    this@AttendanceRegularizationActivity
                ) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        bottomSheetDialog.dismiss()
                        selectedRequestTypeId = 0
                        callAttendanceRegularizationList()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                attendanceRegularizationInsertResponseData.observe(
                    this@AttendanceRegularizationActivity
                ) { it ->
                    toggleLoader(false)
                    when (it.Status) {
                        "1" -> {
                            showToast(getString(R.string.apply_successfully))
                            bottomSheetDialog.dismiss()
                            selectedRequestTypeId = 0
                            callAttendanceRegularizationList()
                        }
                        "2" -> {
                            showToast(getString(R.string.already_applied))
                            selectedRequestTypeId = 0
                            bottomSheetDialog.dismiss()
                            callAttendanceRegularizationList()
                        }
                        else -> {
                            showToast(getString(R.string.applied_failed))
                        }
                    }
                }

                messageData.observe(this@AttendanceRegularizationActivity) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }
            }

            with(leavesViewModel) {
                leavesBalanceResponse.observe(this@AttendanceRegularizationActivity) { it ->
                    val cl = it.CL?.toDouble() ?: 0.0
                    val pl = it.PL?.toDouble() ?: 0.0
                    val sl = it.SL?.toDouble() ?: 0.0
                    val total = cl + pl + sl
                    bottomSheetNewAttendanceRegularizationBinding.apply {
                        tvBalanceLeaveValue.text = total.toString()

                        tvBalanceLeave.visibility = VISIBLE
                        tvBalanceLeaveValue.visibility = VISIBLE
                    }
                }
                messageData.observe(
                    this@AttendanceRegularizationActivity
                ) { it -> showToast(it.toString()) }
            }

            with(attendanceViewModel) {
                attendanceValidationResponseData.observe(
                    this@AttendanceRegularizationActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Message == Constant.SUCCESS) {
                        callInsertAttendanceRegularizationApi()
//                            callAttendanceRegularizationInsertApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@AttendanceRegularizationActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

        }
    }

    private fun callAttendanceRegularizationInsertApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceRegularizationViewModel.callAttendanceRegularizationInsertApi(
                getInsertAttendanceRegularizationRequestModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onResume() {
        super.onResume()
        callAttendanceRegularizationList()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callAttendanceRegularizationList()
            }
            fabAddAttRegularization.setOnClickListener {
                openNewAttendanceRegularizationBottomSheet()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callAttendanceRegularizationList()
            }
        }
    }

    private fun getInsertAttendanceRegularizationRequestModel(): InsertAttendanceRegularizationRequestModel {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            val request = InsertAttendanceRegularizationRequestModel()
            request.Empcode = gnetAssociateId
            request.InTime =
                if (etInTime.text.toString().trim().isEmpty()) "00:00" else etInTime.text.toString()
                    .trim()
            request.Location = if (etLocation.text.toString().trim()
                    .isEmpty()
            ) "null" else etLocation.text.toString().trim()
            request.OutTime = if (etOutTime.text.toString().trim()
                    .isEmpty()
            ) "00:00" else etOutTime.text.toString().trim()
            request.RegularizationDate = etFromDate.text.toString().trim()
            request.Remarks = etReason.text.toString().trim()
            request.ToDate = etFromDate.text.toString().trim()
            request.RequestTypeId = selectedRequestTypeId
            request.LeaveAppliedFor = ""
            return request
        }
    }


    private fun callAttendanceRegularizationList() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            binding.layoutNoInternet.root.visibility = GONE
            //TODO convert Date format
            attendanceRegularizationViewModel.callAttendanceRegularizationListApi(
                request = AttendanceRegularizationListRequestModel(
                    FromDate = AppUtils.INSTANCE?.getCurrentDate().toString(),
                    GNETAssociateID = gnetAssociateId,
                    InnovId = innovId,
                    ToDate = AppUtils.INSTANCE?.getCurrentDate().toString()
                )
            )

        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
            binding.recyclerAttendanceRegularization.visibility= GONE
        }
        binding.layoutNoData.root.visibility= GONE
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun setUpRegularizationAdapter() {
        attendanceRegularizationAdapter = AttendanceRegularizationAdapter(this, regularizationList)
        binding.recyclerAttendanceRegularization.adapter = attendanceRegularizationAdapter
    }

    private fun openNewAttendanceRegularizationBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_new_leave_request, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetNewAttendanceRegularizationBinding = BottomSheetNewLeaveRequestBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        callAttendanceRegularizationTypeApi()

        bottomSheetNewAttendanceRegularizationBinding.apply {
            tvNewLeaveRequest.text = getString(R.string.new_attendance_regularization)
            layoutLeaveType.hint = getString(R.string.request_type)
            layoutSubLeaveType.visibility = GONE
            layoutToDate.visibility = GONE
            layoutInTime.visibility = VISIBLE
            layoutOutTime.visibility = VISIBLE
            btnApply.text = getString(R.string.apply)
            layoutReason.hint = getString(R.string.remark)
            layoutFromDate.hint = getString(R.string.date)
            tvBalanceLeave.visibility = GONE
            tvBalanceLeaveValue.visibility = GONE
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnApply.setOnClickListener {
                clearError()
                attendanceRegularizationViewModel.validateAttendanceRegularization(
                    getInsertAttendanceRegularizationRequestModel()
                )
            }
            etLeaveType.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    selectedRequestTypeId =
                        regularizationTypeList[position].RegularizationTypeID ?: 0
                    hideViews()
                    clearData()
                    clearError()
                    when (selectedRequestTypeId) {
                        RequestType.OD.value -> {
                            layoutLocation.visibility = VISIBLE
                            layoutOutTime.visibility = VISIBLE
                            layoutInTime.visibility = VISIBLE
                        }
                        RequestType.ATTENDANCE.value -> {
                            layoutOutTime.visibility = VISIBLE
                            layoutInTime.visibility = VISIBLE
                        }
                        RequestType.LEAVE.value -> {
                            callBalanceLeavesApi()
                        }
                        RequestType.WO.value -> {

                        }
                    }
                }

            etInTime.setOnClickListener {
                AppUtils.INSTANCE?.openTimePicker(etInTime, this@AttendanceRegularizationActivity)
            }
            etOutTime.setOnClickListener {
                AppUtils.INSTANCE?.openTimePicker(etOutTime, this@AttendanceRegularizationActivity)
            }
            etFromDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@AttendanceRegularizationActivity, { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedFromDate = selectedCalendar
                        val selectedDate = AppUtils.INSTANCE?.convertDateToString(
                            selectedCalendar.time, "dd MMM yyyy"
                        )
                        etFromDate.setText(selectedDate)
                        etToDate.setText("")
                    }, y, m, d
                )
                val minDateCalendar = Calendar.getInstance()
                minDateCalendar.set(y, Calendar.JANUARY, 1)
                datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis

                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
//            etToDate.setOnClickListener {
//                val cal = Calendar.getInstance()
//                val y = cal.get(Calendar.YEAR)
//                val m = cal.get(Calendar.MONTH)
//                val d = cal.get(Calendar.DAY_OF_MONTH)
//
//
//                val datePickerDialog = DatePickerDialog(
//                    this@AttendanceRegularizationActivity,
//                    { view, year, monthOfYear, dayOfMonth ->
//                        val selectedCalendar = Calendar.getInstance()
//                        selectedCalendar.set(Calendar.YEAR, year)
//                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
//                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                        val selectedDate =
//                            AppUtils.INSTANCE?.convertDateToString(
//                                selectedCalendar.time,
//                                "dd MMM yyyy"
//                            )
//                        etToDate.setText(selectedDate)
//                    },
//                    y,
//                    m,
//                    d
//                )
//                if (selectedFromDate == null) {
//                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
//
//                } else {
//                    datePickerDialog.datePicker.minDate = selectedFromDate?.time?.time!!
//                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
//                }
//                datePickerDialog.show()
//            }
        }
    }

    private fun callBalanceLeavesApi() {
        if (isNetworkAvailable()) {
            binding.layoutNoInternet.root.visibility = GONE
            leavesViewModel.callLeaveBalanceApi(getBalanceLeavesRequestModel())
        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
        }
    }

    private fun getBalanceLeavesRequestModel(): LeaveBalanceRequestModel {
        val request = LeaveBalanceRequestModel()
        request.GNETAssociateID = gnetAssociateId
        request.InnovID = innovId
        request.Month = AppUtils.INSTANCE?.getCurrentMonth()
        request.Year = AppUtils.INSTANCE?.getCurrentYear()
        return request
    }

    private fun hideViews() {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            layoutLocation.visibility = GONE
            layoutInTime.visibility = GONE
            layoutOutTime.visibility = GONE
            tvBalanceLeave.visibility = INVISIBLE
            tvBalanceLeaveValue.visibility = INVISIBLE
        }
    }

    private fun callAttendanceRegularizationTypeApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceRegularizationViewModel.callAttendanceRegularizationTypeApi(
                request = CommonRequestModel(
                    InnovId = innovId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            tvTitle.text = getString(R.string.attendance_regularization)
            divider.visibility = VISIBLE
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
    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerAttendanceRegularization,
                show = show
            )
        }
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        var regularizationType =
            bottomSheetNewAttendanceRegularizationBinding.etLeaveType.text.toString().trim()

        if (regularizationType == "Attendance Regularization") {
            regularizationType = "AR"
        } else if (regularizationType == "Leave") {
            regularizationType = "LR"
        }
        callAttendanceValidationApi(regularizationType)
    }

    private fun callAttendanceValidationApi(regularizationType: String) {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                attendanceViewModel.callAttendanceValidationApi(
                    request = AttendanceValidationRequestModel(
                        FromDate = etFromDate.text.toString().replace(" ", "-"),
                        GNETAssociateID = gnetAssociateId,
                        InnovID = innovId,
                        ToDate = etFromDate.text.toString().replace(" ", "-"),
                        Type = regularizationType
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callInsertAttendanceRegularizationApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceRegularizationViewModel.callInsertAttendanceRegularizationApi(
                getInsertAttendanceRegularizationRequestModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun clearError() {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            layoutLeaveType.error = ""
            layoutFromDate.error = ""
            layoutToDate.error = ""
            layoutInTime.error = ""
            layoutOutTime.error = ""
            layoutReason.error = ""
            layoutLocation.error = ""
        }
    }

    private fun clearData() {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            etFromDate.setText("")
            etToDate.setText("")
            etInTime.setText("")
            etOutTime.setText("")
            etLocation.setText("")
            etReason.setText("")
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            when (type) {
                Constant.ListenerConstants.REQUEST_TYPE_ERROR -> {
                    layoutLeaveType.error = getString(msg)
                }
                Constant.ListenerConstants.FROM_DATE_ERROR -> {
                    layoutFromDate.error = getString(msg)
                }
//                Constant.ListenerConstants.TO_DATE_ERROR -> {
//                    layoutToDate.error = getString(msg)
//                }
                Constant.ListenerConstants.IN_TIME_ERROR -> {
                    layoutInTime.error = getString(msg)
                }
                Constant.ListenerConstants.OUT_TIME_ERROR -> {
                    layoutOutTime.error = getString(msg)
                }
                Constant.ListenerConstants.LOCATION_ERROR -> {
                    layoutLocation.error = getString(msg)
                }
                Constant.ListenerConstants.REMARK_ERROR -> {
                    layoutReason.error = getString(msg)
                }
            }
        }
    }
}