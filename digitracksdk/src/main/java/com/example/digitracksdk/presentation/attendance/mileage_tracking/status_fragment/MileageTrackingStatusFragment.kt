package com.example.digitracksdk.presentation.attendance.mileage_tracking.status_fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagRequestModel
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.databinding.BottomSheetMileageTrackingBinding
import com.example.digitracksdk.databinding.FragmentMileageTrackingStatusBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.mileage_tracking.MileageTrackingViewModel
import com.example.digitracksdk.presentation.attendance.mileage_tracking.adapter.MileageTrackingStatusAdapter
import com.example.digitracksdk.presentation.attendance.mileage_tracking.model.AttendanceMileageTrackingModel
import com.example.digitracksdk.utils.AppUtils
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.collections.ArrayList


enum class ReadingStatus(val Value: Int) {
    OPENING(0),
    CLOSING(1),
    NONE(2)
}

class MileageTrackingStatusFragment : BaseFragment(), ValidationListener {

    lateinit var binding: FragmentMileageTrackingStatusBinding
    private val mileageTrackingViewModel: MileageTrackingViewModel by viewModel()
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    var geoLat: String = ""
    var geoLong: String = ""
    var radius: Int = 0
    var currentLatitude: String? = ""
    var currentLongitude: String? = ""
    lateinit var mileageTrackingStatusAdapter: MileageTrackingStatusAdapter
    lateinit var bottomSheetMileageTrackingBinding: BottomSheetMileageTrackingBinding
    var mileageTrackingList: ArrayList<AttendanceMileageTrackingModel> = ArrayList()
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var preferenceUtils: PreferenceUtils
    var gnetAssociateId: String = ""
    var innovId: String = ""
    var readingStatus: Int = ReadingStatus.NONE.Value
    var readingImage: String = ""

    companion object {
        fun newInstance(): MileageTrackingStatusFragment {
            return MileageTrackingStatusFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMileageTrackingStatusBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(this.requireContext())
        mileageTrackingViewModel.validationListener = this
        getPreferenceData()
        setObserver()
        setUpMileageTrackingStatusAdapter()
        callMileageTrackingListApi()
        callGeoFancingApi()
        setUpListener()
    }


    private fun setObserver() {
        binding.apply {
            with(attendanceViewModel) {
                attendanceGeoFancingResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.status?.lowercase() == Constant.success) {
                        geoLat = it.Latitude.toString()
                        geoLong = it.Longitude.toString()
                        radius = it.Radius ?: 0
                    } else {
                        showToast(it.Message.toString())
                    }
                }
            }

            with(mileageTrackingViewModel) {
                mileageTrackingListResponseData.observe(
                    this@MileageTrackingStatusFragment.requireActivity()
                ) { it ->
                    toggleLoader(false)
                    if (it.lstMileage?.size != 0) {
                        showNoDataLayout(false)
                        mileageTrackingList.clear()
                        for (i in it.lstMileage ?: arrayListOf()) {
                            mileageTrackingList.add(
                                AttendanceMileageTrackingModel(
                                    date = i.TravelDate,
                                    startReading = i.StartReading,
                                    closeReading = i.EndReading,
                                    mileageTrackingStatus = AppUtils.INSTANCE?.getLeaveStatus(i.Status.toString())
                                        ?: com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus.PENDING
                                )
                            )
                        }
                        mileageTrackingStatusAdapter.notifyDataSetChanged()
                    } else {
                        showNoDataLayout(true)
                    }
                }

                insertMileageTrackingResponseData.observe(
                    this@MileageTrackingStatusFragment.viewLifecycleOwner
                ) { it ->
                    toggleLoader2(false)
                    if (it.Status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        bottomSheetDialog.dismiss()
                        readingImage = ""
                        callMileageTrackingListApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                mileageTrackingFlagResponseData.observe(
                    this@MileageTrackingStatusFragment.viewLifecycleOwner
                ) { it ->
                    toggleLoader2(false)
                    bottomSheetMileageTrackingBinding.apply {
                        readingStatus = it.Status ?: ReadingStatus.NONE.Value
                        when (it.Status) {
                            ReadingStatus.OPENING.Value -> {
                                layoutOpeningReading.visibility = VISIBLE
                                layoutUploadImage.visibility = VISIBLE
                                btnUploadFile.visibility = VISIBLE
                            }

                            ReadingStatus.CLOSING.Value -> {
                                layoutClosingReading.visibility = VISIBLE
                                layoutUploadImage.visibility = VISIBLE
                                btnUploadFile.visibility = VISIBLE
                            }

                            ReadingStatus.NONE.Value -> {
                                hideViews()
                            }
                        }
                    }
                }

                messageData.observe(
                    this@MileageTrackingStatusFragment.viewLifecycleOwner
                ) { t ->
                    toggleLoader(false)
                    toggleLoader2(false)
                    showToast(t.toString())
                    //TODO if api got error it mark as 200 code
                    //TODO need to think about recyclerView hide or not
                }
            }
        }
    }

    private fun callGeoFancingApi() {
        if (requireActivity().isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callAttendanceGeoFancingApi(
                request = CommonRequestModel(
                    InnovId = innovId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun checkIsLocationValid(): Boolean {
        val destLat: Double = currentLatitude?.toDouble() ?: 0.0
        val destLong: Double = currentLongitude?.toDouble() ?: 0.0
        if (radius == 0) {
            radius = 100
        }
        val arr = floatArrayOf(0f)
        Location.distanceBetween(geoLat.toDouble(), geoLong.toDouble(), destLat, destLong, arr)
        return arr.first() <= radius
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun callMileageTrackingListApi() {
        binding.apply {

            if (context?.isNetworkAvailable() == true) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                mileageTrackingViewModel.callMileageTrackingListApi(
                    request = MileageTrackingListRequestModel(
                        EmployeeCode = gnetAssociateId
                    )
                )

            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerStatus.visibility = GONE
            }
            layoutNoData.root.visibility = GONE

        }
    }

    private val locationUtil =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentLatitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LATITUDE)
                currentLongitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LONGITUDE)
            }
        }

    private fun setUpListener() {
        binding.apply {
            fabMileageTracking.setOnClickListener {
                openBottomSheetMileageTracking()
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callMileageTrackingListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callMileageTrackingListApi()
            }
        }
    }

    private fun openBottomSheetMileageTracking() {
        bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_mileage_tracking, null)
        bottomSheetMileageTrackingBinding = BottomSheetMileageTrackingBinding.bind(view)
        bottomSheetDialog.apply {
            setContentView(view)
            setCancelable(true)
            show()
        }
        hideViews()
        callMileageTrackingFlagApi()
        bottomSheetMileageTrackingBinding.apply {
            etChooseDate.setText(AppUtils.INSTANCE?.getCurrentDate())
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            btnUploadFile.setOnClickListener {
                clearError()
                pickUpImage()
            }
            btnDelete.setOnClickListener {
                etUploadImage.setText("")
                readingImage = ""
                btnDelete.visibility = GONE
            }

            btnDone.setOnClickListener {
                if (readingStatus == ReadingStatus.NONE.Value) {
                    bottomSheetDialog.dismiss()
                } else {
                    clearError()
                    if (geoLat != "0" || geoLong != "0") {
                        if (currentLatitude?.isNotEmpty() == true && currentLongitude?.isNotEmpty() == true) {
                            if (checkIsLocationValid()) {
                                mileageTrackingViewModel.validateMileageTracking(
                                    getInsertMileageTrackingRequestModel(),
                                    readingStatus
                                )
                            } else {
                                showToast(getString(R.string.your_are_not_in_mileage_range))
                            }
                        } else {
                            locationUtil.launch(Intent(requireContext(), LocationUtils::class.java))
                        }
                    } else {
                        mileageTrackingViewModel.validateMileageTracking(
                            getInsertMileageTrackingRequestModel(),
                            readingStatus
                        )
                    }

                }

            }
        }
    }

