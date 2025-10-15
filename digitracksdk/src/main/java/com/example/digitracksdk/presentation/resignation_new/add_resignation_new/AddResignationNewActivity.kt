package com.example.digitracksdk.presentation.resignation_new.add_resignation_new

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityAddResignationNewBinding
import com.innov.digitrac.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.resignation.ListResignationCategory
import com.example.digitracksdk.domain.model.resignation.LstResignationReason
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.resignation.add_resignation.AddResignationViewModel
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.util.ArrayList
import java.util.Calendar

class AddResignationNewActivity : BaseActivity(), ValidationListener, DialogUtils.DialogManager {
    lateinit var binding: ActivityAddResignationNewBinding
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    lateinit var preferenceUtils: PreferenceUtils
    private var resignationCategoryList: ArrayList<ListResignationCategory> = ArrayList()
    private var resignationReasonList: ArrayList<LstResignationReason> = ArrayList()
    private val addResignationViewModel: AddResignationViewModel by viewModel()
    private var selectedLastWorkingDate: String? = ""
    private var noticePeriodDay: Int = 0
    private var selectedResignationCategoryId: Int = 0
    private var selectedResignationReasonId: Int = 0
    private var imageBitmap: String = ""
    private var imageExtn: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddResignationNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        addResignationViewModel.validationListener = this
        setUpToolbar()
        setObserver()
        callNoticePeriodApi()
        callResignationCategoryApi()
        callResignationReasonApi()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(addResignationViewModel) {
                resignationReasonResponseData.observe(this@AddResignationNewActivity) {
                    if (it.status?.lowercase() == com.example.digitracksdk.Constant.success) {
                        if (it.lstResignationReason?.size != 0) {
                            resignationReasonList.clear()
                            it.lstResignationReason?.let { it1 ->
                                resignationReasonList.addAll(
                                    it1
                                )
                            }

                            val reasonList: ArrayList<String> = ArrayList()
                            reasonList.clear()
                            for (i in resignationReasonList) {
                                reasonList.add(i.ResignationCategory.toString())
                            }
                            val adapter = ArrayAdapter(
                                this@AddResignationNewActivity,
                                android.R.layout.simple_list_item_1,
                                reasonList
                            )
                            etResignationReason.setAdapter(adapter)
                        }


                    } else {
                        showToast(it.Message.toString())
                    }

                }


                resignationNoticePeriodResponseData.observe(this@AddResignationNewActivity) {
                    if (it.status?.lowercase() == com.example.digitracksdk.Constant.success) {

                        noticePeriodDay = it.NoticePeriod?.toIntOrNull() ?: 0

                    } else {
                        showToast(it.Message.toString())
                    }
                    setUpView()
                }

                resignationCategoryResponseData.observe(this@AddResignationNewActivity) {
                    if (it.status == com.example.digitracksdk.Constant.success) {
                        if (it.lstResignationCategory?.size != 0) {
                            resignationCategoryList.clear()
                            it.lstResignationCategory?.let { it1 ->
                                resignationCategoryList.addAll(
                                    it1
                                )
                            }
                            val resignationList: ArrayList<String> = ArrayList()
                            resignationList.clear()
                            for (i in resignationCategoryList) {
                                resignationList.add(i.ResignationCategory.toString())
                            }
                            val adapter = ArrayAdapter(
                                this@AddResignationNewActivity,
                                android.R.layout.simple_list_item_1,
                                resignationList
                            )
                            etResignationCategory.setAdapter(adapter)

                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                resignationResponseData.observe(this@AddResignationNewActivity) {
                    if (it.status == com.example.digitracksdk.Constant.success) {
//                        clearData()
//                        setUpView()
                        finish()
                    }
                    showToast(it.Message.toString())
                }


                showProgressbar.observe(this@AddResignationNewActivity) {
                    toggleLoader(it)
                }
                messageData.observe(this@AddResignationNewActivity) {
                    showToast(it)
                }
            }
        }

    }

    private fun setUpListener() {
        binding.apply {
            etExpectedLastWorkingDay.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog: DatePickerDialog = DatePickerDialog(
                    this@AddResignationNewActivity, { view, year, monthOfYear, dayOfMonth ->

                        //For selected date
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedLastWorkingDate = AppUtils.INSTANCE?.convertDateToString(
                            selectedCalendar.time, "dd MMM yyyy"
                        )

                        if (noticePeriodDay == 0) {
                            noticePeriodDay = 30
                        }
                        //For add 30 days in selected date
                        val calendar = Calendar.getInstance()
//                        calendar.set(Calendar.YEAR, year)
//                        calendar.set(Calendar.MONTH, monthOfYear)
//                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        calendar.add(Calendar.DAY_OF_MONTH, noticePeriodDay)
                        calendar.time
                        if (selectedCalendar.time < calendar.time) {
                            etExpectedLastWorkingDay.setText("")
                            DialogUtils.showPermissionDialog(
                                this@AddResignationNewActivity,
                                getString(
                                    R.string.if_preferred_last_working_day_is_less_then_last_working_day
                                ),
                                getString(
                                    R.string.alert
                                ),
                                getString(R.string.continue_),
                                getString(R.string.close),
                                isFinish = false,
                                isOtherAction = true,
                                listener = this@AddResignationNewActivity
                            )
                        } else {
                            etExpectedLastWorkingDay.setText(selectedLastWorkingDate)
                        }
//                        val lastWorkingDate = AppUtils.INSTANCE?.convertDateToString(
//                            calendar.time,
//                            "dd MMM yyyy"
//                        )
//                        etLastWorkingDay.setText(lastWorkingDate)
                    }, y, m, d
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, noticePeriodDay)

                datePickerDialog.datePicker.maxDate = calendar.time.time
                datePickerDialog.show()
            }
            etResignationCategory.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    selectedResignationCategoryId =
                        resignationCategoryList[position].ResignationCategoryId ?: 0
                }

            btnUploadFile.setOnClickListener {
                openAddPhotoBottomSheet()
            }
            btnDelete.setOnClickListener {
                etUploadImage.setText("")
                imageBitmap = ""
                imageExtn = null
                btnDelete.visibility = View.GONE
            }


            etResignationReason.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    selectedResignationReasonId =
                        resignationReasonList[position].ResignationCategoryId ?: 0
                }
            btnSubmit.setOnClickListener {
                addResignationViewModel.validateNewResignation(
                    getResignationRequestModel(),
                    selectedResignationReasonId
                )
            }
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
        val intent = Intent(this@AddResignationNewActivity, AddImageUtils::class.java)
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

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val fileName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME)
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitmapFile =
                    File(url.toString())
                val bitmap: Bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
                imageBitmap = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                imageExtn = fileName.toString().split(".").last()
                binding.apply {
                    etUploadImage.setText(fileName.toString())
                    btnDelete.visibility = View.VISIBLE
                }
            }
        }

    private fun getResignationRequestModel(): ResignationRequestModel {
        val request = ResignationRequestModel()
        request.AssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID)
        request.DateOfResignation = binding.etExpectedLastWorkingDay.text.toString().trim()
        request.EmployeeId = preferenceUtils.getValue(Constant.PreferenceKeys.EMPLOYEE_ID)
        request.ExpectedLastWorkingDate = binding.etLastWorkingDay.text.toString().trim()
        request.Extn = imageExtn.toString()
        request.InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.Reason = binding.etResignationReason.text.toString().trim()
        request.ResgImageArr = imageBitmap
        request.ResignationCategoryId = selectedResignationCategoryId
        return request
    }


    override fun onContinueClick() {
        binding.etExpectedLastWorkingDay.setText(selectedLastWorkingDate)
    }

    private fun setUpView() {
        binding.apply {
            if (noticePeriodDay == 0) {
                noticePeriodDay = 30
            }
            etLastWorkingDay.setText(AppUtils.INSTANCE?.getResignationDate(noticePeriodDay))
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


    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.resignation_new)
            divider.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun callNoticePeriodApi() {
        if (isNetworkAvailable()) {
            val request = ResignationNoticePeriodRequestModel()
            request.GNETAssociateId =
                preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)

            addResignationViewModel.callResignationNoticePeriodApi(request)
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callResignationCategoryApi() {
        if (isNetworkAvailable()) {
            addResignationViewModel.callResignationCategoryApi()
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callResignationReasonApi() {
        if (isNetworkAvailable()) {
            val request = ResignationReasonRequestModel()
            request.GNETAssociateId =
                preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)

            addResignationViewModel.callResignationReasonApi(request)
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        clearErrors()
        callResignationApi()
    }

    private fun callResignationApi() {
        if (isNetworkAvailable()) {
            addResignationViewModel.callResignationApi(getResignationRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        clearErrors()
        when (type) {
            Constant.ListenerConstants.RESIGNATION_CATEGORY_ID_ERROR -> {
                binding.layoutResignationCategory.error = getString(msg)
            }

            Constant.ListenerConstants.LAST_WORKING_DATE_ERROR -> {
                binding.layoutExpectedLastWorkingDay.error = getString(msg)
            }

            Constant.ListenerConstants.REASON_ERROR -> {
                binding.layoutReason.error = getString(msg)
            }

            /*    Constant.ListenerConstants.IMAGE_ERROR -> {
                    binding.layoutUploadImage.error = getString(msg)
                }*/

        }
    }

    private fun clearErrors() {
        binding.apply {
            layoutResignationCategory.error = ""
            layoutExpectedLastWorkingDay.error = ""
            layoutReason.error = ""
        }
    }

}