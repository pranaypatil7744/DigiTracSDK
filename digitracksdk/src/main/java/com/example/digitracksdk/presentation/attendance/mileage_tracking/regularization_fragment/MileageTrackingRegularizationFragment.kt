package com.example.digitracksdk.presentation.attendance.mileage_tracking.regularization_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.databinding.BottomSheetMileageTrackingBinding
import com.example.digitracksdk.databinding.FragmentMileageTrackingRegularizationBinding
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.mileage_tracking.MileageTrackingViewModel
import com.example.digitracksdk.presentation.attendance.mileage_tracking.adapter.MileageTrackingStatusAdapter
import com.example.digitracksdk.presentation.attendance.mileage_tracking.model.AttendanceMileageTrackingModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class MileageTrackingRegularizationFragment : BaseFragment(), ValidationListener {

    lateinit var binding: FragmentMileageTrackingRegularizationBinding
    lateinit var bottomSheetDialog: BottomSheetDialog

    lateinit var mileageTrackingStatusAdapter: MileageTrackingStatusAdapter
    lateinit var bottomSheetMileageTrackingBinding: BottomSheetMileageTrackingBinding
    private val mileageTrackingViewModel: MileageTrackingViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils

    var innovId: String = ""
    var gnetAssociateId: String = ""
    var mileageRegularizationList: ArrayList<AttendanceMileageTrackingModel> = ArrayList()

    companion object {
        fun newInstance(): MileageTrackingRegularizationFragment {
            return MileageTrackingRegularizationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentMileageTrackingRegularizationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(this.requireContext())
        mileageTrackingViewModel.validationListener = this
        getPreferenceData()
        setUpMileageTrackingAdapter()
        setObserver()
        setUpListener()
        callMileageRegularizationListApi()
    }

    private fun setObserver() {
        binding.apply {
            with(mileageTrackingViewModel) {
                mileageRegularizationListResponseData.observe(this@MileageTrackingRegularizationFragment.viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        if (!it.MileageRegularizationlst.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            mileageRegularizationList.clear()
                            for (i in it.MileageRegularizationlst ?: arrayListOf()) {
                                mileageRegularizationList.add(
                                    AttendanceMileageTrackingModel(
                                        date = i.TravelDate,
                                        startReading = i.StartReading,
                                        mileageTrackingStatus = AppUtils.INSTANCE?.getLeaveStatus(i.ApprovalStatus.toString())
                                            ?: com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus.PENDING,
                                        closeReading = i.EndReading
                                    )
                                )
                            }
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }
                insertMileageRegularizationResponseData.observe(
                    this@MileageTrackingRegularizationFragment.viewLifecycleOwner
                ) { it ->
                    toggleLoader2(false)
                    if (it.Status == Constant.SUCCESS) {
                        bottomSheetDialog.dismiss()
                        showToast(it.Message.toString())
                        callMileageRegularizationListApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(
                    this@MileageTrackingRegularizationFragment.viewLifecycleOwner
                ) { t ->
                    toggleLoader(false)
                    toggleLoader2(false)
                    showToast(t.toString())
                }
            }
        }
    }

   /* override fun onResume() {
        super.onResume()
        callMileageRegularizationListApi()
    }*/

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun callMileageRegularizationListApi() {
        if (context?.isNetworkAvailable() == true) {
            binding.layoutNoInternet.root.visibility = GONE
            toggleLoader(true)
            mileageTrackingViewModel.callMileageRegularizationListApi(
                request = MileageRegularizationListRequestModel(
                    EmpCode = gnetAssociateId
                )
            )

        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
            binding.recyclerRegularization.visibility = GONE
        }
        binding.layoutNoData.root.visibility = GONE
    }

    private fun getInsertMileageRegularizationRequestModel(): InsertMileageRegularizationRequestModel {
        bottomSheetMileageTrackingBinding.apply {
            val request = InsertMileageRegularizationRequestModel()
            request.Empcode = gnetAssociateId
            request.EndReading = etClosing.text.toString().trim()
            request.StartReading = etOpening.text.toString().trim()
            request.TravelDate = etChooseDate.text.toString().trim()
            request.Remark = etReason.text.toString().trim()
            return request
        }
    }

    private fun callInsertMileageRegularizationApi() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader2(true)
            mileageTrackingViewModel.callInsertMileageRegularizationApi(
                getInsertMileageRegularizationRequestModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpListener() {

        binding.apply {
            fabMileageTracking.setOnClickListener {
                openBottomSheetMileageTrackingRegularization()
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callMileageRegularizationListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callMileageRegularizationListApi()
            }
        }
    }

    private fun openBottomSheetMileageTrackingRegularization() {
        bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_mileage_tracking, null)
        bottomSheetMileageTrackingBinding = BottomSheetMileageTrackingBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetMileageTrackingBinding.apply {
            layoutUploadImage.visibility = GONE
            layoutUploadImage2.visibility = GONE
            btnUploadFile.visibility = GONE
            layoutReason.visibility = VISIBLE
            tvNewReimbursementRequest.text = getString(R.string.mileage_tracking_regularization)
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnDone.setOnClickListener {
                clearError()
                mileageTrackingViewModel.validateMileageRegularization(
                    getInsertMileageRegularizationRequestModel()
                )
            }
            etChooseDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@MileageTrackingRegularizationFragment.requireContext(),
                    { view, year, monthOfYear, dayOfMonth ->

                        //For selected date
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd MMM yyyy"
                            )
                        etChooseDate.setText(selectedDate)
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
        }

        bottomSheetDialog.show()
    }

    private fun setUpMileageTrackingAdapter() {
        mileageTrackingStatusAdapter =
            MileageTrackingStatusAdapter(this.requireContext(), mileageRegularizationList)
        binding.recyclerRegularization.adapter = mileageTrackingStatusAdapter
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
                recyclerView = recyclerRegularization,
                show = show
            )
        }
    }

    private fun toggleLoader2(showLoader: Boolean) {
        toggleFadeView(
            bottomSheetMileageTrackingBinding.root,
            bottomSheetMileageTrackingBinding.contentLoading.root,
            bottomSheetMileageTrackingBinding.contentLoading.imageLoading,
            showLoader
        )
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        callInsertMileageRegularizationApi()
    }

    override fun onValidationFailure(type: String, msg: Int) {
        bottomSheetMileageTrackingBinding.apply {
            when (type) {
                Constant.ListenerConstants.FROM_DATE_ERROR -> {
                    layoutChooseDate.error = getString(msg)
                }
                Constant.ListenerConstants.OPEN_READING_ERROR -> {
                    layoutOpeningReading.error = getString(msg)
                }
                Constant.ListenerConstants.CLOSE_READING_ERROR -> {
                    layoutClosingReading.error = getString(msg)
                }
                Constant.ListenerConstants.REMARK_ERROR -> {
                    layoutReason.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        bottomSheetMileageTrackingBinding.apply {
            layoutChooseDate.error = ""
            layoutOpeningReading.error = ""
            layoutClosingReading.error = ""
            layoutReason.error = ""
        }
    }

}