package com.example.digitracksdk.presentation.home.reimbursements

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Base64
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.digitracksdk.domain.model.reimbursement_model.AssociateReimbursementDetailListModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1RequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationRequestModel
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
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityAddReimbursementBinding
import com.innov.digitrac.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedReimbursementValidationRequestModel
import com.innov.digitrac.domain.model.reimbursement_model.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DecimalDigitsInputFilter
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.presentation.home.reimbursements.adapter.AddReimbursementListAdapter
import com.innov.digitrac.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


enum class ReimbursementCategory(val value: Int) {
    FUEL_BILL(1),
    TRAVELLING_PUBLIC(2),
    NIGHT_HALT_WITH_FOOD(2),
    MOBILE(3),
    STATIONARY_INTERNET_MISC(4),
    TRAVELLING_SELF(5),
    DAILY_MILEAGE(5),
    RELOCATION_ALLOWANCE(6),
    COURIER_PHOTOCOPY(7),
    BASF_OTHERS(8), //for nunhems and BASF
    FOOD_BILL(9),
    LODGING_BOARDING(11),
    OTHERS(12), // for innov_source and ceragon
    DAILY_ALLOWANCES(12),
    NO_REIMBURSEMENT(13)
}

class AddReimbursementActivity : BaseActivity(), ValidationListener,
    AddReimbursementListAdapter.ReimbursementListClickManager, DialogUtils.DialogManager {
    lateinit var binding: ActivityAddReimbursementBinding
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private var flagPocketExpense: Boolean = false
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    lateinit var approvalDialog: AlertDialog.Builder
    lateinit var addReimbursementListAdapter: AddReimbursementListAdapter
    var empId: String = ""
    var isUploadAlertDialog: Boolean = false
    var isTaxApplicable: Boolean = false

    var gnetAssociateId: String = ""
    var innovId: String = ""
    private var imagePath: String = "" // its used for image as well as pdf
    private var selectedClaimDate: Calendar? = null
    private var selectedBillDate: Calendar? = null
    var selectedBillFromDate: Calendar? = null
    private var reimbursementCategoryList: ArrayList<ReimbursementCategoryDetailsModel> =
        ArrayList()
    private var reimbursementSubCategoryList: ArrayList<ReimbursementSubCategoryDetailsModel> =
        ArrayList()
    private var reimbursementDetailsList: ArrayList<AssociateReimbursementDetailListModel> =
        ArrayList()
    private var travelModeList: ArrayList<ModeOfTravelDetailsModel> = ArrayList()
    private var selectedCategoryId: Int = 0
    var selectedSubCategoryId: Int = 0
    var selectedTravelModeId: Int = 0
    var isAdd: Boolean = false
    var isDone: Boolean = false
    var isEdit: Boolean = false
    var isReimbursementPreApproval: Boolean = false
    private var selectedItemPosition: Int = 0
    var localEndKm = 0

    var isNormalValidationFlow: Boolean = false

    //for update voucher
    private var updateVoucherData: ReimbursementDetailsModel = ReimbursementDetailsModel()
    var isFromEditBill: Boolean = false
    var associateReimbursementId: String = ""

    var allocationType: String = ""
    var approvalDate: String = ""
    var approverName: String = ""
    var approvedAdditionalAmt: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddReimbursementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        reimbursementViewModel.validationListener = this
        getPreferencesData()
        setObserver()
        getIntentData()
        setUpToolbar()
        setUpDecimalLimit()
        setUpReimbursementListAdapter()
        callReimbursementCategoryApi()
        setUpListener()
    }

    private fun setUpDecimalLimit() {
        binding.apply {
            etBaseAmt.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(9, 2))
            etTaxAmt.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(6, 2))
