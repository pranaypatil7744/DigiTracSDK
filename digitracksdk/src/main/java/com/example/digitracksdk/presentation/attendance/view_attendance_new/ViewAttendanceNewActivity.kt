package com.example.digitracksdk.presentation.attendance.view_attendance_new


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityViewAttendanceNewBinding
import com.innov.digitrac.databinding.BottomSheetNewLeaveRequestBinding
import com.innov.digitrac.databinding.BottomSheetViewAttendanceFilterBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_model.ListViewAttendanceModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.ListAttnRegularizationTypeModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.ListAttnRegularizationType
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.CandidateViewModel
import com.example.digitracksdk.presentation.attendance.attendance_regularization.AttendanceRegularizationViewModel
import com.example.digitracksdk.presentation.attendance.attendance_regularization.RequestType
import com.example.digitracksdk.presentation.attendance.view_attendance.adapter.ViewAttendanceAdapter
import com.example.digitracksdk.presentation.attendance.view_attendance_new.adapter.ViewAttendanceDialogAdapter
import com.example.digitracksdk.presentation.attendance.view_attendance_new.model.DialogModel
import com.example.digitracksdk.presentation.home.pending_reimbursement.adapter.PendingReimbursementAdapter
import com.example.digitracksdk.presentation.home.pending_reimbursement.model.PendingReimbursementModel
import com.example.digitracksdk.presentation.leaves.LeavesViewModel
import com.example.digitracksdk.presentation.leaves.apply_leave.LeaveTypes
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ViewAttendanceNewActivity : BaseActivity(), ViewAttendanceAdapter.ViewAttendanceClickManager,
    PendingReimbursementAdapter.PendingReimbursementClickManager,
    ValidationListener {
    lateinit var binding: ActivityViewAttendanceNewBinding
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val attendanceRegularizationViewModel: AttendanceRegularizationViewModel by viewModel()
    private val leavesViewModel: LeavesViewModel by viewModel()
    private val candidateViewModel: CandidateViewModel by viewModel()
    private var index: Int = 0

    //    lateinit var viewAttendanceAdapter: ViewAttendanceAdapter
    lateinit var pendingReimbursementAdapter: PendingReimbursementAdapter

    private var viewAttendanceListData: ArrayList<ListViewAttendanceModel> = ArrayList()
    private var pendingList: ArrayList<PendingReimbursementModel> = ArrayList()
    private var checkSelection: ArrayList<PendingReimbursementModel> = ArrayList()

    lateinit var preferenceUtils: PreferenceUtils
    var fromDate: String = ""
    var toDate: String = ""
    var innovId: String = ""
    var gnetAssociateId: String = ""
    var maxCount = 0
    var count = 0
    var tempList = ArrayList<PendingReimbursementModel>()

    var successResultMap = mutableMapOf<Int, String>()
    var selectedFromDate: Calendar? = null
    lateinit var bottomSheetViewAttendanceFilterBinding: BottomSheetViewAttendanceFilterBinding

    //For attendance Regularization
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetNewAttendanceRegularizationBinding: BottomSheetNewLeaveRequestBinding
    private var regularizationTypeList: ArrayList<ListAttnRegularizationTypeModel> = ArrayList()
    private var selectedRequestTypeId: Int = 0
    var selectedItemPosition: Int? = 0
    var isAttendanceRegularizationBottomSheet: Boolean = false

    //for apply leave
    lateinit var bottomSheetNewLeavesRequestBinding: BottomSheetNewLeaveRequestBinding
    var leavesTypeList: java.util.ArrayList<ListAttnRegularizationType> = java.util.ArrayList()
    var isApplyLeaveBottomSheet: Boolean = false

    var selectedLeaveType: Int = 0
    var cl: Double = 0.0
    var pl: Double = 0.0
    var sl: Double = 0.0
    var from = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewAttendanceNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this@ViewAttendanceNewActivity)
        attendanceRegularizationViewModel.validationListener = this@ViewAttendanceNewActivity
        leavesViewModel.validationListener = this@ViewAttendanceNewActivity

        setUpView()
        getPreferenceData()
        setUpObserver()
        setUpViewAttendanceAdapter()
        setUpListener()
        callViewAttendanceApi(fromDate, toDate)
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun setUpObserver() {
        binding.apply {
            with(candidateViewModel) {
                candidateAttendanceResponseData.observe(this@ViewAttendanceNewActivity) {
                    if (!it.Status.isNullOrEmpty()) when (it.Status!!.toInt()) {
                        1 -> {
                            successResultMap[successResultMap.size] = "Successful"
                            count += 1
                            checkIsHitAll()
                        }
                        2 -> {
                            successResultMap[successResultMap.size] = "Already Applied"
                            count += 1
                            checkIsHitAll()
                        }
                        else -> {
                            successResultMap[successResultMap.size] = "Applied Failed"
                            count += 1
                            checkIsHitAll()
                        }
                    }
                }
                progressBar.observe(this@ViewAttendanceNewActivity) {
                    toggleLoader(false)

                }
                messageData.observe(this@ViewAttendanceNewActivity) {
                    showToast(it)
                }
            }
            with(attendanceViewModel) {
                viewAttendanceResponseData.observe(this@ViewAttendanceNewActivity) {
                    toggleLoader(false)
                    tvCurrentMonthYear.text = "$fromDate - $toDate"
                    if (it.Status == Constant.SUCCESS) {
                        if (it.LSTAttendaceTimeAsheetDetails?.isNotEmpty() == true) {
                            layoutNoData.root.visibility = GONE
                            pendingList.clear()
//                            viewAttendanceListData.clear()
//                            viewAttendanceListData.addAll(
//                                it.LSTAttendaceTimeAsheetDetails
//                            )
                            it.LSTAttendaceTimeAsheetDetails?.let { it1 ->
                                convertModel(
                                    it1
                                )
                            }?.let { it2 -> pendingList.addAll(it2) }
                            pendingReimbursementAdapter.notifyDataSetChanged()

                        } else {
                            layoutNoData.root.visibility = VISIBLE
//                            viewAttendanceListData.clear()
                            pendingList.clear()
                        }
//                        viewAttendanceAdapter.notifyDataSetChanged()
//                        pendingReimbursementAdapter.notifyDataSetChanged()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@ViewAttendanceNewActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }

            with(attendanceViewModel) {
                attendanceValidationResponseData.observe(/* owner = */ this@ViewAttendanceNewActivity)
                /* observer = */ { it ->
                    toggleLoader(false)
                    if (it.Message == Constant.SUCCESS) {
                        callInsertAttendanceRegularizationApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@ViewAttendanceNewActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(attendanceRegularizationViewModel) {

                attendanceRegularizationTypeResponseData.observe(
                    this@ViewAttendanceNewActivity
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
                                this@ViewAttendanceNewActivity,
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

                messageData.observe(
                    this@ViewAttendanceNewActivity
                ) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }
            }

            with(leavesViewModel) {
                leavesBalanceResponse.observe(
                    this@ViewAttendanceNewActivity
                ) { it ->
                    cl = it.CL?.toDouble() ?: 0.0
                    pl = it.PL?.toDouble() ?: 0.0
                    sl = it.SL?.toDouble() ?: 0.0
                    val total = cl + pl + sl
                    if (isAttendanceRegularizationBottomSheet) {
                        bottomSheetNewAttendanceRegularizationBinding.apply {
                            tvBalanceLeaveValue.text =
                                total.toString()

                            tvBalanceLeave.visibility = VISIBLE
                            tvBalanceLeaveValue.visibility = VISIBLE
                        }
                    }

                }

                applyLeavesResponse.observe(
                    this@ViewAttendanceNewActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        bottomSheetDialog.dismiss()
                        selectedLeaveType = 0
                        showToast(it.Message.toString())
                        callViewAttendanceApi(fromDate, toDate)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                leavesTypeResponse.observe(this@ViewAttendanceNewActivity) {
                    toggleLoader(false)
                    if (!it.LstAttnRegularizationType.isNullOrEmpty()) {
                        leavesTypeList.clear()
                        leavesTypeList.addAll(it.LstAttnRegularizationType ?: arrayListOf())

                        val issueList: java.util.ArrayList<String> = java.util.ArrayList()
                        issueList.clear()
                        for (i in leavesTypeList) {
                            issueList.add(i.RegularizationType.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@ViewAttendanceNewActivity,
                            android.R.layout.simple_list_item_1, issueList
                        )
                        bottomSheetNewLeavesRequestBinding.etLeaveType.setAdapter(adapter)
                    } else {
                        showToast(it.Message.toString())
                    }

                }

                messageData.observe(
                    this@ViewAttendanceNewActivity
                ) { it ->
                    toggleLoader(false)
                    it?.let { it1 -> showToast(it1) }
                }
            }
        }
    }

    private fun convertModel(lstAttendaceTimeAsheetDetails: java.util.ArrayList<ListViewAttendanceModel>): Collection<PendingReimbursementModel> {

//                            data class ListViewAttendanceModel(
//                                var AttendanceDate:String? = "",
//                                var InsTime:String? = "",
//                                var OutTime:String? = "",
//                                var WorkHours:String? = "",
//                                var Shift:String? = "",
//                                var AttendanceStatus:String? = "",
//                                var Day:String? = "",
//                            )

//                            data class PendingReimbursementModel(
//                                var title:String? = "",
//                                var date:String? = "",
//                                var amount:String? = "",
//                                var isSelected:Boolean = false,
//                                var icon:Int? = null,
//                                var AR_AssociateReimbursementId:String? ="",
//                                var month:String? ="",
//                                var year:String? =""
//                            )
        val temp: ArrayList<PendingReimbursementModel> = ArrayList()
        for (i in lstAttendaceTimeAsheetDetails) {

            temp.add(
                PendingReimbursementModel(
                    title = i.AttendanceDate,
                    date = i.Day ?: "",
                    month = i.AttendanceStatus ?: "",
                    inTime = i.InsTime?.let { if (it.isNotEmpty()) it.substring(0, 8) else "" } ?: "",
                    outTime = i.OutTime?.let { if (it.isNotEmpty()) it.substring(0, 8) else "" } ?: "")
            )
        }
        return temp
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

    private fun setUpViewAttendanceAdapter() {
        binding.apply {
//            viewAttendanceAdapter = ViewAttendanceAdapter(
//                this@ViewAttendanceNewActivity,
//                viewAttendanceListData,
//                this@ViewAttendanceNewActivity
//            )
            pendingReimbursementAdapter = PendingReimbursementAdapter(
                this@ViewAttendanceNewActivity,
                pendingList,
                this@ViewAttendanceNewActivity,
                from = from
            )
//            recyclerViewAttendance.adapter = viewAttendanceAdapter
            recyclerViewAttendance.adapter = pendingReimbursementAdapter
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewAttendanceApi(fromDate, toDate)
            }
            btnSubmit.setOnClickListener {
                checkSelection = pendingList.filter {
                    it.isSelected
                } as ArrayList<PendingReimbursementModel>

                pendingReimbursementAdapter.let {
                    maxCount = 0
                    count = 0
                    //                    val f = it.list.partition { it.isSelected == true }
                    //                    maxCount = f.first.size
                    maxCount = checkSelection.size

                    if (maxCount != 0) {
                        tempList.clear()
                        tempList.addAll(checkSelection)
                        //                        tempList.addAll(f.first)
                        toggleLoader(true)

                        updateAttendance(tempList)
                    } else
                        showToast(getString(R.string.please_select_first))
                }

            }

        }
    }

    private fun updateAttendance(arrayList: ArrayList<PendingReimbursementModel>) {

        for (i in 0 until arrayList.size) {
            CoroutineScope(Dispatchers.IO).async {
//                callApiSubmitDAta(i, arrayList[i])
                candidateViewModel.callCandidateAttendanceApi(
                    getInsertAttendanceRegularizationRequestModel1(arrayList[i])
                )
            }
        }
    }

    private fun callApiSubmitDAta(i: Int, list: PendingReimbursementModel) {
        index = i
//        candidateViewModel.callCandidateAttendanceApi(
//            getInsertAttendanceRegularizationRequestModel1(list)
//        )
    }

    private fun getInsertAttendanceRegularizationRequestModel1(list: PendingReimbursementModel): InsertAttendanceRegularizationRequestModel {

        //emp=301901559
        //spineerValueTypeId=2
        //regulixationDate=2023-Jan-13
        //Intime=10:46:00
        //OutTime=20:46:00
        //locatiom=null
        //remark=test
        //toDate=2023-Jan-13

        val request = InsertAttendanceRegularizationRequestModel()

        val regularizationDate = AppUtils.INSTANCE?.convertDateFormat(
            dateFormatToRead = "dd MMM yyyy",
            dateToRead = list.title.toString().trim(), dateFormatToConvert = "yyyy-MMM-dd"
        )
        //2023-Jan-13

        request.apply {
            Empcode = gnetAssociateId
            RequestTypeId = 2
            RegularizationDate = regularizationDate ?: ""
            toDate = regularizationDate ?: ""
            InTime = list.inTime?.let { if (it.isNotEmpty()) it.substring(0, 8) else "00:00:00" } ?: "00:00:00"
            OutTime = list.outTime?.let { if (it.isNotEmpty()) it.substring(0, 8) else "00:00:00" } ?: "00:00:00"
            Location = "null"
            Remarks = "for date $regularizationDate"
        }
        return request
    }


    @SuppressLint("SuspiciousIndentation")
    private fun createDialog() {
        val dialog = Dialog(this@ViewAttendanceNewActivity)
            dialog.apply {
                setContentView(R.layout.dialog_view_attendence)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
                val dismiss = dialog.findViewById<TextView>(R.id.btnSubmit)

                val adap = ViewAttendanceDialogAdapter(
                    getDialogData()
                )
                recyclerView.adapter = adap
                dismiss.setOnClickListener {
                    pendingList.forEach {
                        it.isSelected=false
                    }
                    successResultMap.clear()
                    pendingReimbursementAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                show()
            }
    }

    private fun getDialogData(): ArrayList<DialogModel> {
        val list = ArrayList<DialogModel>()
        list.clear()
        for (i in 0 until count) {
//            list.add("Attendance on ${tempList[i].month} is ${successResultMap[i]}")
            list.add(DialogModel(title="Attendance is ${successResultMap[i]} on", subTitle = tempList[i].title ?: ""))
        }
        return list
    }


    private fun checkIsHitAll() {
        if (count == maxCount) {
            toggleLoader(false)
            createDialog()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun callViewAttendanceApi(fromDate: String, toData: String) {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                tvCurrentMonthYear.visibility = VISIBLE
                recyclerViewAttendance.visibility = VISIBLE
                btnSubmit.visibility = VISIBLE
                attendanceViewModel.callViewAttendanceApi(
                    request =
                    ViewAttendanceRequestModel(
                        AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                        FromDate = fromDate,
                        Todate = toData,
                        MappingID = ""
                    )
                )
            } else {
                tvCurrentMonthYear.visibility = GONE
                layoutNoInternet.root.visibility = VISIBLE
                recyclerViewAttendance.visibility = GONE
                layoutNoData.root.visibility = GONE
                btnSubmit.visibility = GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpView() {
        binding.apply {
            toolbar.apply {
                btnBack.setOnClickListener {
                    finish()
                }
                tvTitle.text = getString(R.string.view_attendance_new)
                divider.visibility = VISIBLE
                btnOne.setImageResource(R.drawable.ic_filter)
                btnOne.visibility = VISIBLE
                btnOne.setOnClickListener {
                    openFilterBottomSheet()
                }
            }
            toDate = AppUtils.INSTANCE?.getCurrentDate().toString()
            fromDate = "01" + toDate.subSequence(2, 11)
//            val currentMonth = AppUtils.INSTANCE?.getCurrentDate().toString().subSequence(3,6)
            tvCurrentMonthYear.text = "$fromDate - $toDate"
        }
    }

    private fun openFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_view_attendance_filter, null)

        bottomSheetViewAttendanceFilterBinding =
            BottomSheetViewAttendanceFilterBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetViewAttendanceFilterBinding.apply {
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnApply.setOnClickListener {
                clearError()
                fromDate = etFromDate.text.toString().trim()
                toDate = etToDate.text.toString().trim()
                if (fromDate.isEmpty()) {
                    layoutFromDate.error = getString(R.string.please_choose_from_date)
                } else if (toDate.isEmpty()) {
                    layoutToDate.error = getString(R.string.please_choose_to_date)
                } else {
                    callViewAttendanceApi(fromDate, toDate)
                    bottomSheetDialog.dismiss()
                }
            }
            etFromDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@ViewAttendanceNewActivity,
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
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
            etToDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    this@ViewAttendanceNewActivity,
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
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                } else {
                    datePickerDialog.datePicker.minDate = selectedFromDate?.time?.time!!
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                }
                datePickerDialog.show()
            }
        }
        bottomSheetDialog.show()
    }

    fun clearError() {
        bottomSheetViewAttendanceFilterBinding.apply {
            layoutFromDate.error = ""
            layoutToDate.error = ""
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

    private fun getInsertAttendanceRegularizationRequestModel(): InsertAttendanceRegularizationRequestModel {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            val request = InsertAttendanceRegularizationRequestModel()
            request.Empcode = gnetAssociateId
            request.InTime =
                if (etInTime.text.toString().trim()
                        .isEmpty()
                ) "00:00" else etInTime.text.toString()
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

    private fun hideViews() {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            layoutLocation.visibility = GONE
            layoutInTime.visibility = GONE
            layoutOutTime.visibility = GONE
            tvBalanceLeave.visibility = View.INVISIBLE
            tvBalanceLeaveValue.visibility = View.INVISIBLE
        }
    }

    private fun clearData() {
        bottomSheetNewAttendanceRegularizationBinding.apply {
            etToDate.setText("")
            etLocation.setText("")
            etReason.setText("")
        }
    }

    private fun openAttendanceRegularizationBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_new_leave_request, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetNewAttendanceRegularizationBinding =
            BottomSheetNewLeaveRequestBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        callAttendanceRegularizationTypeApi()

        bottomSheetNewAttendanceRegularizationBinding.apply {
            val data = viewAttendanceListData[selectedItemPosition ?: 0]
            tvNewLeaveRequest.text = getString(R.string.attendance_regularization)
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
            etFromDate.setText(data.AttendanceDate)
            etFromDate.isEnabled = false
            if (data.InsTime?.isNotEmpty() == true) {
                etInTime.setText(data.InsTime?.substring(0, 5))
            }
            if (data.OutTime?.isNotEmpty() == true) {
                etOutTime.setText(data.OutTime?.substring(0, 5))
            }

            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnApply.setOnClickListener {
                clearAttendanceReguError()
                isAttendanceRegularizationBottomSheet = true
                isApplyLeaveBottomSheet = false
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
                    clearAttendanceReguError()
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
                AppUtils.INSTANCE?.openTimePicker(etInTime, this@ViewAttendanceNewActivity)
            }
            etOutTime.setOnClickListener {
                AppUtils.INSTANCE?.openTimePicker(etOutTime, this@ViewAttendanceNewActivity)
            }
        }
    }

    private fun clearAttendanceReguError() {
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
            leavesViewModel.callLeaveBalanceApi(getBalanceLeavesRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onClickAttendanceRegularization(position: Int) {
        selectedItemPosition = position
        openAttendanceRegularizationBottomSheet()
    }

    override fun onClickApplyLeave(position: Int) {
        selectedItemPosition = position
        openNewLeaveRequestBottomSheet()
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        if (isAttendanceRegularizationBottomSheet) {
            var regularizationType =
                bottomSheetNewAttendanceRegularizationBinding.etLeaveType.text.toString().trim()

            if (regularizationType == "Attendance Regularization") {
                regularizationType = "AR"
            } else if (regularizationType == "Leave") {
                regularizationType = "LR"
            }
            callAttendanceValidationApi(regularizationType)
        } else if (isApplyLeaveBottomSheet) {
            callApplyLeaveApi()
        }
    }

    private fun callApplyLeaveApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            leavesViewModel.callApplyLeaveApi(getApplyLeaveRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
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

    private fun setupLeaveDropdown() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
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

    private fun openNewLeaveRequestBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_new_leave_request, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetNewLeavesRequestBinding = BottomSheetNewLeaveRequestBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        callBalanceLeavesApi()
        setupLeaveDropdown()
        setupSubLeaveDropdown()
        bottomSheetNewLeavesRequestBinding.apply {
            val data = viewAttendanceListData[selectedItemPosition ?: 0]
            etFromDate.isEnabled = false
            etToDate.isEnabled = false
            etFromDate.setText(data.AttendanceDate)
            etToDate.setText(data.AttendanceDate)
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnApply.setOnClickListener {
                clearLeaveError()
                isApplyLeaveBottomSheet = true
                isAttendanceRegularizationBottomSheet = false
                leavesViewModel.validateApplyLeaveRequest(
                    getApplyLeaveRequestModel(),
                    this@ViewAttendanceNewActivity
                )
            }
            etLeaveType.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    selectedLeaveType = leavesTypeList[position].RegularizationTypeID ?: 0
                    if (selectedLeaveType == LeaveTypes.Pl.value) {
                        tvBalanceLeaveValue.text = pl.toString()
                    } else if (selectedLeaveType == 12) {
                        tvBalanceLeaveValue.text = sl.toString()
                    } else if (selectedLeaveType == LeaveTypes.ClSlCombined.value) {
                        val cl = cl
                        val sl = sl
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
                            etToDate.setText(data.AttendanceDate)
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
                            etToDate.setText(data.AttendanceDate)
                        }
                    } else {
                        bottomSheetNewLeavesRequestBinding.layoutToDate.visibility = VISIBLE
                    }
                }
        }
    }

    private fun clearLeaveError() {
        bottomSheetNewLeavesRequestBinding.apply {
            layoutLeaveType.error = ""
            layoutSubLeaveType.error = ""
            layoutFromDate.error = ""
            layoutToDate.error = ""
            layoutReason.error = ""
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        if (isAttendanceRegularizationBottomSheet) {
            bottomSheetNewAttendanceRegularizationBinding.apply {
                when (type) {
                    Constant.ListenerConstants.REQUEST_TYPE_ERROR -> {
                        layoutLeaveType.error = getString(msg)
                    }
                    Constant.ListenerConstants.FROM_DATE_ERROR -> {
                        layoutFromDate.error = getString(msg)
                    }
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
        } else {
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
    }

    override fun clickOnItem(position: Int) {
        pendingList[position].isSelected = !pendingList[position].isSelected
        pendingReimbursementAdapter.notifyItemChanged(position)
    }

}