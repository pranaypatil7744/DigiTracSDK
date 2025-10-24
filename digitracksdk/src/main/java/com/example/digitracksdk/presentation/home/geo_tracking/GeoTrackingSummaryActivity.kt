package com.example.digitracksdk.presentation.home.geo_tracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log.d
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityGeoTrackingSummaryBinding
import com.example.digitracksdk.databinding.BottomSheetViewAttendanceFilterBinding
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.presentation.home.geo_tracking.adapter.GeoTrackingSummaryAdapter
import com.example.digitracksdk.presentation.home.geo_tracking.model.GeoTrackingModel
import com.example.digitracksdk.presentation.home.geo_tracking.tracking_details.GeoTrackingDetailsActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class GeoTrackingSummaryActivity : BaseActivity() ,
    GeoTrackingSummaryAdapter.GeoTrackingClickManager  {
    lateinit var binding: ActivityGeoTrackingSummaryBinding
    lateinit var bottomSheetViewAttendanceFilterBinding: BottomSheetViewAttendanceFilterBinding
    private lateinit var geoTrackingSummaryAdapter: GeoTrackingSummaryAdapter

    private val geoTrackingSummaryViewModel: GeoTrackingSummaryViewModel by viewModel()

    private var geoTrackingList: ArrayList<GeoTrackingModel> = ArrayList()
    lateinit var preferenceUtils: PreferenceUtils

    var fromDate: String = ""
    var toDate: String = ""
    var i=0
    var selectedFromDate: Calendar? = null
    var currentLatitude: String? = ""
    var currentLongitude: String? = ""
    val notificationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGrant ->
            if (!isGrant && i<3) {
                i++
                requestNotificationPermission()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGeoTrackingSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        locationUtil.launch(Intent(this@GeoTrackingSummaryActivity, LocationUtils::class.java))
        setUpToolbar()
        setUpObserver()
        setUpListener()
        setUpAdapter()
        requestNotificationPermission()
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun setUpAdapter() {
        geoTrackingSummaryAdapter = GeoTrackingSummaryAdapter(this, geoTrackingList ,this)
        binding.recyclerGeoTrackingSummary.adapter = geoTrackingSummaryAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpObserver() {

        binding.apply {
            with(geoTrackingSummaryViewModel) {
                geoTrackingSummaryListResponseData.observe(this@GeoTrackingSummaryActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        if (it.lstGeoTrackingList.isNotEmpty()) {
                            showNoDataLayout(false)
                            geoTrackingList.clear()
                            for (i in it.lstGeoTrackingList) {
                                geoTrackingList.add(
                                    GeoTrackingModel(
                                        GeoTrackingID = i.AssociateGeoTrackingID,
                                        CreatedDate = i.CreatedDate,
                                        TrackingStartDateTime = i.StartDateTime,
                                        TrackingEndDateTime = i.EndDateTime,
                                        TrackingStartLat = i.StartLat,
                                        TrackingStartLon = i.StartLon,
                                        TrackingEndLat = i.EndLat,
                                        TrackingEndLon = i.EndLon,
                                        StartAddress = i.StartAddress,
                                        EndAddress = i.EndAddress
                                    )
                                )

                            }
                            geoTrackingSummaryAdapter.notifyDataSetChanged()
                        } else {
                            binding.layoutNoData.tvNoData.text = it.message
                            showNoDataLayout(true)
                        }
                    } else {
                        showToast(it.message)
                        showNoDataLayout(true)
                    }
                }

                messageData.observe(this@GeoTrackingSummaryActivity) {
                    toggleLoader(false)
                    showToast(it)
                }

            }

        }

    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(binding.root, loader = binding.contentLoading.root, binding.contentLoading.imageLoading, showLoader)
    }
    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root, recyclerView = recyclerGeoTrackingSummary, show = show
            )
        }
    }
    override fun onResume() {
        super.onResume()
        callGeoTrackingListApi()
    }
    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callGeoTrackingListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callGeoTrackingListApi()
            }
            btnStartTracking.setOnClickListener {
//                val intent = Intent(this@GeoTrackingSummaryActivity, GeoTrackingDetailsActivity::class.java)
//                startActivity(intent)
                if (!Settings.canDrawOverlays(this@GeoTrackingSummaryActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    systemAlertLauncher.launch(intent)
                } else {
                    startActivity(Intent(this@GeoTrackingSummaryActivity, GeoTrackingDetailsActivity::class.java))
                }

            }
        }


    }
    private val systemAlertLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(Settings.canDrawOverlays(this@GeoTrackingSummaryActivity)){
                startActivity(Intent(this, GeoTrackingDetailsActivity::class.java))
            }
        }

    private fun callGeoTrackingListApi() {
        with(binding){
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                geoTrackingSummaryViewModel.callGeoTrackingSummaryListApi(
                    requestModel()
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerGeoTrackingSummary.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun requestModel(): GeoTrackingSummaryListRequestModel {
        val request = GeoTrackingSummaryListRequestModel()
        request.GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        request.FromDate = fromDate
        request.ToDate = toDate
        return request

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.tracking_summary)

            divider.visibility = VISIBLE
            btnOne.setImageResource(R.drawable.ic_filter)
            btnOne.visibility = VISIBLE
            btnOne.setOnClickListener {
                openFilterBottomSheet()
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
                    callGeoTrackingListApi()
                    bottomSheetDialog.dismiss()
                }

            }

            etFromDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@GeoTrackingSummaryActivity,
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
                this@GeoTrackingSummaryActivity,
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


    override fun clickOnViewButton(position: Int) {
        val intent = Intent(this, GeoTrackingDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.IntentExtras.EXTRA_LATITUDE, currentLatitude)
        bundle.putString(Constant.IntentExtras.EXTRA_LONGITUDE, currentLongitude)
        bundle.putSerializable(Constant.IntentExtras.GEO_TRACKING_ID ,geoTrackingList[position].GeoTrackingID)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    private val locationUtil =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentLatitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LATITUDE)
                d("MASTER DEBUG Lat ",currentLatitude.toString())
                currentLongitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LONGITUDE)
                d("MASTER DEBUG Lang ",currentLongitude.toString())
            }
        }
}