//            etGrossAmt.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(10, 2))
        }
    }

    private fun setObserver() {
        binding.apply {
            with(attendanceViewModel) {
                attendanceValidationResponseData.observe(
                    this@AddReimbursementActivity
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
                                    callReimbursementValidateApi()
                                }
                            }
                        }
                    }
                }

                messageData.observe(this@AddReimbursementActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(reimbursementViewModel) {

                reimbursementSubCategoryResponseData.observe(this@AddReimbursementActivity) {
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
                            this@AddReimbursementActivity,
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

                reimbursementEndKmResponseData.observe(this@AddReimbursementActivity) {
                    toggleLoader(false)
                    if (!isEdit) {
                        if (localEndKm < (it.EndKM?.toIntOrNull() ?: 0)) {
                            localEndKm = it.EndKM?.toIntOrNull() ?: 0
                        }
                        etRunKm.setText(localEndKm.toString())
                        etStartKm.setText(localEndKm.toString())
                    }
                }

                checkReimbursementLimitResponseData.observe(this@AddReimbursementActivity) {
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
                        if (it.Limit != 0.0) {

                            if (reimbursementDetailsList.size >= 1) {
                                val limitList = reimbursementDetailsList.filter { data ->
                                    data.ReimbursementCategoryId == selectedCategoryId
                                }
                                var grossAmt = 0.0
                                var totalAmt = 0.0
                                for (i in limitList) {
                                    grossAmt += if (i.GrossAmount.toString()
                                            .isEmpty()
                                    ) 0.0 else i.GrossAmount ?: 0.0
                                    totalAmt = grossAmt + if (etGrossAmt.text.toString()
                                            .isEmpty()
                                    ) 0.0 else etGrossAmt.text.toString().toDoubleOrNull() ?: 0.0
                                }
                                if ((it.Limit ?: 0.0) < totalAmt) {
                                    limit = totalAmt.toString().toDouble() - it.Limit.toString()
                                        .toDouble()
                                    openApprovalDialogue(limit.toString())
                                } else {
                                    callReimbursementValidateApi()
                                }

                            } else {
                                callReimbursementValidateApi()
                            }
                        } else {
                            callReimbursementValidateApi()
                        }
                    }
                }

                saveReimbursementPreApprovalResponseData.observe(this@AddReimbursementActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        DialogUtils.closeReimbursementPreApprovalDialog()
                        callReimbursementValidateApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                checkReimbursementValidationV1ResponseData.observe(this@AddReimbursementActivity) {
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
                        if (it.Limit != 0) {

                            if (reimbursementDetailsList.size >= 1) {
                                val limitList = reimbursementDetailsList.filter { data ->
                                    data.ReimbursementCategoryId == selectedCategoryId
                                }
                                var grossAmt = 0
                                var totalAmt = 0
                                for (i in limitList) {
                                    grossAmt += (if (i.GrossAmount.toString()
                                            .isEmpty()
                                    ) 0 else i.GrossAmount?.toInt()
                                        ?: 0)
                                }
                                if (isEdit) {
                                    val oldGrossAmt =
                                        reimbursementDetailsList[selectedItemPosition].GrossAmount?.toString()
                                            ?.toIntOrNull()
                                            ?: 0
                                    val newGrossAmt = etGrossAmt.text.toString().toIntOrNull() ?: 0
                                    grossAmt -= oldGrossAmt
                                    totalAmt = grossAmt + newGrossAmt
                                } else {
                                    totalAmt = grossAmt + (if (etGrossAmt.text.toString()
                                            .isEmpty()
                                    ) 0 else etGrossAmt.text.toString().toIntOrNull() ?: 0)
                                }
                                if ((it.Limit ?: 0) < totalAmt) {
                                    limit = totalAmt.toString().toDouble() - it.Limit.toString()
                                        .toDouble()
                                    openApprovalDialogue(limit.toString())
                                } else {
                                    callReimbursementValidateApi()
                                }

                            } else {
                                callReimbursementValidateApi()
                            }
                        } else {
                            callReimbursementValidateApi()
                        }
                    }
                }

                reimbursementValidationResponseData.observe(
                    this@AddReimbursementActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS && it.Message == Constant.SUCCESS) {
                        val checkDuplicateList = reimbursementDetailsList.find {
                            (it.BillDate == getReimbursementDetailsRequestModel().BillDate).and(
                                it.ReimbursementCategoryId == getReimbursementDetailsRequestModel().ReimbursementCategoryId
                            ).and(it.BillNo == getReimbursementDetailsRequestModel().BillNo)
                                .and(it.GrossAmount == getReimbursementDetailsRequestModel().GrossAmount)
                        }
                        if (checkDuplicateList == null) {
                            if (reimbursementDetailsList.size >= 1) {
                                val month = reimbursementDetailsList[0].BillDate.toString()
                                    .subSequence(3, 6)
                                val selectMonth =
                                    getReimbursementDetailsRequestModel().BillDate.toString()
                                        .subSequence(3, 6)
                                if (month == selectMonth) {
                                    if (isEdit) {
                                        isEdit = false
                                        reimbursementDetailsList[selectedItemPosition] =
                                            getReimbursementDetailsRequestModel()
                                    } else {
                                        reimbursementDetailsList.add(
                                            getReimbursementDetailsRequestModel()
                                        )
                                    }
                                    localEndKm =
                                        getReimbursementDetailsRequestModel().EndKm.toIntOrNull()
                                            ?: 0
                                    btnSubmit.text = getString(R.string.submit_reimbursement)
                                    etExpenseType.setText(getString(R.string.Select))
                                    selectedCategoryId = 0
                                    clearData()
                                    clearError()
                                    setUpToolbar()
                                    layoutAddReimbursement.visibility = GONE
                                    layoutReimbursementList.visibility = VISIBLE
                                    addReimbursementListAdapter.notifyDataSetChanged()
                                } else {
                                    showToast(getString(R.string.you_are_allowed_to_make_entry_for_same_bill_month_in_one_voucher_In_case_of_different_bill_month_please_submit_and_create_a_new_entry))
                                }
                            } else {
                                if (isEdit) {
                                    isEdit = false
                                    reimbursementDetailsList[selectedItemPosition] =
                                        getReimbursementDetailsRequestModel()
                                } else {
                                    reimbursementDetailsList.add(
                                        getReimbursementDetailsRequestModel()
                                    )
                                }
                                localEndKm =
                                    getReimbursementDetailsRequestModel().EndKm.toIntOrNull()
                                        ?: 0
                                btnSubmit.text = getString(R.string.submit_reimbursement)
                                etExpenseType.setText(getString(R.string.Select))
                                selectedCategoryId = 0
                                clearData()
                                clearError()
                                setUpToolbar()
                                layoutAddReimbursement.visibility = GONE
                                layoutReimbursementList.visibility = VISIBLE
                                addReimbursementListAdapter.notifyDataSetChanged()
                            }

                        } else {
                            if (!isEdit) {
                                if (selectedCategoryId != ReimbursementCategory.DAILY_MILEAGE.value || selectedCategoryId != ReimbursementCategory.TRAVELLING_SELF.value) {
                                    showToast(getString(R.string.bill_no_and_bill_amount_is_same_not_allowed))
                                }
                            } else {
                                if (isEdit) {
                                    isEdit = false
                                    reimbursementDetailsList[selectedItemPosition] =
                                        getReimbursementDetailsRequestModel()
                                } else {
                                    reimbursementDetailsList.add(
                                        getReimbursementDetailsRequestModel()
                                    )
                                }
                                localEndKm =
                                    getReimbursementDetailsRequestModel().EndKm.toIntOrNull()
                                        ?: 0
                                btnSubmit.text = getString(R.string.submit_reimbursement)
                                etExpenseType.setText(getString(R.string.Select))
                                selectedCategoryId = 0
                                clearData()
                                clearError()
                                setUpToolbar()
                                layoutAddReimbursement.visibility = GONE
                                layoutReimbursementList.visibility = VISIBLE
                                addReimbursementListAdapter.notifyDataSetChanged()
                            }
                        }

                    } else {
                        showToast(it.Message.toString())
                    }
                }

                reimbursementCategoryResponseData.observe(this@AddReimbursementActivity) {
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
                            this@AddReimbursementActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            categoryList
                        )
                        if (isFromEditBill) {
                            val categoryData = reimbursementCategoryList.find { data ->
                                data.ReimbursementCategory == updateVoucherData.ReimbursementCategory
                            }
                            if (categoryData != null) {
                                selectedCategoryId = categoryData.ReimbursementCategoryId ?: 0
                                hideViews()
                                clearData()
                                setUpDropDownItemSelectedData(selectedCategoryId)
                                setUpUpdateVoucherUI()
                            }

                        }
                        etExpenseType.setAdapter(adapter)

                    } else {
                        showToast(it.toString())
                    }
                }

                reimbursementModeOfTravelResponseData.observe(this@AddReimbursementActivity) {
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
                            this@AddReimbursementActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            list
                        )
                        etTravelMode.setAdapter(adapter)
                        if (isFromEditBill) {
                            val selectModeList = travelModeList.find { data ->
                                data.ModeOfTravle == updateVoucherData.ModeOfTravel
                            }
                            if (selectModeList != null) {
                                selectedTravelModeId = selectModeList.ModeOFTravelId ?: 0
                            }
                        }
                    } else {
                        showToast(it.toString())
                    }
                }

                rejectedReimbursementValidationResponseData.observe(
                    this@AddReimbursementActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        callUpdateVoucherBillApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                updateReimbursementDetailsResponseData.observe(
                    this@AddReimbursementActivity
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

                insertReimbursementResponseData.observe(
                    this@AddReimbursementActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        clearData()
                        selectedCategoryId = 0
                        etExpenseType.setText(R.string.Select)
                        layoutAddReimbursement.visibility = VISIBLE
                        layoutReimbursementList.visibility = GONE
                        reimbursementDetailsList.clear()
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@AddReimbursementActivity) {
                    toggleLoader(false)
                    showToast(it)
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
            this@AddReimbursementActivity,
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

    private fun callCheckNightHaltLimitApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                reimbursementViewModel.callCheckReimbursementValidationV1Api(
                    request =
                    CheckReimbursementLimitV1RequestModel(
                        Amount = decFormat.format(
                            etGrossAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                        ).toDoubleOrNull() ?: 0.00,
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

    private fun callReimbursementLimitApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                reimbursementViewModel.callCheckReimbursementLimitApi(
                    request =
                    CheckReimbursementLimitRequestModel(
                        Amount = decFormat.format(
                            etGrossAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                        ).toDoubleOrNull() ?: 0.00,
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
            etBaseAmt.setText(decFormat.format(updateVoucherData.Amount))
            etTaxAmt.setText(decFormat.format(updateVoucherData.TaxAmount))
            etGrossAmt.setText(updateVoucherData.GrossAmount)
            etStartKm.setText(decFormat.format(updateVoucherData.StartKM))
            etEndKm.setText(updateVoucherData.EndKM)
            etTravelMode.setText(updateVoucherData.ModeOfTravel)
            etRemark.setText(updateVoucherData.Remark)
            layoutUploadVoucher.visibility = GONE
            btnUpload.visibility = GONE
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromEditBill = getBoolean(Constant.IS_FOR_EDIT_BILL, false)
            updateVoucherData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(
                    Constant.DATA,
                    ReimbursementDetailsModel::class.java) as ReimbursementDetailsModel
            }else
                getSerializable(Constant.DATA) as ReimbursementDetailsModel
            associateReimbursementId = getString(Constant.associateReimbursementId).toString()
        }
    }

    private fun setUpReimbursementListAdapter() {
        addReimbursementListAdapter =
            AddReimbursementListAdapter(this, reimbursementDetailsList, this)
        binding.recyclerReimbursementList.adapter = addReimbursementListAdapter
    }

    private fun setUpListener() {
        binding.apply {

            onBackPressedDispatcher.addCallback(this@AddReimbursementActivity, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    binding.apply {
                        if (isEdit) {
                            layoutReimbursementList.visibility = VISIBLE
                            layoutAddReimbursement.visibility = GONE
                            btnSubmit.text = getString(R.string.apply_for_reimbursement)
                            isEdit = false
                            clearData()
                            clearError()
                            setUpToolbar()
                        } else {
                            if (selectedCategoryId != 0 || reimbursementDetailsList.isNotEmpty()) {
                                isUploadAlertDialog = false
                                DialogUtils.showPermissionDialog(
                                    this@AddReimbursementActivity,
                                    msg = getString(R.string.your_reimbursement_wont_be_saved),
                                    title = getString(R.string.are_you_sure),
                                    positiveBtn = getString(R.string.yes),
                                    negativeBtn = getString(R.string.cancel),
                                    isFinish = false,
                                    isOtherAction = true,
                                    listener = this@AddReimbursementActivity
                                )
                            } else {
                                finish()
                            }
                        }
                    }
                }
            })
            toolbar.btnBack.setOnClickListener {
                if (isEdit) {
                    layoutReimbursementList.visibility = VISIBLE
                    layoutAddReimbursement.visibility = GONE
                    btnSubmit.text = getString(R.string.apply_for_reimbursement)
                    isEdit = false
                    clearData()
                    clearError()
                    setUpToolbar()
                } else {
                    finish()
                }
            }
            toolbar.tvSave.setOnClickListener {
                reimbursementDetailsList.removeAt(selectedItemPosition)
                addReimbursementListAdapter.notifyDataSetChanged()
                layoutReimbursementList.visibility = VISIBLE
                layoutAddReimbursement.visibility = GONE
                btnSubmit.text = getString(R.string.apply_for_reimbursement)
                isEdit = false
                clearData()
                clearError()
                setUpToolbar()
            }

            etTaxAmt.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty() && text.toString() != ".") {
                    val taxAmt: Double = text.toString().trim().toDoubleOrNull() ?: 0.00
                    val baseAmt: Double
                    if (!etBaseAmt.text.isNullOrEmpty() && etBaseAmt.text.toString() != ".") {
                        baseAmt = etBaseAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                    } else {
                        baseAmt = 0.00
                    }
                    val total = taxAmt.plus(baseAmt)
                    if (total.toString() == ".00") {
                        etGrossAmt.setText("0.00")
                    } else {
                        etGrossAmt.setText(decFormat.format(total))
                    }
                } else {
                    etGrossAmt.setText(
                        decFormat.format(
                            etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00
                        )
                    )
                }
            }
            etBaseAmt.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty() && text.toString() != ".") {
                    etTaxAmt.setText("0")
                    val a = decFormat.format(text.toString().toDoubleOrNull() ?: 0.00)
                    if (text.toString() == "0") {
                        etGrossAmt.setText("0.00")
                    } else {
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
                    val startKm: Int
                    startKm = if (!etStartKm.text.isNullOrEmpty()) {
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

            btnUpload.setOnClickListener {
                openAddPhotoBottomSheet()
            }

            btnSubmit.setOnClickListener {
                clearError()
                isNormalValidationFlow = true
                if (btnSubmit.text.toString() == getString(R.string.add)) {
                    isAdd = true
                    isDone = false
                    reimbursementViewModel.validateApplyReimbursement(
                        getReimbursementDetailsRequestModel(),
                        localEndKm = localEndKm ,
                        isOldReimbursement = true
                    )
                } else if (btnSubmit.text.toString() == getString(R.string.done)) {
                    isDone = true
                    isAdd = false
                    reimbursementViewModel.validateApplyReimbursement(
                        getReimbursementDetailsRequestModel(),
                        localEndKm = reimbursementDetailsList[selectedItemPosition].StartKm.toIntOrNull()
                            ?: 0
                    )
                } else if (isFromEditBill) {
                    //TODO handle this
//                    callReimbursementValidateApi()
                    reimbursementViewModel.validateApplyReimbursement(
                        getReimbursementDetailsRequestModel(),
                        localEndKm = updateVoucherData.StartKM?.toIntOrNull() ?: 0
                    )
                } else {
                    if (!reimbursementDetailsList.isNullOrEmpty()) {
                        callApplyReimbursementApi()
                    } else {
                        showToast(getString(R.string.click_on_add_reimbursement))
                    }
                }
            }

            tvAddReimbursement.setOnClickListener {
                layoutReimbursementList.visibility = GONE
                layoutAddReimbursement.visibility = VISIBLE
                btnSubmit.text = getString(R.string.add)
            }

            etExpenseType.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, long ->
                    selectedCategoryId =
                        reimbursementCategoryList[position].ReimbursementCategoryId ?: 0
                    isTaxApplicable = reimbursementCategoryList[position].IsTaxApplicable == "Y"
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
                        if (selectedTravelModeId == 0) {
                            etBaseAmt.setText("0")
                        } else if (selectedTravelModeId == 25) {
                            // 25 for fourWheeler
                            val runKm = etRunKm.text.toString().toIntOrNull() ?: 0
                            val amt = runKm * dailyMileageLimitFourWheelerAmt
                            etBaseAmt.setText(amt.toString())
                        } else {
                            val runKm = etRunKm.text.toString().toIntOrNull() ?: 0
                            val amt = runKm * dailyMileageLimitAmt
                            etBaseAmt.setText(amt.toString())
                        }
                    }
                }

            etClaimDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@AddReimbursementActivity,
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
                    this@AddReimbursementActivity,
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
                    this@AddReimbursementActivity,
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
                    this@AddReimbursementActivity,
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

    private fun getUpdateVoucherRequestModel(): UpdateReimbursementDetailsRequestModel {
        binding.apply {
            val request = UpdateReimbursementDetailsRequestModel()
            request.AssociateId =
                preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID).toIntOrNull() ?: 0
            request.AssociateReimbursementDetailId =
                updateVoucherData.AssociateReimbursementDetailId?.toIntOrNull() ?: 0
            request.AssociateReimbursementId = associateReimbursementId.toIntOrNull() ?: 0
            request.BillDate = etBillDate.text.toString().trim()
            request.BaseAmount = 0.00
            request.TaxAmount =
                if (etTaxAmt.text.toString().trim().isEmpty()) 0.0 else decFormat.format(
                    etTaxAmt.text.toString()
                        .trim().toDoubleOrNull() ?: 0.00
                ).toDoubleOrNull() ?: 0.00
            request.GrossAmount =
                if (etGrossAmt.text.toString().trim().isEmpty()) 0.0 else decFormat.format(
                    etGrossAmt.text.toString()
                        .trim().toDoubleOrNull() ?: 0.00
                ).toDoubleOrNull() ?: 0.00
            request.BillNo = etBillNo.text.toString().trim()
            request.ClaimDate = etClaimDate.text.toString().trim()
            request.EndKM =
                if (etEndKm.text.toString().trim().isEmpty()) 0 else etEndKm.text.toString().trim()
                    .toInt()
            request.FromDate = etBillFrom.text.toString().trim()
            request.FromLocation = etJourneyFrom.text.toString().trim()
            request.GNETAssociateId = updateVoucherData.GNETAssociateId.toString()
            request.ModeOfTravelId = selectedTravelModeId
            request.NameOfPlace = ""
            request.ReimbursementCategoryId = selectedCategoryId
            request.ReimbursementSubCategoryId = selectedSubCategoryId
            request.Remark = etRemark.text.toString().trim()
            request.StartKM =
                if (etStartKm.text.toString().trim().isEmpty()) 0 else etStartKm.text.toString()
                    .trim().toInt()
            request.ToDate = etBillTo.text.toString().trim()
            request.ToLocation = etJourneyTo.text.toString().trim()
            request.UpdatedBy = ""
            request.Amount =
                if (etBaseAmt.text.toString().trim().isEmpty()) 0.00 else decFormat.format(
                    etBaseAmt.text.toString()
                        .trim().toDoubleOrNull() ?: 0.00
                ).toDoubleOrNull() ?: 0.00
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

    private fun setUpDropDownItemSelectedData(selectedCategoryId: Int) {
        binding.apply {
            layoutClaimDate.isEnabled = true

            when (selectedCategoryId) {
                ReimbursementCategory.FUEL_BILL.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutTravelMode.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
//                ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value -> {
//                    layoutBillDate.hint = getString(R.string.bill_date)
//                    layoutBaseAmt.hint = getString(R.string.base_amount)
//                    layoutBillNo.hint = getString(R.string.bill_number)
//                    layoutBillDate.visibility = VISIBLE
//                    layoutBillNo.visibility = VISIBLE
//                    layoutBillFrom.visibility = VISIBLE
//                    layoutBillTo.visibility = VISIBLE
//                    layoutJourneyFrom.visibility = VISIBLE
//                    layoutJourneyTo.visibility = VISIBLE
//                    layoutTravelMode.visibility = VISIBLE
//                    layoutBaseAmt.visibility = VISIBLE
//                    layoutTaxAmt.visibility = VISIBLE
//                    layoutGrossAmt.visibility = VISIBLE
//                    etBaseAmt.isEnabled = true
//                    etBaseAmt.setText("")
//                }
                ReimbursementCategory.MOBILE.value, ReimbursementCategory.STATIONARY_INTERNET_MISC.value,
                ReimbursementCategory.COURIER_PHOTOCOPY.value,
                ReimbursementCategory.DAILY_ALLOWANCES.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
                ReimbursementCategory.RELOCATION_ALLOWANCE.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.reference_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutTravelMode.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
                ReimbursementCategory.TRAVELLING_PUBLIC.value, ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value -> {
                    callGetModeOfTravelApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutBillFrom.visibility = VISIBLE
                    layoutBillTo.visibility = VISIBLE
                    layoutJourneyFrom.visibility = VISIBLE
                    layoutJourneyTo.visibility = VISIBLE
                    layoutTravelMode.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
                ReimbursementCategory.TRAVELLING_SELF.value, ReimbursementCategory.DAILY_MILEAGE.value -> {
                    callGetModeOfTravelApi()
                    callReimbursementEndKmApi()
                    layoutBillDate.hint = getString(R.string.activity_date)
                    layoutBaseAmt.hint = getString(R.string.bill_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutJourneyFrom.visibility = VISIBLE
                    layoutJourneyTo.visibility = VISIBLE
                    layoutStartKm.visibility = VISIBLE
                    layoutEndKm.visibility = VISIBLE
                    layoutRunKm.visibility = VISIBLE
                    layoutTravelMode.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = false
                    etBaseAmt.setText("0")
                }
                ReimbursementCategory.FOOD_BILL.value -> {
                    callReimbursementSubCategoryApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutSubLeaveType.visibility = VISIBLE
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    etBaseAmt.requestFocusFromTouch()
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
                ReimbursementCategory.BASF_OTHERS.value -> {
                    callReimbursementSubCategoryApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutSubLeaveType.visibility = VISIBLE
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTravelMode.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
                ReimbursementCategory.LODGING_BOARDING.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutBillFrom.visibility = VISIBLE
                    layoutBillTo.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etBaseAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
                }
                ReimbursementCategory.OTHERS.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutBillDate.visibility = VISIBLE
                    layoutBillNo.visibility = VISIBLE
                    layoutBaseAmt.visibility = VISIBLE
                    layoutTaxAmt.visibility = VISIBLE
                    layoutGrossAmt.visibility = VISIBLE
                    layoutUploadVoucher.visibility = VISIBLE
                    btnUpload.visibility = VISIBLE
                    layoutRemark.visibility = VISIBLE
                    etTaxAmt.isEnabled = true
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }

                }
                ReimbursementCategory.NO_REIMBURSEMENT.value -> {
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBillDate.visibility = VISIBLE
                    layoutClaimDate.isEnabled = false
                }
                else -> {
                    etBaseAmt.isEnabled = true
                    etTaxAmt.isEnabled = false
                    etTaxAmt.setText("0")
                    if (!isFromEditBill) {
                        etBaseAmt.setText("")
                    }
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

    private fun getReimbursementDetailsRequestModel(): AssociateReimbursementDetailListModel {
        binding.apply {
            val request = AssociateReimbursementDetailListModel()
            request.dailyMileageLimit = etRunKm.text.toString()
            request.Amount = decFormat.format(etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00)
                .toDoubleOrNull() ?: 0.00
            request.BillDate = etBillDate.text.toString().trim()
            request.BillNo = etBillNo.text.toString().trim()
            request.ClaimDate = etClaimDate.text.toString().trim()
            request.Extn = etUploadVoucher.text.toString().split(".").last()
            request.FilePath = imagePath
            request.FromDate = etBillFrom.text.toString().trim()
            request.ToDate = etBillTo.text.toString().trim()
            request.FromLocation = etJourneyFrom.text.toString().trim()
            request.GrossAmount =
                decFormat.format(etGrossAmt.text.toString().trim().toDoubleOrNull() ?: 0.00)
                    .toDoubleOrNull() ?: 0.00
            request.ModeOfTravelId = selectedTravelModeId
            request.NameOfPlace = ""
            request.ReimbursementCategoryId = selectedCategoryId
            request.reimbursementCategoryName = etExpenseType.text.toString().trim()
            request.ReimbursementSubCategoryId = selectedSubCategoryId
            request.reimbursementSubCategoryName = etExpenseSubType.text.toString().trim()
            request.Remark = etRemark.text.toString().trim()
            request.setModeOfTravelName = etTravelMode.text.toString().trim()
            request.TaxAmount =
                decFormat.format(etTaxAmt.text.toString().trim().toDoubleOrNull() ?: 0.0)
                    .toDoubleOrNull() ?: 0.00
            request.ToLocation = etJourneyTo.text.toString().trim()
            request.StartKm = etStartKm.text.toString().trim()
            request.EndKm = etEndKm.text.toString().trim()
            return request
        }
    }

    private fun getAddReimbursementRequestModel(): InsertReimbursementRequestModel {
        val request = InsertReimbursementRequestModel()
        request.AssociateReimbursementDetailList = reimbursementDetailsList
        request.GNETAssociateId = gnetAssociateId
        request.Extn1 = ""
        request.FilePath1 = ""
        request.Remark = ""
        return request
    }


    private fun hideViews() {
        binding.apply {
            layoutSubLeaveType.visibility = GONE
            layoutBillNo.visibility = GONE
            layoutBillDate.visibility = GONE
            layoutBillFrom.visibility = GONE
            layoutBillTo.visibility = GONE
            layoutJourneyFrom.visibility = GONE
            layoutJourneyTo.visibility = GONE
            layoutBaseAmt.visibility = GONE
            layoutTaxAmt.visibility = GONE
            layoutGrossAmt.visibility = GONE
            layoutStartKm.visibility = GONE
            layoutEndKm.visibility = GONE
            layoutTravelMode.visibility = GONE
            layoutRunKm.visibility = GONE
            layoutUploadVoucher.visibility = GONE
            btnUpload.visibility = GONE
            layoutRemark.visibility = GONE
        }
    }

    private fun callApplyReimbursementApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callInsertReimbursementApi(request = getAddReimbursementRequestModel())
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callReimbursementEndKmApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementEndKmApi(
                request = ReimbursementEndKmRequestModel(
                    GNETAssociateId = gnetAssociateId
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

    private fun getPreferencesData() {
        empId = preferenceUtils.getValue(Constant.PreferenceKeys.EMPLOYEE_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
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

    private fun setUpToolbar() {
        binding.toolbar.apply {
            if (isEdit) {
                tvTitle.text = getString(R.string.edit)
                tvSave.apply {
                    text = getString(R.string.delete)
                    tvSave.setTextColor(
                        ContextCompat.getColor(
                            this@AddReimbursementActivity,
                            R.color.rejected_color
                        )
                    )
                    visibility = VISIBLE
                }
            } else if (isFromEditBill) {
                tvTitle.text = getString(R.string.update_expense_voucher)
                divider.visibility = VISIBLE
                tvSave.visibility = GONE
            } else {
                tvTitle.text = getString(R.string.apply_for_reimbursement)
                divider.visibility = VISIBLE
                tvSave.visibility = GONE
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

    private val pdfResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val fileName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME)
                imagePath =
                    Base64.encodeToString(
                        AppUtils.INSTANCE?.getPdfToBase64(
                            Uri.parse(url.toString()),
                            this
                        ), Base64.NO_WRAP
                    )
                binding.apply {
                    if (imagePath.length > 1000000) {
                        imagePath = ""
                        etUploadVoucher.setText("")
                        showToast(getString(R.string.pdf_file_size_more_than_1_mb_not_allowed))
                    } else {

                        if (isReimbursementPreApproval) {
                            callReimbursementPreApprovalApi(
                                approvedAdditionalAmt,
                                approverName,
                                approvalDate,
                                "pdf"
                            )
                        } else {
                            etUploadVoucher.setText(fileName.toString())
                        }

                    }
                }
            }
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

                binding.apply {
                    if (isReimbursementPreApproval) {
                        callReimbursementPreApprovalApi(
                            approvedAdditionalAmt,
                            approverName,
                            approvalDate,
                            fileName.toString().split(".").last()
                        )
                    } else {
                        etUploadVoucher.setText(fileName.toString())
                    }
                }
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
                    ApprovedAmount = decFormat.format(approvedAmt.toDoubleOrNull() ?: 0.00)
                        .toDoubleOrNull() ?: 0.00,
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
            imgPdf.visibility = VISIBLE
            tvPdf.visibility = VISIBLE
            tvAddPhoto.text = getString(R.string.choose_image_pdf)
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
            imgPdf.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, false)
                b.putBoolean(Constant.IS_PDF, true)
                intent.putExtras(b)
                pdfResult.launch(intent)
                bottomSheetDialog.dismiss()
            }
        }

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

    override fun onValidationSuccess(type: String, msg: Int) {
        //TODO uncomment this
        if (isAdd || isDone) {
            if (imagePath.isNullOrEmpty() && selectedCategoryId != ReimbursementCategory.NO_REIMBURSEMENT.value) {
                isUploadAlertDialog = true
                DialogUtils.showPermissionDialog(
                    this,
                    title = getString(R.string.upload_voucher),
                    msg = getString(R.string.documents_have_not_added_do_you_want_to_still_proceed),
                    positiveBtn = getString(R.string.continue_),
                    negativeBtn = getString(R.string.close),
                    isFinish = false,
                    isOtherAction = true,
                    listener = this
                )
            } else {
                callAttendanceValidationApi()
            }
        } else if (isFromEditBill) {
            callRejectedReimbursementValidationApi()
        }

    }

    private fun callRejectedReimbursementValidationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callRejectedReimbursementValidationApi(
                    request = RejectedReimbursementValidationRequestModel(
                        Amount = decFormat.format(
                            etBaseAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                        ).toDoubleOrNull() ?: 0.00,
                        AssociateReimbursementDetailId = updateVoucherData.AssociateReimbursementDetailId.toString(),
                        BillDate = etBillDate.text.toString().trim(),
                        BillNo = etBillNo.text.toString().trim(),
                        ExpenseTypeId = selectedCategoryId,
                        GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID),
                        GrossAmount = decFormat.format(
                            etGrossAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                        ).toDoubleOrNull() ?: 0.00
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    override fun onContinueClick() {
        if (isUploadAlertDialog) {
            callAttendanceValidationApi()
        } else {
            finish()
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

    private fun getReimbursementValidateRequestModel(): ReimbursementValidationRequestModel {
        binding.apply {
            val request = ReimbursementValidationRequestModel()
            request.Amount = decFormat.format(etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00)
                .toDoubleOrNull() ?: 0.00
            request.BillDate = etBillDate.text.toString().replace(" ", "-")
            request.BillNo = etBillNo.text.toString()
            request.GNETAssociateID = gnetAssociateId
            request.GrossAmount =
                decFormat.format(etGrossAmt.text.toString().toDoubleOrNull() ?: 0.00)
                    .toDoubleOrNull() ?: 0.00
            request.ExpenseTypeId = selectedCategoryId.toString()
            return request
        }
    }

    private fun callReimbursementValidateApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callReimbursementValidationApi(
                    getReimbursementValidateRequestModel()
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
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

    override fun clickOnDeleteBtn(position: Int) {
        reimbursementDetailsList.removeAt(position)
        addReimbursementListAdapter.notifyDataSetChanged()
    }

    override fun clickOnItem(position: Int) {
        isEdit = true
        selectedItemPosition = position
        binding.apply {
            layoutAddReimbursement.visibility = VISIBLE
            layoutReimbursementList.visibility = GONE
            btnSubmit.text = getString(R.string.done)
        }
        setUpToolbar()
        setEditReimbursementUi()
    }

    private fun setEditReimbursementUi() {
        val data = reimbursementDetailsList[selectedItemPosition]
        binding.apply {
            selectedCategoryId = data.ReimbursementCategoryId ?: 0
            hideViews()
            clearData()
            setUpDropDownItemSelectedData(selectedCategoryId)

            selectedSubCategoryId = data.ReimbursementSubCategoryId ?: 0
            selectedTravelModeId = data.ModeOfTravelId ?: 0
            imagePath = data.FilePath.toString()

            etExpenseType.setText(data.reimbursementCategoryName.toString())
            etExpenseSubType.setText(data.reimbursementSubCategoryName.toString())
            etClaimDate.setText(data.ClaimDate)
            etBillDate.setText(data.BillDate)
            etBillNo.setText(data.BillNo)
            etBillFrom.setText(data.FromDate)
            etBillTo.setText(data.ToDate)
            etJourneyFrom.setText(data.FromLocation)
            etJourneyTo.setText(data.ToLocation)
            etBaseAmt.setText(data.Amount.toString())
            etTaxAmt.setText(data.TaxAmount.toString())
            etGrossAmt.setText(data.GrossAmount.toString())
            etStartKm.setText(data.StartKm)
            etEndKm.setText(data.EndKm)
            etTravelMode.setText(data.setModeOfTravelName)
            etRemark.setText(data.Remark)
            etUploadVoucher.setText(if (data.FilePath.isNullOrEmpty()) "" else "${getString(R.string.bill).lowercase()}.${data.Extn ?: "jpg"}")
        }
    }
}