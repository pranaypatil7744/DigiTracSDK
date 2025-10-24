package com.example.digitracksdk.presentation.home.geo_tracking_2

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.BottomSheetViewAttendanceFilterBinding
import com.example.digitracksdk.databinding.GeoTrackingListingActivityBinding
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingList
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.presentation.home.geo_tracking.GeoTrackingSummaryViewModel
import com.example.digitracksdk.presentation.home.geo_tracking.tracking_details.GeoTrackingDetailsActivity
import com.example.digitracksdk.presentation.home.geo_tracking_2.adapter.GeoTrackingListingAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar

class GeoTrackingListingActivity : BaseActivity(),
    GeoTrackingListingAdapter.GeoTrackingListener {
    private var isFilterApplied: Boolean = false
    private var selectedFromDate: Calendar? = null
    private val geoTrackingList: ArrayList<GeoTrackingList> = arrayListOf()
    private lateinit var geoTrackingListingAdapter: GeoTrackingListingAdapter
    private var fromDate: String = ""
    private var toDate: String = ""
    private lateinit var bottomSheetViewAttendanceFilterBinding: BottomSheetViewAttendanceFilterBinding

    private lateinit var binding: GeoTrackingListingActivityBinding
    private val geoTrackingSummaryViewModel: GeoTrackingSummaryViewModel by viewModel()
    private lateinit var preferenceUtils: PreferenceUtils
    val cal = Calendar.getInstance()
    private var y = cal.get(Calendar.YEAR)
    private var m = cal.get(Calendar.MONTH)
    private var d = cal.get(Calendar.DAY_OF_MONTH)

    private var yearForToDate = cal.get(Calendar.YEAR)
    private var monthForToDate = cal.get(Calendar.MONTH)
    private var dayForToDate = cal.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = GeoTrackingListingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this@GeoTrackingListingActivity)
        setUpToolbar()
        setUpListeners()
        setUpAdapter()
        setUpObserver()
    }

    private fun setUpObserver() {
        binding.apply {
            with(geoTrackingSummaryViewModel) {
                geoTrackingSummaryListResponseData.observe(this@GeoTrackingListingActivity) {
                    if (it.status == Constant.SUCCESS) {
                        if (it.lstGeoTrackingList.isNotEmpty()) {
                            showNoDataLayout(false)
                            geoTrackingList.clear()
                            geoTrackingList.addAll(it.lstGeoTrackingList)
                            setUpAdapter()
                        } else {
                            binding.layoutNoData.tvNoData.text = it.message
                            showNoDataLayout(true)
                        }
                    } else {
                        showToast(it.message)
                        showNoDataLayout(true)
                    }
                }
                messageData.observe(this@GeoTrackingListingActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@GeoTrackingListingActivity) {
                    toggleLoader(it)
                }


            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            if (::geoTrackingListingAdapter.isInitialized)
                geoTrackingListingAdapter.notifyDataSetChanged()
            else {
                geoTrackingListingAdapter = GeoTrackingListingAdapter(
                    this@GeoTrackingListingActivity,
                    geoTrackingList,
                    this@GeoTrackingListingActivity
                )
                binding.rcvGeoTrackingSummary.adapter = geoTrackingListingAdapter
            }
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.tracking_summary)

            divider.visibility = VISIBLE
            btnOne.apply {
                visibility = VISIBLE
                setImageResource(R.drawable.ic_filter_1)
                setOnClickListener {
                    openFilterBottomSheet()
                }

            }
        }
    }

    private fun openFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_view_attendance_filter, null)

        bottomSheetViewAttendanceFilterBinding = BottomSheetViewAttendanceFilterBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetViewAttendanceFilterBinding.apply {
            etFromDate.setText(fromDate)
            etToDate.setText(toDate)

            etToDate.isFocusable = false
            etFromDate.isFocusable = false
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnApply.setOnClickListener {
                fromDate = etFromDate.text.toString().trim()
                toDate = etToDate.text.toString().trim()
                if (fromDate.isEmpty()) {
                    layoutFromDate.error = getString(R.string.please_choose_from_date)
                } else if (toDate.isEmpty()) {
                    layoutToDate.error = getString(R.string.please_choose_to_date)
                } else {
                    isFilterApplied = true
                    binding.toolbar.btnOne.setImageResource(R.drawable.ic_filter_applied)
                    callGeoTrackingListApi()
                    bottomSheetDialog.dismiss()
                }

            }
            etFromDate.setOnClickListener {

                val datePickerDialog = DatePickerDialog(
                    this@GeoTrackingListingActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        y = year
                        m = monthOfYear
                        d = dayOfMonth
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
                val datePickerDialog = DatePickerDialog(
                    this@GeoTrackingListingActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        yearForToDate = year
                        monthForToDate = monthOfYear
                        dayForToDate = dayOfMonth
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

                        etToDate.setText(selectedDate)
                    },
                    yearForToDate,
                    monthForToDate,
                    dayForToDate
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


    private fun setUpListeners() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callGeoTrackingListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                isFilterApplied = false
                fromDate = ""
                toDate = ""
                toolbar.btnOne.setImageResource(R.drawable.ic_filter_1)
                callGeoTrackingListApi()
            }
            binding.btnStartTracking.setOnClickListener {
                if (!Settings.canDrawOverlays(this@GeoTrackingListingActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    systemAlertLauncher.launch(intent)
                } else {
                    startActivity(
                        Intent(
                            this@GeoTrackingListingActivity,
                            GeoTrackingActivity::class.java
                        )
                    )
                }

            }
        }
    }

    private val systemAlertLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Settings.canDrawOverlays(this@GeoTrackingListingActivity)) {
                startActivity(Intent(this, GeoTrackingDetailsActivity::class.java))
            }
        }


    private fun callGeoTrackingListApi() {
        with(binding) {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                geoTrackingSummaryViewModel.callGeoTrackingSummaryListApi(
                    getGeoTrackingSummaryRequestModel()
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                rcvGeoTrackingSummary.visibility = GONE
            }
            layoutNoData.root.visibility = GONE
        }
    }

    private fun getGeoTrackingSummaryRequestModel(): GeoTrackingSummaryListRequestModel {
        val request = GeoTrackingSummaryListRequestModel()
        request.GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        request.FromDate = fromDate
        request.ToDate = toDate
        return request
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
                recyclerView = rcvGeoTrackingSummary,
                show = show
            )
        }
    }

    override fun onViewButtonListener(position: Int) {
        val intent =
            Intent(this@GeoTrackingListingActivity, GeoTrackingDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(
            Constant.IntentExtras.GEO_TRACKING_ID,
            geoTrackingList[position].AssociateGeoTrackingID
        )
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        callGeoTrackingListApi()
    }
}