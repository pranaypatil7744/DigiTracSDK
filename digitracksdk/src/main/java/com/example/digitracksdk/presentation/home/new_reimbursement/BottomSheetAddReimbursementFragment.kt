package com.example.digitracksdk.presentation.home.new_reimbursement

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Insets
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
import com.example.digitracksdk.domain.model.reimbursement_model.SaveReimbursementPreApprovalRequestModel
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.BottomSheetAddNewReimbursmentFragmentBinding
import com.example.digitracksdk.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthYearDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementCategory
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DecimalDigitsInputFilter
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class BottomSheetAddReimbursementFragment : BottomSheetDialogFragment(), DialogUtils.DialogManager,
    ValidationListener {
    lateinit var binding: BottomSheetAddNewReimbursmentFragmentBinding
    lateinit var dialog: BottomSheetDialog
    lateinit var approvalDialog: AlertDialog.Builder

    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    private var imagePath: String = "" // its used for image as well as pdf
    var isReimbursementPreApproval: Boolean = false
    lateinit var preferenceUtils: PreferenceUtils
    var gnetAssociateId: String = ""
    var innovId: String = ""
    var associateId: String = ""
    var empId: String = ""
    var isTaxApplicable: Boolean = false
    private var flagPocketExpense: Boolean = false
    var isNormalValidationFlow: Boolean = false

    var localEndKm = 0

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
    private var selectedClaimDate: Calendar? = null
    private var selectedBillDate: Calendar? = null
    var selectedBillFromDate: Calendar? = null

    var allocationType: String = ""
    var approvalDate: String = ""
    var approverName: String = ""
    var approvedAdditionalAmt: String = ""

    private val yearAdapterName = ArrayList<String>()
    private val yearAdapterId = ArrayList<Int>()
    private val monthAdapterName = ArrayList<String>()
    private val monthAdapterId = ArrayList<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddNewReimbursmentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            setupFullHeight(dialog)
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight: Int = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
//        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        //TODO need to check.
        val s = (context as Activity?)!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics: WindowMetrics =
                s.currentWindowMetrics

            val windowInsets = metrics.windowInsets
            val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars()
                        or WindowInsets.Type.displayCutout()
            )
            val insetsWidth: Int = insets.right + insets.left
            val insetsHeight: Int = insets.top + insets.bottom
            val bounds: Rect = metrics.bounds
            return Size(
                bounds.width() - insetsWidth,
                bounds.height() - insetsHeight
            ).height
        } else {
            return displayMetrics.heightPixels
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reimbursementViewModel.validationListener = this
        preferenceUtils = PreferenceUtils(requireActivity())
        getPreferencesData()
        setUpListener()
        setUpObserver()
        callReimbursementCategoryApi()
        callGetYearDetailsApi()
        callGetMonthDetailsApi()
    }

    private fun callGetMonthDetailsApi() {
        if (requireActivity().isNetworkAvailable()) {
            innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            associateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID)
            reimbursementViewModel.callGetYearDetailsApi(
                request = GetMonthYearDetailsRequestModel(
                    InnovID = innovId.toInt(),
                    Associates = associateId
                )
            )
        }
    }

    private fun callGetYearDetailsApi() {
        if (requireActivity().isNetworkAvailable()) {

            innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            associateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID)
            reimbursementViewModel.callGetMonthDetailsApi(
                request = GetMonthYearDetailsRequestModel(
                    InnovID = innovId.toInt(),
                    Associates = associateId
                )
            )
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(attendanceViewModel) {
                attendanceValidationResponseData.observe(
                    requireActivity()
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
                                    if(selectedCategoryId == ReimbursementCategory.TRAVELLING_SELF.value)
                                    {
                                        callNewReimbursementInsertApi()
                                    }else{
                                        callReimbursementLimitApi()
                                    }

                                } else {
                                    callReimbursementValidateApi()
                                }
                            }
                        }
                    }
                }

                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(reimbursementViewModel) {
                reimbursementCategoryResponseData.observe(requireActivity()) {
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
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            categoryList
                        )

                        etExpenseType.setAdapter(adapter)

                    } else {
                        showToast(it.toString())
                    }
                }

                reimbursementSubCategoryResponseData.observe(requireActivity()) {
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
                            requireContext(),
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

                reimbursementEndKmResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (localEndKm < (it.EndKM?.toIntOrNull() ?: 0)) {
                        localEndKm = it.EndKM?.toIntOrNull() ?: 0
                    }
                    etRunKm.setText(localEndKm.toString())
                    etStartKm.setText(localEndKm.toString())
                }

                checkReimbursementValidationV1ResponseData.observe(requireActivity()) {
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
                        callReimbursementValidateApi()
                    }
                }

                saveReimbursementPreApprovalResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        DialogUtils.closeReimbursementPreApprovalDialog()
                        callReimbursementValidateApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                checkReimbursementLimitResponseData.observe(requireActivity()) {
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
                        callReimbursementValidateApi()
                    }
                }

                reimbursementValidationResponseData.observe(
                    requireActivity()
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS && it.Message == Constant.SUCCESS) {
//                        callApplyReimbursementApi()
                        callNewReimbursementInsertApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                insertReimbursementResponseData.observe(
                    requireActivity()
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        clearData()
                        selectedCategoryId = 0
                        etExpenseType.setText(R.string.Select)
                        reimbursementDetailsList.clear()
                        dismiss()
                        (requireActivity() as Reimbursement1Activity).pendingVouchersListFragment.callReimbursementListForVoucherApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                insertNewReimbursementResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        clearData()
                        selectedCategoryId = 0
                        etExpenseType.setText(R.string.Select)
                        reimbursementDetailsList.clear()
                        dismiss()
                        (requireActivity() as Reimbursement1Activity).pendingVouchersListFragment.callReimbursementListForVoucherApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }



                reimbursementModeOfTravelResponseData.observe(requireActivity()) {
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
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            list
                        )
                        etTravelMode.setAdapter(adapter)

                    } else {
                        showToast(it.toString())
                    }
                }

                getYearDetailsResponseData.observe(requireActivity()) {
//                    toggleLoader(false)
                    if (!it.Status.isNullOrEmpty() && it.Status.lowercase() == Constant.success) {
                        if (!it.LstYearDetails.isNullOrEmpty()) {
                            it.LstYearDetails.forEach {
                                it.YearDesc?.let { name ->
                                    yearAdapterName.add(name)
                                    yearAdapterId.add(it.YearId ?: 0)
                                }
                            }
                        }

                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            yearAdapterName
                        )
                        etYear.setAdapter(adapter)

                    } else {
                        showToast(it.toString())
                    }
                }
                getMonthDetailsResponseData.observe(requireActivity())
                {
//                    toggleLoader(false)
                    if (!it.Status.isNullOrEmpty() && it.Status.lowercase() == Constant.success) {
                        if (!it.LstMonthDetails.isNullOrEmpty()) {
                            it.LstMonthDetails.forEach {
                                it.Month_Name?.let { name ->
                                    monthAdapterName.add(name)
                                    monthAdapterId.add(it.MonthId ?: 0)
                                }
                            }
                        }

                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            monthAdapterName
                        )
                        etMonth.setAdapter(adapter)
                    } else {
                        showToast(it.toString())
                    }
                }

                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun callNewReimbursementInsertApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                reimbursementViewModel.callInsertNewReimbursementApi(getNewReimbursementInsertModel())
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun getNewReimbursementInsertModel(): InsertNewReimbursementRequestModel {
        binding.apply {
            val request = InsertNewReimbursementRequestModel()
//        request.dailyMileageLimit = etRunKm.text.toString()
            request.Amount =
                Constant.decFormat.format(etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00)
                    .toDoubleOrNull() ?: 0.00
            request.BillDate = etBillDate.text.toString().trim()
            request.BillNo = etBillNo.text.toString().trim()
            request.ClaimDate = etClaimDate.text.toString().trim()
            request.ClaimMonth =
                monthAdapterId.get(monthAdapterName.indexOf(etMonth.text.toString())).toString()
            request.ClaimYear =etYear.text.toString()
            request.EndKM = etEndKm.text.toString().trim()
            request.Extn = etUploadVoucher.text.toString().split(".").last()
            request.FilePath = imagePath
            request.FromDate = etBillFrom.text.toString().trim()
            request.ToDate = etBillTo.text.toString().trim()
            request.FromLocation = etJourneyFrom.text.toString().trim()
            request.GNETAssociateId = gnetAssociateId
            request.GrossAmount =
                Constant.decFormat.format(
                    etGrossAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                ).toDoubleOrNull() ?: 0.00
            request.ModeOfTravelId = selectedTravelModeId
            request.NameOfPlace = ""
            request.ReimbursementCategoryId = selectedCategoryId.toString()
            request.reimbursementCategoryName = etExpenseType.text.toString().trim()
            request.ReimbursementSubCategoryId = selectedSubCategoryId.toString()
//            request.reimbursementSubCategoryName = etExpenseSubType.text.toString().trim()
            request.Remark = etRemark.text.toString().trim()
            request.StartKM = etStartKm.text.toString().trim()

//            request.setModeOfTravelName = etTravelMode.text.toString().trim()
            request.TaxAmount =
                Constant.decFormat.format(etTaxAmt.text.toString().trim().toDoubleOrNull() ?: 0.0)
                    .toDoubleOrNull() ?: 0.00
            request.ToLocation = etJourneyTo.text.toString().trim()
            return request
        }
    }

    private fun callReimbursementValidateApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callReimbursementValidationApi(
                    getReimbursementValidateRequestModel()
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callApplyReimbursementApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callInsertReimbursementApi(request = getAddReimbursementRequestModel())
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun getAddReimbursementRequestModel(): InsertReimbursementRequestModel
    {
        val request = InsertReimbursementRequestModel()
        reimbursementDetailsList.clear()
        reimbursementDetailsList.add(getReimbursementDetailsRequestModel())
        request.AssociateReimbursementDetailList = reimbursementDetailsList
        request.GNETAssociateId = gnetAssociateId
        request.Extn1 = ""
        request.FilePath1 = ""
        request.Remark = ""
        return request
    }

    private fun getReimbursementValidateRequestModel(): ReimbursementValidationRequestModel
    {
        binding.apply {
            val request = ReimbursementValidationRequestModel()
            request.Amount =
                Constant.decFormat.format(etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00)
                    .toDoubleOrNull() ?: 0.00
            request.BillDate = etBillDate.text.toString().replace(" ", "-")
            request.BillNo = etBillNo.text.toString()
            request.GNETAssociateID = gnetAssociateId
            request.GrossAmount =
                Constant.decFormat.format(etGrossAmt.text.toString().toDoubleOrNull() ?: 0.00)
                    .toDoubleOrNull() ?: 0.00
            request.ExpenseTypeId = selectedCategoryId.toString()
            return request
        }
    }

    private fun openApprovalDialogue(approvedAmt: String)
    {
        approvalDialog = AlertDialog.Builder(requireContext())
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
        DialogUtils.showReimbursementPreApprovalDialog(requireContext(), this, approvedAmt)
    }

    override fun onChooseDateClick(date: TextInputEditText)
    {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
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

    private fun callReimbursementLimitApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                reimbursementViewModel.callCheckReimbursementLimitApi(
                    request =
                    CheckReimbursementLimitRequestModel(
                        Amount = Constant.decFormat.format(
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

    private fun callCheckNightHaltLimitApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                reimbursementViewModel.callCheckReimbursementValidationV1Api(
                    request =
                    CheckReimbursementLimitV1RequestModel(
                        Amount = Constant.decFormat.format(
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

    private fun callReimbursementCategoryApi() {
        if (requireActivity().isNetworkAvailable()) {
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

    private fun callGetModeOfTravelApi() {
        if (requireActivity().isNetworkAvailable()) {
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

    private fun toggleLoader(showLoader: Boolean)
    {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    fun toggleFadeView(
        parent: View,
        loader: View,
        imageView: ImageView,
        showLoader: Boolean
    )
    {

        if (showLoader) {
            AppUtils.INSTANCE?.hideFadeView(parent, Constant.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showFadeView(loader, Constant.VIEW_ANIMATE_DURATION)
            ImageUtils.INSTANCE?.loadLocalGIFImage(imageView, R.drawable.loader)
            loader.visibility = VISIBLE
        } else {
            AppUtils.INSTANCE?.hideView(loader, Constant.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showView(parent, Constant.VIEW_ANIMATE_DURATION)
            loader.visibility = GONE
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnSubmit.setOnClickListener {
                isNormalValidationFlow = true
                clearError()
                reimbursementViewModel.validateApplyReimbursement(
                    getReimbursementDetailsRequestModel(),
                    localEndKm = 0
                )
            }
            btnClose.setOnClickListener {
                dismiss()
            }
            btnUpload.setOnClickListener {
                openAddPhotoBottomSheet()
            }
            etBaseAmt.apply {
                filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(9, 2))
            }
            etTaxAmt.apply {
                filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(6, 2))
            }
//            etGrossAmt.apply {
//                filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2))
//            }
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
                        etGrossAmt.setText(Constant.decFormat.format(total))
                    }
                } else {
                    etGrossAmt.setText(
                        Constant.decFormat.format(
                            etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00
                        )
                    )
                }
            }

            etBaseAmt.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty() && text.toString() != ".") {
                    etTaxAmt.setText("0")
                    val a = Constant.decFormat.format(text.toString().toDoubleOrNull() ?: 0.00)
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

            etYear.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, long ->
                    clearError()
                }

            etMonth.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, long ->
                    clearError()
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
                    requireContext(),
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
                    requireContext(),
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
                    requireContext(),
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
                    requireContext(),
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
            request.Amount =
                Constant.decFormat.format(etBaseAmt.text.toString().toDoubleOrNull() ?: 0.00)
                    .toDoubleOrNull() ?: 0.00
            request.BillDate = etBillDate.text.toString().trim()
            request.BillNo = etBillNo.text.toString().trim()
            request.ClaimDate = etClaimDate.text.toString().trim()
            request.ClaimYear = etYear.text.toString().trim()
            request.ClaimMonth = etMonth.text.toString().trim()
            request.Extn = etUploadVoucher.text.toString().split(".").last()
            request.FilePath = imagePath
            request.FromDate = etBillFrom.text.toString().trim()
            request.ToDate = etBillTo.text.toString().trim()
            request.FromLocation = etJourneyFrom.text.toString().trim()
            request.GrossAmount =
                Constant.decFormat.format(
                    etGrossAmt.text.toString().trim().toDoubleOrNull() ?: 0.00
                )
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
                Constant.decFormat.format(etTaxAmt.text.toString().trim().toDoubleOrNull() ?: 0.0)
                    .toDoubleOrNull() ?: 0.00
            request.ToLocation = etJourneyTo.text.toString().trim()
            request.StartKm = etStartKm.text.toString().trim()
            request.EndKm = etEndKm.text.toString().trim()
            return request
        }
    }

    private fun callReimbursementEndKmApi() {
        if (requireActivity().isNetworkAvailable()) {
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

    private fun callReimbursementSubCategoryApi() {
        if (requireActivity().isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementSubCategoryApi(
                request = ReimbursementSubCategoryRequestModel(ReimbursementCategoryId = selectedCategoryId)
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callAttendanceValidationApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
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
                }

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
                    layoutExpenseSubType.visibility = VISIBLE
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
                }

                ReimbursementCategory.BASF_OTHERS.value -> {
                    callReimbursementSubCategoryApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutExpenseSubType.visibility = VISIBLE
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
                }

                ReimbursementCategory.LODGING_BOARDING.value -> {
                    callReimbursementSubCategoryApi()
                    layoutBillDate.hint = getString(R.string.bill_date)
                    layoutBaseAmt.hint = getString(R.string.base_amount)
                    layoutBillNo.hint = getString(R.string.bill_number)
                    layoutExpenseSubType.visibility = VISIBLE
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

    private fun clearError() {
        binding.apply {
            layoutExpenseType.error = ""
            layoutBillDate.error = ""
            layoutYearType.error = ""
            layoutMonthType.error = ""
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

    private fun hideViews() {
        binding.apply {
            layoutExpenseSubType.visibility = GONE
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

    private fun openAddPhotoBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_photo, null)
        addPhotoBottomSheetDialogBinding = BottomSheetAddPhotoBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        val intent = Intent(requireContext(), AddImageUtils::class.java)
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
                            requireContext()
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

    private fun callReimbursementPreApprovalApi(
        approvedAmt: String,
        approvedBy: String,
        approvedDate: String,
        extn: String
    ) {
        if (requireActivity().isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callSaveReimbursementPreApprovalApi(
                request =
                SaveReimbursementPreApprovalRequestModel(
                    ApplicableForPeriod = allocationType,
                    ApprovedAmount = Constant.decFormat.format(approvedAmt.toDoubleOrNull() ?: 0.00)
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


    private fun getPreferencesData() {
        empId = preferenceUtils.getValue(Constant.PreferenceKeys.EMPLOYEE_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    fun showToast(msg: String) {
        AppUtils.INSTANCE?.showLongToast(requireContext(), msg)
    }

    override fun onContinueClick() {
        callAttendanceValidationApi()
    }

    override fun onAttachmentClick(date: String, name: String, amt: String) {
        isReimbursementPreApproval = true
        approvalDate = date
        approverName = name
        approvedAdditionalAmt = amt
        openAddPhotoBottomSheet()
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        if (imagePath.isNullOrEmpty() && selectedCategoryId != ReimbursementCategory.NO_REIMBURSEMENT.value) {
            DialogUtils.showPermissionDialog(
                requireActivity(),
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
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.EXPENSE_TYPE_ERROR -> {
                    layoutExpenseType.error = getString(msg)
                }

                Constant.ListenerConstants.CLAIM_YEAR_TYPE_ERROR -> {
                    layoutYearType.error = getString(msg)
                }

                Constant.ListenerConstants.CLAIM_MONTH_TYPE_ERROR -> {
                    layoutMonthType.error = getString(msg)
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