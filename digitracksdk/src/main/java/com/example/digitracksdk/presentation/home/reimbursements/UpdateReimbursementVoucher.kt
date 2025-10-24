package com.example.digitracksdk.presentation.home.reimbursements

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.example.digitracksdk.domain.model.reimbursement_model.AssociateReimbursementDetailListModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1RequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.SaveReimbursementPreApprovalRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsRequestModel
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.example.digitracksdk.Constant
import com.example.digitracksdk.Constant.Companion.decFormat
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAddReimbursementBinding
import com.example.digitracksdk.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DecimalDigitsInputFilter
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class UpdateReimbursementVoucher:BaseActivity(), ValidationListener, DialogUtils.DialogManager {

    //TODO remove warnings
    lateinit var binding: ActivityAddReimbursementBinding
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private var flagPocketExpense: Boolean = false
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    lateinit var approvalDialog: AlertDialog.Builder
    var isNormalValidationFlow:Boolean = false

    var empId: String = ""
    var gnetAssociateId: String = ""
    var innovId: String = ""
    private var imagePath: String = ""
    private var selectedClaimDate: Calendar? = null
    private var selectedBillDate: Calendar? = null
    var selectedBillFromDate: Calendar? = null
    var isReimbursementPreApproval: Boolean = false


    var allocationType: String = ""
    var approvalDate: String = ""
    var approverName: String = ""
    var approvedAdditionalAmt: String = ""

    private var reimbursementCategoryList: ArrayList<ReimbursementCategoryDetailsModel> =
        ArrayList()
    private var reimbursementSubCategoryList: ArrayList<ReimbursementSubCategoryDetailsModel> =
        ArrayList()
//    private var reimbursementDetailsList: ArrayList<AssociateReimbursementDetailListModel> =
//        ArrayList()
    private var travelModeList: ArrayList<ModeOfTravelDetailsModel> = ArrayList()
    private var selectedCategoryId: Int = 0
    var selectedSubCategoryId: Int = 0
    var selectedTravelModeId: Int = 0
    var localEndKm = 0
    var startKmApi = 0
    var endKmApi = 0

    var isTaxApplicable:Boolean = false


    private var updateVoucherData: ReimbursementDetailsModel = ReimbursementDetailsModel()
    var isFromEditBill: Boolean = false
    var associateReimbursementId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddReimbursementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        reimbursementViewModel.validationListener = this
        getPreferencesData()
        setUpView()
        setObserver()
        getIntentData()
        setUpToolbar()
        setUpDecimalLimit()
        callReimbursementCategoryApi()
        setUpListener()
    }

    private fun setUpDecimalLimit() {
        binding.apply {
            etBaseAmt.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(9, 2))
            etTaxAmt.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(6, 2))