    private fun getInsertMileageTrackingRequestModel(): InsertMileageTrackingRequestModel {
        bottomSheetMileageTrackingBinding.apply {
            val request = InsertMileageTrackingRequestModel()
            request.EmployeeCode = gnetAssociateId
            request.EndReading = etClosing.text.toString().trim()
            request.StartReading = etOpening.text.toString().trim()
            request.TravelDate = etChooseDate.text.toString().trim()
            request.StartReadingImageArr =
                if (readingStatus == ReadingStatus.OPENING.Value) readingImage else ""
            request.EndReadingImageArr =
                if (readingStatus == ReadingStatus.CLOSING.Value) readingImage else ""
            return request
        }
    }

    private fun callInsertMileageTrackingApi() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader2(true)
            mileageTrackingViewModel.callInsertMileageTrackingApi(
                getInsertMileageTrackingRequestModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun hideViews() {
        bottomSheetMileageTrackingBinding.apply {
            layoutClosingReading.visibility = GONE
            layoutOpeningReading.visibility = GONE
            layoutUploadImage.visibility = GONE
            layoutUploadImage2.visibility = GONE
            btnUploadFile.visibility = GONE
        }
    }

    private fun callMileageTrackingFlagApi() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader2(true)
            mileageTrackingViewModel.callMileageTrackingFlagApi(
                request = MileageTrackingFlagRequestModel(
                    EmployeeCode = gnetAssociateId
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
                val bitmapFile =
                    File(url.toString())
                val bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
                readingImage = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                bottomSheetMileageTrackingBinding.apply {
                    etUploadImage.setText(fileName.toString())
                    btnDelete.visibility = VISIBLE
                }
            }
        }

    private fun pickUpImage() {
        val intent = Intent(this.context, AddImageUtils::class.java)
        val b = Bundle()
        b.putBoolean(Constant.IS_CAMERA, true)
        intent.putExtras(b)
        addImageUtils.launch(intent)
    }

    private fun setUpMileageTrackingStatusAdapter() {
        mileageTrackingStatusAdapter =
            MileageTrackingStatusAdapter(this.requireContext(), mileageTrackingList)
        binding.recyclerStatus.adapter = mileageTrackingStatusAdapter
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
                recyclerView = recyclerStatus,
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
        callInsertMileageTrackingApi()
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

                Constant.ListenerConstants.IMAGE_ERROR -> {
                    layoutUploadImage.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        bottomSheetMileageTrackingBinding.apply {
            layoutChooseDate.error = ""
            layoutOpeningReading.error = ""
            layoutClosingReading.error = ""
            layoutUploadImage.error = ""
        }
    }

}