//            etGrossAmt.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2))
        }
    }

    private fun setUpListener() {
        binding.apply {

            toolbar.btnBack.setOnClickListener {
                finish()
            }

            etTaxAmt.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty() && text.toString() != ".") {
                    val taxAmt: Double = text.toString().trim().toDoubleOrNull()?:0.00
                    val baseAmt: Double = if (!etBaseAmt.text.isNullOrEmpty() && etBaseAmt.text.toString() != ".") {
                        etBaseAmt.text.toString().trim().toDoubleOrNull()?:0.00
                    } else {
                        0.00
                    }
                    val total = taxAmt.plus(baseAmt)
                    if (total.toString() == ".00"){
                        etGrossAmt.setText("0.00")
                    }else{
                        etGrossAmt.setText(decFormat.format(total))
                    }
                } else {
                    etGrossAmt.setText(decFormat.format(etBaseAmt.text.toString().toDoubleOrNull()?:0.00))
                }
            }
            etBaseAmt.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty() && text.toString() != ".") {
                    etTaxAmt.setText("0")
                    val a = decFormat.format(text.toString().toDoubleOrNull()?:0.00)
                    if (text.toString() == "0"){
                        etGrossAmt.setText("0.00")
                    }else{
                        etGrossAmt.setText(a)

                    }
                } else {
                    etGrossAmt.setText("")
                }
            }

            etRunKm.doOnTextChanged { text, start, before, count ->
                val dailyMileageLimitAmt =
                    preferenceUtils.getValue(Constant.PreferenceKeys.DailyMileageLimit)
                        .toIntOrNull() ?: 0
                val dailyMileageLimitFourWheelerAmt =
                    preferenceUtils.getValue(Constant.PreferenceKeys.DailyMileageFourWheelar)
                        .toIntOrNull() ?: 0
                if (selectedCategoryId == ReimbursementCategory.TRAVELLING_SELF.value || selectedCategoryId == ReimbursementCategory.DAILY_ALLOWANCES.value) {
                    when (selectedTravelModeId) {
                        0 -> {
                            etBaseAmt.setText("0")
                        }
                        25 -> {
                            // 25 for fourWheeler
                            val runKm = etRunKm.text.toString().toIntOrNull() ?: 0
                            val amt = runKm * dailyMileageLimitFourWheelerAmt
                            etBaseAmt.setText(amt.toString())
                        }
                        else -> {
                            val runKm = etRunKm.text.toString().toIntOrNull() ?: 0
                            val amt = runKm * dailyMileageLimitAmt
                            etBaseAmt.setText(amt.toString())
                        }
                    }
                }
            }

            etEndKm.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty()) {
                    val endKm: Int = text.toString().trim().toInt()
                    val startKm: Int = if (!etStartKm.text.isNullOrEmpty()) {
                        etStartKm.text.toString().trim().toInt()
                    } else {
                        0
                    }
                    val total = endKm.minus(startKm)
                    etRunKm.setText(total.toString())
                } else {
                    etRunKm.setText(etStartKm.text.toString())
                }
            }

            etStartKm.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty()) {

                    if (!etEndKm.text.isNullOrEmpty()) {
                        val endKm: Int = etEndKm.text.toString().trim().toInt()
                        val startKm = etStartKm.text.toString().trim().toInt()
                        val total = endKm.minus(startKm)
                        etRunKm.setText(total.toString())
                    } else {
                        //                    etEndKm.setText("")
                        etRunKm.setText(text)
                    }
                } else {
                    etRunKm.setText("")
                }
            }

            btnSubmit.setOnClickListener {
                clearError()
                isNormalValidationFlow = true
                reimbursementViewModel.validateApplyReimbursement(
                    getReimbursementDetailsRequestModel(),localEndKm = localEndKm,isFromRejected = true,startKmApi = startKmApi,endKmApi = endKmApi
                )
            }

            tvAddReimbursement.setOnClickListener {
                layoutReimbursementList.visibility = View.GONE
                layoutAddReimbursement.visibility = View.VISIBLE
                btnSubmit.text = getString(R.string.add)
            }

            etExpenseType.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, long ->
                    selectedCategoryId =
                        reimbursementCategoryList[position].ReimbursementCategoryId ?: 0
                    hideViews()
                    clearData()
                    clearError()
                    setUpDropDownItemSelectedData(selectedCategoryId)
                }

            etExpenseType.setOnClickListener {
                callReimbursementCategoryApi()
            }

            etExpenseSubType.setOnClickListener {
                callReimbursementSubCategoryApi()
            }

            etTravelMode.setOnClickListener {
                callGetModeOfTravelApi()
            }

            etExpenseSubType.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, p3 ->
                    selectedSubCategoryId =
                        reimbursementSubCategoryList[position].ReimbursementSubCategoryId ?: 0
                    if (selectedCategoryId == 9) {
                        flagPocketExpense = selectedSubCategoryId == 13 //if 13 then true else false
                    }
                }
            etTravelMode.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                    selectedTravelModeId = travelModeList[position].ModeOFTravelId ?: 0
                    val dailyMileageLimitAmt =
                        preferenceUtils.getValue(Constant.PreferenceKeys.DailyMileageLimit)
                            .toIntOrNull() ?: 0
                    val dailyMileageLimitFourWheelerAmt =
                        preferenceUtils.getValue(Constant.PreferenceKeys.DailyMileageFourWheelar)
                            .toIntOrNull() ?: 0
                    if (selectedCategoryId == ReimbursementCategory.TRAVELLING_SELF.value || selectedCategoryId == ReimbursementCategory.DAILY_ALLOWANCES.value) {
                        when (selectedTravelModeId) {
                            0 -> {
                                etBaseAmt.setText("0")
                            }
                            25 -> {
                                // 25 for fourWheeler
                                val runKm = etRunKm.text.toString().toIntOrNull() ?: 0
                                val amt = runKm * dailyMileageLimitFourWheelerAmt
                                etBaseAmt.setText(amt.toString())
                            }
                            else -> {
                                val runKm = etRunKm.text.toString().toIntOrNull() ?: 0
                                val amt = runKm * dailyMileageLimitAmt
                                etBaseAmt.setText(amt.toString())
                            }
                        }
                    }
                }

            etClaimDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@UpdateReimbursementVoucher,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedClaimDate = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd MMM yyyy"
                            )
                        etClaimDate.setText(selectedDate)
                        etBillDate.setText("")
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }

            etBillDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    this@UpdateReimbursementVoucher,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedBillDate = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd MMM yyyy"
                            )
                        etBillDate.setText(selectedDate)
                        isNormalValidationFlow = false
                        callAttendanceValidationApi()
                    },
                    y,
                    m,
                    d
                )
                if (selectedClaimDate == null) {
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                } else {
                    datePickerDialog.datePicker.maxDate = selectedClaimDate?.time?.time!!
                }
                datePickerDialog.show()
            }

            etBillFrom.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@UpdateReimbursementVoucher,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedBillFromDate = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd MMM yyyy"
                            )
                        etBillFrom.setText(selectedDate)
                        etBillTo.setText("")
                    },
                    y,
                    m,
                    d
                )
                if (selectedBillDate == null) {
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                } else {
                    datePickerDialog.datePicker.maxDate = selectedBillDate?.time?.time!!
                }
                datePickerDialog.show()
            }

            etBillTo.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    this@UpdateReimbursementVoucher,
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
                        etBillTo.setText(selectedDate)
                    },
                    y,
                    m,
                    d
                )
                if (selectedBillFromDate == null) {
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                } else {
                    datePickerDialog.datePicker.minDate = selectedBillFromDate?.time?.time!!
                    datePickerDialog.datePicker.maxDate = selectedBillDate?.time?.time!!
                }
                datePickerDialog.show()
            }
        }
    }

    private fun getReimbursementDetailsRequestModel(): AssociateReimbursementDetailListModel {
        binding.apply {
            val request = AssociateReimbursementDetailListModel()
            request.dailyMileageLimit = etRunKm.text.toString()
            request.Amount = decFormat.format(etBaseAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.BillDate = etBillDate.text.toString().trim()
            request.BillNo = etBillNo.text.toString().trim()
            request.ClaimDate = etClaimDate.text.toString().trim()
            request.Extn = etUploadVoucher.text.toString().split(".").last()
            request.FilePath = imagePath
            request.FromDate = etBillFrom.text.toString().trim()
            request.ToDate = etBillTo.text.toString().trim()
            request.FromLocation = etJourneyFrom.text.toString().trim()
            request.GrossAmount = decFormat.format(etGrossAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.ModeOfTravelId = selectedTravelModeId
            request.NameOfPlace = ""
            request.ReimbursementCategoryId = selectedCategoryId
            request.reimbursementCategoryName = etExpenseType.text.toString().trim()
            request.ReimbursementSubCategoryId = selectedSubCategoryId
            request.reimbursementSubCategoryName = etExpenseSubType.text.toString().trim()
            request.Remark = etRemark.text.toString().trim()
            request.setModeOfTravelName = etTravelMode.text.toString().trim()
            request.TaxAmount = decFormat.format(etTaxAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.ToLocation = etJourneyTo.text.toString().trim()
            request.StartKm = etStartKm.text.toString().trim()
            request.EndKm = etEndKm.text.toString().trim()
            return request
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.update_expense_voucher)
            divider.visibility = View.VISIBLE
            tvSave.visibility = View.GONE
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            updateVoucherData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(
                    Constant.DATA,
                    ReimbursementDetailsModel::class.java) as ReimbursementDetailsModel
            }else
                getSerializable(Constant.DATA) as ReimbursementDetailsModel

            associateReimbursementId = getString(Constant.associateReimbursementId).toString()
        }
    }

    private fun setUpView() {
        binding.apply {
            btnSubmit.text = getString(R.string.update)
            etExpenseType.isEnabled = false
            layoutExpenseType.isEnabled = false
        }
    }

    private fun setObserver() {
        binding.apply {
            with(attendanceViewModel) {
                attendanceValidationResponseData.observe(this@UpdateReimbursementVoucher
                ) {
                    toggleLoader(false)
                    if (it.Message != Constant.SUCCESS) {
                        showToast(it.Message.toString())
                    } else {
                        if (isNormalValidationFlow) {
                            if (selectedCategoryId == ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value) {
                                callCheckNightHaltLimitApi()
                            } else {
                                if (selectedCategoryId != ReimbursementCategory.BASF_OTHERS.value) {
                                    callReimbursementLimitApi()
                                } else {
//                                    callReimbursementValidateApi()
                                    callRejectedReimbursementValidationApi()
                                }
                            }
                        }
                    }
                }

                messageData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
            with(reimbursementViewModel){

                reimbursementSubCategoryResponseData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    if (it.ReimbursementSubCategoryDetails?.size != 0) {
                        reimbursementSubCategoryList.clear()
                        reimbursementSubCategoryList.addAll(
                            it.ReimbursementSubCategoryDetails ?: arrayListOf()
                        )
                        val subCategoryList: ArrayList<String> = ArrayList()
                        subCategoryList.clear()
                        for (i in reimbursementSubCategoryList) {
                            subCategoryList.add(i.ReimbursementSubCategory.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@UpdateReimbursementVoucher,
                            android.R.layout.simple_dropdown_item_1line,
                            subCategoryList
                        )
                        binding.etExpenseSubType.apply {
                            setText(subCategoryList[0])
                            setAdapter(adapter)
                        }
                    } else {
                        showToast(it.toString())
                    }
                }

//                reimbursementEndKmResponseData.observe(this@UpdateReimbursementVoucher, {
//                    toggleLoader(false)
//                    if (localEndKm < it.EndKM?.toIntOrNull() ?: 0) {
//                        localEndKm = it.EndKM?.toIntOrNull() ?: 0
//                    }
//                    if (updateVoucherData.EndKM.toString().isEmpty() || updateVoucherData.EndKM == "0"){
//                        localEndKm = it.EndKM.toString().toIntOrNull()?:0
//                        etRunKm.setText(it.EndKM.toString())
//                        etStartKm.setText(it.EndKM.toString())
//                    }else{
//                        etRunKm.setText(updateVoucherData.StartKM)
//                        etStartKm.setText(updateVoucherData.EndKM)
//                    }
//
//                })
                rejectedEndKmResponseData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        localEndKm = 0
                        startKmApi = it.StartReading ?: 0
                        endKmApi = it.EndReading ?: 0
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                reimbursementCategoryResponseData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    if (it.ReimbursementCategoryDetails?.size != 0) {
                        reimbursementCategoryList.clear()
                        reimbursementCategoryList.addAll(
                            it.ReimbursementCategoryDetails ?: arrayListOf()
                        )
                        val categoryList: ArrayList<String> = ArrayList()
                        categoryList.clear()
                        for (i in reimbursementCategoryList) {
                            categoryList.add(i.ReimbursementCategory.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@UpdateReimbursementVoucher,
                            android.R.layout.simple_dropdown_item_1line,
                            categoryList
                        )
                        val categoryData = reimbursementCategoryList.find { data ->
                            data.ReimbursementCategory == updateVoucherData.ReimbursementCategory
                        }
                        if (categoryData != null) {
                            selectedCategoryId = categoryData.ReimbursementCategoryId ?: 0
                            isTaxApplicable = categoryData.IsTaxApplicable == "Y"
                            hideViews()
                            clearData()
                            setUpDropDownItemSelectedData(selectedCategoryId)
                            setUpUpdateVoucherUI()
                        }
                        etExpenseType.setAdapter(adapter)

                    } else {
                        showToast(it.toString())
                    }
                }

                reimbursementModeOfTravelResponseData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    if (it.ModeOfTravelDetails?.size != 0) {
                        travelModeList.clear()
                        travelModeList.addAll(it.ModeOfTravelDetails ?: arrayListOf())
                        val list: ArrayList<String> = ArrayList()
                        list.clear()
                        for (i in travelModeList) {
                            list.add(i.ModeOfTravle.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@UpdateReimbursementVoucher,
                            android.R.layout.simple_dropdown_item_1line,
                            list
                        )
                        etTravelMode.setAdapter(adapter)
                        val selectModeList = travelModeList.find { data ->
                            data.ModeOfTravle == updateVoucherData.ModeOfTravel
                        }
                        if (selectModeList != null) {
                            selectedTravelModeId =
                                if (etTravelMode.text.toString() == getString(R.string.Select)) {
                                    0
                                } else {
                                    selectModeList.ModeOFTravelId ?: 0
                                }
                        }
                    } else {
                        showToast(it.toString())
                    }
                }

                checkReimbursementLimitResponseData.observe(this@UpdateReimbursementVoucher) {
                    var limit = 0.0
                    if ((it.Limit ?: 0.0) < (etGrossAmt.text.toString().toDoubleOrNull() ?: 0.0)) {
                        limit = if ((it.Limit ?: 0.0) > 0) {
                            etGrossAmt.text.toString().toDouble() - it.Limit.toString().toDouble()
                        } else {
                            etGrossAmt.text.toString().toDoubleOrNull() ?: 0.0
                        }
                    }
                    if (it.Message == Constant.alert) {
                        allocationType = it.AllocationType.toString()
                        openApprovalDialogue(limit.toString())
                    } else {
                        callRejectedReimbursementValidationApi()
//                        callReimbursementValidateApi()

                        //TODO confirm and handle it
//                        if (it.Limit != 0){
//
//                            if (reimbursementDetailsList.size >= 1){
//                                val limitList = reimbursementDetailsList.filter { data->
//                                    data.ReimbursementCategoryId == selectedCategoryId
//                                }
//                                var grossAmt = 0
//                                var totalAmt = 0
//                                for (i in limitList?: arrayListOf()){
//                                    grossAmt = grossAmt + (if(i.GrossAmount.isNullOrEmpty()) 0 else i.GrossAmount?.toInt())!!
//                                    totalAmt = grossAmt + (if (etGrossAmt.text.toString().isEmpty()) 0 else etGrossAmt.text.toString().toIntOrNull())!!
//                                }
//                                if (it.Limit?:0 < totalAmt){
//                                    limit = totalAmt.toString().toDouble() - it.Limit.toString().toDouble()
//                                    openApprovalDialogue(limit.toString())
//                                }
//
//                            }else{
//                                callReimbursementValidateApi()
//                            }
//                        }else{
//                            callReimbursementValidateApi()
//                        }
                    }
                }

                checkReimbursementValidationV1ResponseData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    var limit = 0.0
                    if ((it.Limit ?: 0) < (etGrossAmt.text.toString().toDoubleOrNull() ?: 0.0)) {
                        limit = if ((it.Limit ?: 0) > 0) {
                            etGrossAmt.text.toString().toDouble() - it.Limit.toString().toDouble()
                        } else {
                            etGrossAmt.text.toString().toDoubleOrNull() ?: 0.0
                        }
                    }
                    if (it.Message == Constant.alert) {
                        allocationType = it.AllocationType.toString()
                        openApprovalDialogue(limit.toString())
                    } else {
                        callRejectedReimbursementValidationApi()
                    }
                }

                saveReimbursementPreApprovalResponseData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        DialogUtils.closeReimbursementPreApprovalDialog()
                        callRejectedReimbursementValidationApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                rejectedReimbursementValidationResponseData.observe(this@UpdateReimbursementVoucher
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        callUpdateVoucherBillApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                updateReimbursementDetailsResponseData.observe(this@UpdateReimbursementVoucher
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        val i = Intent()
                        setResult(Activity.RESULT_OK, i)
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@UpdateReimbursementVoucher) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun openApprovalDialogue(approvedAmt: String) {
        approvalDialog = AlertDialog.Builder(this)
        approvalDialog.setMessage(getString(R.string.exceptional_approval))
        approvalDialog.setCancelable(true)
        approvalDialog.setPositiveButton(
            getString(R.string.yes)
        ) { dailog, p1 ->
            dailog?.cancel()
            openPreApprovalDialogue(approvedAmt)
        }
        approvalDialog.setNegativeButton(
            getString(R.string.no)
        ) { dailog, p1 ->
            dailog?.cancel()
        }
        approvalDialog.show()
    }

    private fun openPreApprovalDialogue(approvedAmt: String) {
        DialogUtils.showReimbursementPreApprovalDialog(this, this, approvedAmt)
    }

    override fun onChooseDateClick(date: TextInputEditText) {
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
                val selectedDate =
                    AppUtils.INSTANCE?.convertDateToString(
                        selectedCalendar.time,
                        "dd MMM yyyy"
                    )
                date.setText(selectedDate)
            },
            y,
            m,
            d
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onAttachmentClick(date: String, name: String, amt: String) {
        isReimbursementPreApproval = true
        approvalDate = date
        approverName = name
        approvedAdditionalAmt = amt
        openAddPhotoBottomSheet()
    }
    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val fileName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME)
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitMap: Bitmap? = BitmapFactory.decodeFile(url.toString())

                imagePath =
                    bitMap?.let { it1 -> ImageUtils.INSTANCE?.bitMapToString(it1).toString() }
                        .toString()

                callReimbursementPreApprovalApi(
                    approvedAdditionalAmt,
                    approverName,
                    approvalDate,
                    fileName.toString().split(".").last()
                )
            }
        }

    private fun callReimbursementPreApprovalApi(
        approvedAmt: String,
        approvedBy: String,
        approvedDate: String,
        extn: String
    ) {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callSaveReimbursementPreApprovalApi(
                request =
                SaveReimbursementPreApprovalRequestModel(
                    ApplicableForPeriod = allocationType,
                    ApprovedAmount = decFormat.format(approvedAmt.toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00,
                    ApprovedBy = approvedBy,
                    ApprovedDate = approvedDate,
                    Extn = extn,
                    FilePath = imagePath,
                    GnetAssociateId = gnetAssociateId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
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

    private fun callReimbursementLimitApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                reimbursementViewModel.callCheckReimbursementLimitApi(
                    request =
                    CheckReimbursementLimitRequestModel(
                        Amount = decFormat.format(etGrossAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00,
                        Date = etBillDate.text.toString().replace(" ", "-"),
                        ExpenseType = selectedCategoryId,
                        GNETAssociateId = gnetAssociateId
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }
    private fun getReimbursementValidateRequestModel(): ReimbursementValidationRequestModel {
        binding.apply {
            val request = ReimbursementValidationRequestModel()
            request.Amount = decFormat.format(etBaseAmt.text.toString().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.BillDate = etBillDate.text.toString().replace(" ", "-")
            request.BillNo = etBillNo.text.toString()
            request.GNETAssociateID = gnetAssociateId
            request.GrossAmount = decFormat.format(etGrossAmt.text.toString().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.ExpenseTypeId = selectedCategoryId.toString()
            return request
        }
    }

//    private fun callReimbursementValidateApi() {
//        binding.apply {
//            if (isNetworkAvailable()) {
//                toggleLoader(true)
//                reimbursementViewModel.callReimbursementValidationApi(
//                    getReimbursementValidateRequestModel()
//                )
//            } else {
//                showToast(getString(R.string.no_internet_connection))
//            }
//        }
//    }

    private fun getUpdateVoucherRequestModel(): UpdateReimbursementDetailsRequestModel {
        binding.apply {
            val request = UpdateReimbursementDetailsRequestModel()
            request.AssociateId =
                preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID).toIntOrNull()?:0
            request.AssociateReimbursementDetailId =
                updateVoucherData.AssociateReimbursementDetailId?.toIntOrNull() ?: 0
            request.AssociateReimbursementId = associateReimbursementId.toIntOrNull() ?: 0
            request.BillDate = etBillDate.text.toString().trim()
            request.BaseAmount = 0.00
            request.TaxAmount = if (etTaxAmt.text.toString().trim().isEmpty()) 0.00 else decFormat.format(etTaxAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.GrossAmount = if (etGrossAmt.text.toString().trim().isEmpty()) 0.00 else decFormat.format(etGrossAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.BillNo = etBillNo.text.toString().trim()
            request.ClaimDate = etClaimDate.text.toString().trim()
            request.EndKM = if (etEndKm.text.toString().trim().isEmpty()) 0 else etEndKm.text.toString().trim().toInt()
            request.FromDate = etBillFrom.text.toString().trim()
            request.FromLocation = etJourneyFrom.text.toString().trim()
            request.GNETAssociateId = updateVoucherData.GNETAssociateId.toString()
            request.ModeOfTravelId = selectedTravelModeId
            request.NameOfPlace = ""
            request.ReimbursementCategoryId = selectedCategoryId
            request.ReimbursementSubCategoryId = selectedSubCategoryId
            request.Remark = etRemark.text.toString().trim()
            request.StartKM = if (etStartKm.text.toString().trim().isEmpty()) 0 else etStartKm.text.toString().trim().toInt()
            request.ToDate = etBillTo.text.toString().trim()
            request.ToLocation = etJourneyTo.text.toString().trim()
            request.UpdatedBy = ""
            request.Amount = if (etBaseAmt.text.toString().trim().isEmpty()) 0.00 else decFormat.format(etBaseAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
            request.categoryName = etExpenseType.text.toString().trim()
            return request
        }
    }

    private fun callUpdateVoucherBillApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callUpdateReimbursementDetailsApi(request = getUpdateVoucherRequestModel())
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callCheckNightHaltLimitApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                reimbursementViewModel.callCheckReimbursementValidationV1Api(
                    request =
                    CheckReimbursementLimitV1RequestModel(
                        Amount = decFormat.format(etGrossAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00,
                        Date = etBillDate.text.toString().trim(),
                        ExpenseType = selectedCategoryId,
                        GNETAssociateId = gnetAssociateId,
                        ToDate = etBillTo.text.toString().trim(),
                        FromDate = etBillFrom.text.toString().trim()
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callAttendanceValidationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                attendanceViewModel.callAttendanceValidationApi(
                    request = AttendanceValidationRequestModel(
                        FromDate = etBillDate.text.toString(),
                        GNETAssociateID = gnetAssociateId,
                        InnovID = innovId,
                        ToDate = etBillDate.text.toString(),
                        Type = "RI"
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callReimbursementCategoryApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementCategoryApi(
                request = ReimbursementCategoryRequestModel(
                    EmployeeID = gnetAssociateId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpUpdateVoucherUI() {
        binding.apply {
            callGetModeOfTravelApi()
            btnSubmit.text = getString(R.string.update)
            etExpenseType.setText(updateVoucherData.ReimbursementCategory)
            etExpenseSubType.setText(updateVoucherData.ReimbursementSubCategory)
            etBillNo.setText(updateVoucherData.BillNo)
            etClaimDate.setText(updateVoucherData.ClaimDate)
            etBillDate.setText(updateVoucherData.BillDate)
            etBillFrom.setText(updateVoucherData.FromDate)
            etBillTo.setText(updateVoucherData.ToDate)
            etJourneyFrom.setText(updateVoucherData.FromLocation)
            etJourneyTo.setText(updateVoucherData.ToLocation)
            etBaseAmt.setText(updateVoucherData.Amount)
            etTaxAmt.setText(updateVoucherData.TaxAmount)
            etGrossAmt.setText(updateVoucherData.GrossAmount)
            etStartKm.setText(updateVoucherData.StartKM)
            etEndKm.setText(updateVoucherData.EndKM)
            etTravelMode.setText(updateVoucherData.ModeOfTravel)
            etRemark.setText(updateVoucherData.Remark)
            layoutUploadVoucher.visibility = View.GONE
            btnUpload.visibility = View.GONE
        }
    }

    private fun getPreferencesData() {
        empId = preferenceUtils.getValue(Constant.PreferenceKeys.EMPLOYEE_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpDropDownItemSelectedData(selectedCategoryId: Int) {
        binding.apply {
            when (selectedCategoryId) {
                ReimbursementCategory.FUEL_BILL.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutTravelMode.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.MOBILE.value, ReimbursementCategory.STATIONARY_INTERNET_MISC.value,
                ReimbursementCategory.COURIER_PHOTOCOPY.value,
                ReimbursementCategory.DAILY_ALLOWANCES.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.RELOCATION_ALLOWANCE.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.reference_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutTravelMode.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.TRAVELLING_PUBLIC.value, ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value -> {
                    callGetModeOfTravelApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutBillFrom.visibility = View.VISIBLE
                    layoutBillTo.visibility = View.VISIBLE
                    layoutJourneyFrom.visibility = View.VISIBLE
                    layoutJourneyTo.visibility = View.VISIBLE
                    layoutTravelMode.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.TRAVELLING_SELF.value, ReimbursementCategory.DAILY_MILEAGE.value -> {
                    callGetModeOfTravelApi()
                    callRejectedEndKmApi()
                    layoutBillDate.hint = getString(R.string.activity_date)
                    layoutBaseAmt.hint = getString(R.string.bill_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutJourneyFrom.visibility = View.VISIBLE
                    layoutJourneyTo.visibility = View.VISIBLE
                    layoutStartKm.visibility = View.VISIBLE
                    layoutEndKm.visibility = View.VISIBLE
                    layoutRunKm.visibility = View.VISIBLE
                    layoutTravelMode.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = false
                    etBaseAmt.setText("0")
                }
                ReimbursementCategory.FOOD_BILL.value -> {
                    callReimbursementSubCategoryApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutSubLeaveType.visibility = View.VISIBLE
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.requestFocusFromTouch()
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.BASF_OTHERS.value -> {
                    callReimbursementSubCategoryApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutSubLeaveType.visibility = View.VISIBLE
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTravelMode.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.LODGING_BOARDING.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutBillFrom.visibility = View.VISIBLE
                    layoutBillTo.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                ReimbursementCategory.OTHERS.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = View.VISIBLE
                    layoutBillNo.visibility = View.VISIBLE
                    layoutBaseAmt.visibility = View.VISIBLE
                    layoutTaxAmt.visibility = View.VISIBLE
                    layoutGrossAmt.visibility = View.VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.setText("")
                }
                else -> {
                    etBaseAmt.isEnabled = true
                    etTaxAmt.isEnabled = false
                    etBaseAmt.setText("")
                    clearData()
                }
            }

            if (isTaxApplicable) {
                etTaxAmt.isEnabled = true
            } else {
                etTaxAmt.isEnabled = false
                etTaxAmt.setText("0")
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

    private fun hideViews() {
        binding.layoutSubLeaveType.visibility = View.GONE
        binding.layoutBillNo.visibility = View.GONE
        binding.layoutBillDate.visibility = View.GONE
        binding.layoutBillFrom.visibility = View.GONE
        binding.layoutBillTo.visibility = View.GONE
        binding.layoutJourneyFrom.visibility = View.GONE
        binding.layoutJourneyTo.visibility = View.GONE
        binding.layoutBaseAmt.visibility = View.GONE
        binding.layoutTaxAmt.visibility = View.GONE
        binding.layoutGrossAmt.visibility = View.GONE
        binding.layoutStartKm.visibility = View.GONE
        binding.layoutEndKm.visibility = View.GONE
        binding.layoutTravelMode.visibility = View.GONE
        binding.layoutRunKm.visibility = View.GONE
    }

    private fun clearError() {
        binding.apply {
            layoutExpenseType.error = ""
            layoutBillDate.error = ""
            layoutBillNo.error = ""
            layoutClaimDate.error = ""
            layoutBillTo.error = ""
            layoutBillFrom.error = ""
            layoutJourneyFrom.error = ""
            layoutJourneyTo.error = ""
            layoutBaseAmt.error = ""
            layoutTaxAmt.error = ""
            layoutGrossAmt.error = ""
            layoutStartKm.error = ""
            layoutEndKm.error = ""
            layoutTravelMode.error = ""
            layoutRemark.error = ""
        }
    }

    private fun clearData() {
        binding.apply {
            etExpenseSubType.setText(R.string.Select)
            etTravelMode.setText(R.string.Select)
            etBillNo.setText("")
            etClaimDate.setText("")
            etBillDate.setText("")
            etBillFrom.setText("")
            etBillTo.setText("")
            etJourneyFrom.setText("")
            etJourneyTo.setText("")
            etBaseAmt.setText("")
            etTaxAmt.setText("0")
            etGrossAmt.setText("")
            etStartKm.setText("")
            etEndKm.setText("")
            etRunKm.setText("")
            etUploadVoucher.setText("")
            etRemark.setText("")

            selectedSubCategoryId = 0
            selectedBillFromDate = null
            selectedClaimDate = null
            selectedTravelModeId = 0
            imagePath = ""
        }
    }

    private fun callReimbursementSubCategoryApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementSubCategoryApi(
                request = ReimbursementSubCategoryRequestModel(ReimbursementCategoryId = selectedCategoryId)
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }
    private fun callRejectedEndKmApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callRejectedEndKmApi(request =
            RejectedEndKmRequestModel(
                GNETAssociateID = gnetAssociateId,
                ReimbursementDetailId = updateVoucherData.AssociateReimbursementDetailId.toString()
            )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callGetModeOfTravelApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementModeOfTravelApi(
                request = ModeOfTravelRequestModel(
                    EmployeeID = gnetAssociateId,
                    ReimbursementCategoryId = selectedCategoryId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callRejectedReimbursementValidationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callRejectedReimbursementValidationApi(
                    request = RejectedReimbursementValidationRequestModel(
                        Amount = decFormat.format(etBaseAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00,
                        AssociateReimbursementDetailId = updateVoucherData.AssociateReimbursementDetailId.toString(),
                        BillDate = etBillDate.text.toString().trim(),
                        BillNo = etBillNo.text.toString().trim(),
                        ExpenseTypeId = selectedCategoryId,
                        GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID),
                        GrossAmount = decFormat.format(etGrossAmt.text.toString().trim().toDoubleOrNull()?:0.00).toDoubleOrNull()?:0.00
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        callAttendanceValidationApi()
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.EXPENSE_TYPE_ERROR -> {
                    layoutExpenseType.error = getString(msg)
                }
                Constant.ListenerConstants.BILL_NUMBER_ERROR -> {
                    layoutBillNo.error = getString(msg)
                }
                Constant.ListenerConstants.CLAIM_DATE_ERROR -> {
                    layoutClaimDate.error = getString(msg)
                }
                Constant.ListenerConstants.BILL_DATE_ERROR -> {
                    layoutBillDate.error = getString(msg)
                }
                Constant.ListenerConstants.FROM_DATE_ERROR -> {
                    layoutBillFrom.error = getString(msg)
                }
                Constant.ListenerConstants.TO_DATE_ERROR -> {
                    layoutBillTo.error = getString(msg)
                }
                Constant.ListenerConstants.JOURNEY_FROM_ERROR -> {
                    layoutJourneyFrom.error = getString(msg)
                }
                Constant.ListenerConstants.JOURNEY_TO_ERROR -> {
                    layoutJourneyTo.error = getString(msg)
                }
                Constant.ListenerConstants.BASE_AMT_ERROR -> {
                    layoutBaseAmt.error = getString(msg)
                }
                Constant.ListenerConstants.GROSS_AMT_ERROR -> {
                    layoutGrossAmt.error = getString(msg)
                }
                Constant.ListenerConstants.TAX_AMT_ERROR -> {
                    layoutTaxAmt.error = getString(msg)
                }
                Constant.ListenerConstants.START_KM_ERROR -> {
                    layoutStartKm.error = getString(msg)
                }
                Constant.ListenerConstants.END_KM_ERROR -> {
                    layoutEndKm.error = getString(msg)
                }
                Constant.ListenerConstants.TRAVEL_MODE_ERROR -> {
                    layoutTravelMode.error = getString(msg)
                }
                Constant.ListenerConstants.REMARK_ERROR -> {
                    layoutRemark.error = getString(msg)
                }
            }
        }
    }
}