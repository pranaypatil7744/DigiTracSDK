package com.example.digitracksdk.presentation.attendance

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAttendanceBinding
import com.example.digitracksdk.databinding.BottomSheetYourVisitMapBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_model.DashboardAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.LogDataListModel
import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitRequestModel
import com.example.digitracksdk.presentation.attendance.adapter.AttendanceAdapter
import com.example.digitracksdk.presentation.attendance.adapter.LogVisitsAdapter
import com.example.digitracksdk.presentation.attendance.attendance_map.AttendanceMapActivity
import com.example.digitracksdk.presentation.attendance.model.AttendanceListModel
import com.example.digitracksdk.presentation.attendance.model.AttendanceStatus
import com.example.digitracksdk.presentation.attendance.model.AttendanceType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagRequestModel
import com.example.digitracksdk.utils.PermissionUtils
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkRequestModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : BaseActivity(),
    AttendanceManagerListener, LogVisitsAdapter.VisitClickManager {
    lateinit var binding: ActivityAttendanceBinding
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private lateinit var gMap: GoogleMap
    lateinit var markerOptions: MarkerOptions
    lateinit var logVisitBottomSheetDialog: BottomSheetDialog
    lateinit var marker: Marker
    private var logYourVisitList: ArrayList<LogDataListModel> = ArrayList()
    private lateinit var bottomSheetLogYourVisitMapBinding: BottomSheetYourVisitMapBinding
    private lateinit var attendanceAdapter: AttendanceAdapter

    lateinit var logVisitsAdapter: LogVisitsAdapter

    var attendanceStatus: AttendanceStatus = AttendanceStatus.CHECK_OUT
    var itemPosition: Int = 0
    var innovId: String = ""
    var associateId: String = ""
    var gnetAssociateId: String = ""
    var currentLatitude: String? = ""
    var currentLongitude: String? = ""
    var currentDate: String = ""
    var geoLat: String = ""
    var geoLong: String = ""
    var radius: Int = 0
    var currentServerTime: String = ""
    var attendanceDate: String = ""
    var logVisitDate: String = ""
    private var attendanceList: ArrayList<AttendanceListModel> = ArrayList()
    var checkInLatLng: LatLng? = null
    var checkOutLatLng: LatLng? = null
    var from = ""
    var switchIsAttendanceAnywhere: Boolean = false

    //for timer
    var mills: Long = 0
    var hours = 0
    var mins: Int = 0
    var seconds: Int = 0
    private var finalMin: String = ""
    private var finalSec: String = ""
    private var finalHrs: String = ""
    var handler: Handler? = null
    var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpIntentData()
        setUpToolbar()
        setUpObserver()
        setUpAttendanceData()
        locationUtil.launch(Intent(this, LocationUtils::class.java))
    }

    private fun setUpIntentData() {
        intent?.extras?.let {
            from = it.getString("from", "")
            if (from == getString(R.string.attendance)) {

            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(attendanceViewModel) {
                attendanceGeoFancingResponseData.observe(this@AttendanceActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.success) {
                        geoLat = it.Latitude.toString()
                        geoLong = it.Longitude.toString()
                        radius = it.Radius ?: 0
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                attendanceMarkResponseData.observe(this@AttendanceActivity) {
                    toggleLoader(false)
                    if (it.Status != null && it.Status == Constant.SUCCESS) {
//                        callStatusApi()
                        callDashboardAttendanceApi()
                        AppUtils.INSTANCE?.logMe(
                            "MASTER DEBUG",
                            "Status Method Call from mark attendance"
                        )
                    }
                }
                attendanceFlagResponseData.observe(this@AttendanceActivity) {
                    toggleLoader(false)

                    it?.let {
                        attendanceList[0].attendanceStatus = when (it.AttendanceFlag ?: 2) {
                            0 -> {
                                AppUtils.INSTANCE?.logMe("MASTER DEBUG", "Check IN")
                                AttendanceStatus.NONE

                            }

                            1 -> {
                                AppUtils.INSTANCE?.logMe("MASTER DEBUG", "Check OUT")

                                AttendanceStatus.CHECK_IN
                            }

                            else -> AttendanceStatus.CHECK_OUT
                        }
                        attendanceAdapter.notifyDataSetChanged()
                        preferenceUtils.setValue(
                            Constant.PreferenceKeys.IN_OUT_FLAG,
                            it.AttendanceFlag!!
                        )
                    }
                }
                dashboardAttendanceStatusResponseData.observe(this@AttendanceActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        setUpAttendanceDashboardUi(it)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                logYourVisitResponseData.observe(
                    this@AttendanceActivity
                ) { it ->
                    toggleLoaderLogYourVisit(false)
                    if (::bottomSheetLogYourVisitMapBinding.isInitialized) {
                        bottomSheetLogYourVisitMapBinding.apply {
                            tvInTimeValue.text = "--:--"
                            tvOutTimeValue.text = "--:--"
                            dividerHorizontal.visibility = GONE
                        }
                    }
                    logYourVisitList.clear()
                    logVisitsAdapter.notifyDataSetChanged()
                    gMap.clear()
                    if (it.Status == Constant.success) {
                        if (!it.LogDataList.isNullOrEmpty()) {
                            setUpLogVisitMapData(it.LogDataList ?: arrayListOf())
                        } else {
                            showToast(it.Message.toString())
                        }
                    } else if (it.Status == Constant.EMPTY) {
                        showToast(getString(R.string.visit_records_are_not_available))
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                attendanceValidationResponseData.observe(
                    this@AttendanceActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        val b = Bundle()
                        val i =
                            Intent(this@AttendanceActivity, AttendanceMapActivity::class.java)
                        b.putString(Constant.IntentExtras.EXTRA_LATITUDE, currentLatitude)
                        b.putString(Constant.IntentExtras.EXTRA_LONGITUDE, currentLongitude)
                        b.putString(Constant.IN_TIME, attendanceList[0].checkInTime)
                        b.putString(
                            Constant.ATTENDANCE_SWITCH_KEY,
                            if (attendanceList[0].isAttendanceAnywhere == true) "1" else "0"
                        )
                        b.putString(Constant.ATTENDANCE_DATE, attendanceDate)
                        i.putExtras(b)
                        startActivity(i)

                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@AttendanceActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callGeoFancingApi()
        callDashboardAttendanceApi()

    }

    private fun callDashboardAttendanceApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callDashboardAttendanceStatusApi(
                request = CommonRequestModel(
                    InnovId = innovId
                )
            )
        } else {
            toggleLoader(false)
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpAttendanceDashboardUi(it: DashboardAttendanceStatusResponseModel) {
        currentServerTime = it.CurrentServerTime.toString()
        attendanceStatus = AppUtils.INSTANCE?.getAttendanceStatus(
            it.InTime.toString(),
            it.OutTime.toString()
        ) ?: AttendanceStatus.CHECK_OUT
        if (currentServerTime.isNotEmpty()) {
            setTimer()
        }
        if (from != getString(R.string.attendance)) {
            val inOrOut = when (attendanceStatus) {
                AttendanceStatus.CHECK_IN -> {
                    "0"
                }

                AttendanceStatus.CHECK_OUT -> {
                    "1"
                }

                else -> "1"
            }
            preferenceUtils.setValue(Constant.PreferenceKeys.IN_OUT_FLAG, inOrOut)
        }
        val list = it.AttendanceDateCollection?.find {
            it.IsPreviousDay == "N"
        }
        attendanceDate =
            list?.AttendanceDate?.toString() ?: AppUtils.INSTANCE?.getCurrentDate().toString()
        logVisitDate = attendanceDate
        attendanceList[0].attendanceStatus = attendanceStatus
        attendanceList[0].checkInTime = it.InTime
        attendanceList[0].checkOutTime = it.OutTime
        attendanceList[0].workingHrs = it.Tolworkinghrs
        attendanceList[0].dateDay =
            AppUtils.INSTANCE?.convertDateFormat("dd-MMM-yyyy", attendanceDate, "EEEE,MMMM dd,yyyy")
        attendanceAdapter.notifyItemChanged(0)
        if (from == getString(R.string.attendance))
            callStatusApi()
        AppUtils.INSTANCE?.logMe("MASTER DEBUG", "Status Method Call from dashboard")
    }

    private fun callStatusApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callAttendanceFlagApi(
                request = AttendanceFlagRequestModel(
                    GnetAssociateId = gnetAssociateId,
                    Attendancedate = attendanceDate.trim().replace(" ", "-")
                )
            )
        }
    }

    private fun setTimer() {
        mills = 0
        val format1 = SimpleDateFormat("hh:mm:ssaa", Locale.ENGLISH)
        var dateClientOutTime: Date? = null
        try {
            format1.timeZone = TimeZone.getTimeZone("GMT")
            dateClientOutTime = format1.parse(currentServerTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        mills = dateClientOutTime!!.time
        hours = (mills / (1000 * 60 * 60)).toInt()
        mins = (mills / (1000 * 60)).toInt() % 60
        seconds = (mills / 1000).toInt() % 60
        if (handler != null) {
            handler?.removeMessages(0)
            runnable?.let { handler?.removeCallbacks(it) }
            handler = null
            runnable = null
        }
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                doWork()
                handler?.postDelayed(this, 1000)
            }
        }
        handler?.postDelayed(runnable as Runnable, 1000)
    }

    private fun doWork() {
        try {
            if (seconds == 60) {
                seconds = 0
                mins++
                if (mins == 60) {
                    mins = 0
                    hours++
                }
                if (hours == 24) {
                    hours = 0
                }
            }
            getInnovTime()
            seconds++
        } catch (e: Exception) {
        }
    }

    private fun getInnovTime() {
        val min: String = mins.toString()
        val sec = seconds.toString()
        val hours: String = hours.toString()
        finalMin = if (min.length != 2) {
            "0$min"
        } else {
            min
        }
        finalSec = if (sec.length != 2) {
            "0$sec"
        } else {
            sec
        }
        finalHrs = if (hours.length != 2) {
            "0$hours"
        } else {
            hours
        }
        attendanceList[0].time = "$finalHrs:$finalMin:$finalSec"
        attendanceAdapter.notifyItemChanged(0)
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
        associateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        switchIsAttendanceAnywhere =
            preferenceUtils.getValue(Constant.PreferenceKeys.AttendanceFromAnyWhere, false)
    }

    private fun callGeoFancingApi() {
        if (isNetworkAvailable()) {
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

    private fun setUpAttendanceData() {
        currentDate = AppUtils.INSTANCE?.getCurrentDate().toString()
        attendanceList.clear()
        attendanceList.add(
            AttendanceListModel(
                attendanceType = AttendanceType.ATTENDANCE_TOP_ITEM,
                attendanceStatus = AttendanceStatus.NONE,
                time = "00:00:00",
                dateDay = currentDate,
                isAttendanceAnywhere = switchIsAttendanceAnywhere
            )
        )
//        attendanceList.add(
//            AttendanceListModel(
//                attendanceType = AttendanceType.ATTENDANCE_ITEM_LIST,
//                attendanceItemName = getString(R.string.time_sheet),
//                attendanceItemIcon = R.drawable.ic_time_sheet
//            )
//        )
        if (from != getString(R.string.attendance))
            attendanceList.add(
                AttendanceListModel(
                    attendanceType = AttendanceType.ATTENDANCE_ITEM_LIST,
                    attendanceItemName = getString(R.string.log_your_visit),
                    attendanceItemIcon = R.drawable.ic_log_visit
                )
            )

        setUpAttendanceAdapter()
    }

    private fun setUpAttendanceAdapter() {
        attendanceAdapter = AttendanceAdapter(this, attendanceList, this)
        binding.recyclerAttendance.adapter = attendanceAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.attendance)
        }
    }

    @SuppressLint("MissingPermission")
    private fun openLogYourVisitBottomSheet() {
        logVisitBottomSheetDialog = BottomSheetDialog(this)
        bottomSheetLogYourVisitMapBinding =
            BottomSheetYourVisitMapBinding.inflate(LayoutInflater.from(this))
        logVisitBottomSheetDialog.setContentView(bottomSheetLogYourVisitMapBinding.root)
        logVisitBottomSheetDialog.setCancelable(false)
        logVisitsAdapter = LogVisitsAdapter(this, logYourVisitList, this)
//        setUpBottomSheetObserver()
        bottomSheetLogYourVisitMapBinding.apply {
            tvCurrentMonthYear.text = attendanceDate.replace("-", " ")
            recyclerVisits.adapter = logVisitsAdapter
            logVisitDate = attendanceDate
            mapView.onCreate(logVisitBottomSheetDialog.onSaveInstanceState())
            mapView.onResume()
            mapView.getMapAsync { map ->
                map.let {
                    gMap = map
                    gMap.isMyLocationEnabled = true
                    gMap.uiSettings.isMyLocationButtonEnabled = true
                    val latLng = LatLng(
                        currentLatitude?.toDouble() ?: 0.0,
                        currentLongitude?.toDouble() ?: 0.0
                    )
                    gMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng, 13f
                        )
                    )
                    val cameraPosition = CameraPosition.Builder()
                        .target(
                            latLng
                        ) // Sets the center of the map to location user
                        .zoom(15f) // Sets the zoom
                        .bearing(90f) // Sets the orientation of the camera to east
                        .tilt(40f) // Sets the tilt of the camera to 30 degrees
                        .build() // Creates a CameraPosition from the builder

                    gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }
            btnClose.setOnClickListener {
                logVisitBottomSheetDialog.dismiss()
//                gMap.clear()
//                marker.remove()
//                tvInTimeValue.text = ""
//                tvOutTimeValue.text = ""
            }
            fab.setOnClickListener {
                when (attendanceStatus) {
                    AttendanceStatus.NONE -> {
                        showToast(getString(R.string.please_mark_in_attendance_first))
                    }

                    AttendanceStatus.CHECK_OUT -> {
                        showToast(getString(R.string.you_have_completed_the_attendance))
                    }

                    else -> {
                        val i = Intent(this@AttendanceActivity, AttendanceMapActivity::class.java)
                        val b = Bundle()
                        b.putBoolean(Constant.IS_FROM_LOG_YOUR_VISIT, true)
                        b.putString(Constant.IntentExtras.EXTRA_LATITUDE, currentLatitude)
                        b.putString(Constant.IntentExtras.EXTRA_LONGITUDE, currentLongitude)
                        b.putString(
                            Constant.ATTENDANCE_SWITCH_KEY,
                            if (attendanceList[0].isAttendanceAnywhere == true) "1" else "0"
                        )
                        b.putString(Constant.ATTENDANCE_DATE, attendanceDate)
                        i.putExtras(b)
                        logVisitResult.launch(i)
                    }
                }
            }
            tvCurrentMonthYear.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@AttendanceActivity,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd-MMM-yyyy"
                            )
                        tvCurrentMonthYear.text = AppUtils.INSTANCE?.convertDateFormat(
                            "dd-MMM-yyyy",
                            selectedDate.toString(),
                            "dd MMM yyyy"
                        )
                        logVisitDate = selectedDate.toString()

                        val currentDate = AppUtils.INSTANCE?.getCurrentDate()
                        if (tvCurrentMonthYear.text.toString() == currentDate) {
                            fab.visibility = VISIBLE
                        } else {
                            fab.visibility = INVISIBLE
                        }
                        callLogYourVisitApi()
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }

            tvInTimeValue.setOnClickListener {
                showCheckInView()
            }
            tvInTime.setOnClickListener {
                showCheckInView()
            }
            tvOutTimeValue.setOnClickListener {
                showCheckOutView()
            }
            tvOutTime.setOnClickListener {
                showCheckOutView()
            }

        }
        callLogYourVisitApi()
        logVisitBottomSheetDialog.show()
    }

    private fun showCheckInView() {
        bottomSheetLogYourVisitMapBinding.apply {
            if (tvInTimeValue.text.toString() != "--:--") {
                gMap.clear()
                val iconIn: BitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.log_visit_in_big)
                checkInLatLng?.let {
                    markerOptions = MarkerOptions().position(it)
                        .title(getString(R.string.visit) + "1")
                        .icon(iconIn)
                        .snippet("@" + tvInTimeValue.text.toString())
                    gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
                }

                if (tvOutTimeValue.text.toString() != "--:--") {
                    val iconOut: BitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.log_visit_out_small)
                    checkOutLatLng?.let {
                        markerOptions = MarkerOptions().position(it)
                            .title(getString(R.string.visit) + "2")
                            .icon(iconOut)
                            .snippet("@" + tvOutTimeValue.text.toString())
                        gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
                    }

                }
                for (i in logYourVisitList) {
                    val visitLatLng =
                        LatLng(i.Latitude?.toDouble() ?: 0.0, i.Longitude?.toDouble() ?: 0.0)
                    val icon: BitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.visit_marker)
                    markerOptions = MarkerOptions().position(visitLatLng)
                        .title(getString(R.string.visit) + i.SrNo)
                        .icon(icon)
                        .snippet("@ ${i.LogTime?.split(".")?.first()}")
                    gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
                }

                checkInLatLng?.let { it1 -> setZoomMap(it1) }
            }
        }
    }

    private fun showCheckOutView() {
        bottomSheetLogYourVisitMapBinding.apply {
            if (tvOutTimeValue.text.toString() != "--:--") {
                gMap.clear()

                val iconIn: BitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.log_visit_in_small)
                checkInLatLng?.let {
                    markerOptions = MarkerOptions().position(it)
                        .title(getString(R.string.visit) + "1")
                        .icon(iconIn)
                        .snippet("@" + tvInTimeValue.text.toString())
                    gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
                }


                val iconOut: BitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.log_visit_out_big)
                checkOutLatLng?.let {
                    markerOptions = MarkerOptions().position(it)
                        .title(getString(R.string.visit) + "2")
                        .icon(iconOut)
                        .snippet("@" + tvOutTimeValue.text.toString())
                    gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
                }


                for (i in logYourVisitList) {
                    val visitLatLng =
                        LatLng(i.Latitude?.toDouble() ?: 0.0, i.Longitude?.toDouble() ?: 0.0)
                    val icon: BitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.visit_marker)
                    markerOptions = MarkerOptions().position(visitLatLng)
                        .title(getString(R.string.visit) + i.SrNo)
                        .icon(icon)
                        .snippet("@ ${i.LogTime?.split(".")?.first()}")
                    gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
                }

                checkOutLatLng?.let { it1 -> setZoomMap(it1) }
            }
        }
    }

    /// Zoom Map
    private fun setZoomMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to location user
            .zoom(11f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun callLogYourVisitApi() {
        bottomSheetLogYourVisitMapBinding.apply {
            if (isNetworkAvailable()) {
                toggleLoaderLogYourVisit(true)
                attendanceViewModel.callLogYourVisitApi(
                    request = LogYourVisitRequestModel(
                        associateId,
                        logVisitDate
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun setUpLogVisitMapData(data: ArrayList<LogDataListModel>) {
        val listIn = data.find {
            it.Type == Constant.IN
        }
        val listOut = data.find {
            it.Type == Constant.OUT
        }

        val visits = data.filter {
            it.Type == Constant.LYV
        }
        if (!visits.isEmpty()) {
            logYourVisitList.clear()
            logYourVisitList.addAll(visits)
            bottomSheetLogYourVisitMapBinding.dividerHorizontal.visibility = VISIBLE
            logVisitsAdapter.notifyDataSetChanged()
            for (i in visits) {
                val visitLatLng =
                    LatLng(i.Latitude?.toDouble() ?: 0.0, i.Longitude?.toDouble() ?: 0.0)
                val icon: BitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.drawable.visit_marker)
                markerOptions = MarkerOptions().position(visitLatLng)
                    .title(getString(R.string.visit) + i.SrNo)
                    .icon(icon)
                    .snippet("@ ${i.LogTime?.split(".")?.first()}")
                gMap.addMarker(markerOptions)?.let { marker = it }
            }
        }

        if (listIn != null) {
            checkInLatLng =
                LatLng(listIn.Latitude?.toDouble() ?: 0.0, listIn.Longitude?.toDouble() ?: 0.0)
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.log_visit_in_big)
            checkInLatLng?.let {
                markerOptions = MarkerOptions().position(it)
                    .title(getString(R.string.visit) + listIn.SrNo)
                    .icon(icon)
                    .snippet("@ ${listIn.LogTime?.split(".")?.first()}")
                gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
            }

            if (!listIn.LogTime.isNullOrEmpty()) {
                bottomSheetLogYourVisitMapBinding.tvInTimeValue.text =
                    listIn.LogTime?.split(".")?.first()
            } else {
                bottomSheetLogYourVisitMapBinding.tvInTimeValue.text = "--:--"
            }
        }
        if (listOut != null) {
            checkOutLatLng =
                LatLng(listOut.Latitude?.toDouble() ?: 0.0, listOut.Longitude?.toDouble() ?: 0.0)
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.log_visit_out_small)
            checkOutLatLng?.let {
                markerOptions = MarkerOptions().position(it)
                    .title(getString(R.string.visit) + listOut.SrNo)
                    .icon(icon)
                    .snippet("@ ${listOut.LogTime?.split(".")?.first()}")
                gMap.addMarker(markerOptions)?.let { mark -> marker = mark }
            }

            if (!listOut.LogTime.isNullOrEmpty()) {
                bottomSheetLogYourVisitMapBinding.tvOutTimeValue.text =
                    listOut.LogTime?.split(".")?.first()
            } else {
                bottomSheetLogYourVisitMapBinding.tvOutTimeValue.text = "--:--"
            }
        }
        val currentLatLong =
            LatLng(currentLatitude?.toDouble() ?: 0.0, currentLongitude?.toDouble() ?: 0.0)
        setZoomMap(currentLatLong)
    }

    override fun clickOnAttendance(position: Int) {

        if (PermissionUtils.getStoragePermission(this@AttendanceActivity)) {
            itemPosition = position
            if (currentLatitude?.isNotEmpty() == true && currentLongitude?.isNotEmpty() == true) {
                if (geoLat != "0" || geoLong != "0") {
                    if (checkIsLocationValid()) {
                        if (from == getString(R.string.attendance))
                            callMarkAttendanceApi()
                        else
                            callAttendanceValidationApi()
                    } else {
                        showToast(getString(R.string.your_location_not_valid_for_attendance))
                    }
                } else {
                    if (from == getString(R.string.attendance))
                        callMarkAttendanceApi()
                    else
                        callAttendanceValidationApi()
                }
            } else {
                locationUtil.launch(Intent(this, LocationUtils::class.java))
            }
        } else {
            PermissionUtils.requestStoragePermissions(this@AttendanceActivity)
        }
    }

    private fun callMarkAttendanceApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            val date = AppUtils.INSTANCE?.convertDateFormat(
                "EEEE,MMMM dd,yyyy",
                attendanceList[0].dateDay ?: "",
                "dd-MMM-yyyy"
            )
            attendanceViewModel.callAttendanceMarkApi(
                AttendanceMarkRequestModel(
                    Attendancedate = date,
                    GnetAssociateId = gnetAssociateId,
                    Latitude = currentLatitude,
                    Longitude = currentLongitude,
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callAttendanceValidationApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callAttendanceValidationApi(
                request = AttendanceValidationRequestModel(
                    FromDate = currentDate,
                    GNETAssociateID = gnetAssociateId,
                    InnovID = innovId,
                    ToDate = currentDate,
                    Type = "AT"
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private val logVisitResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                callLogYourVisitApi()
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

    private fun toggleLoaderLogYourVisit(showLoader: Boolean) {
        toggleFadeView(
            bottomSheetLogYourVisitMapBinding.root,
            bottomSheetLogYourVisitMapBinding.contentLoading.root,
            bottomSheetLogYourVisitMapBinding.contentLoading.imageLoading,
            showLoader
        )
    }

    override fun clickOnAttendanceItem(position: Int) {
        when (attendanceList[position].attendanceItemName) {

            getString(R.string.log_your_visit) -> {
                if (currentLatitude?.isNotEmpty() == true && currentLongitude?.isNotEmpty() == true) {
                    openLogYourVisitBottomSheet()
                } else {
                    locationUtil.launch(Intent(this, LocationUtils::class.java))
                }
            }
        }
    }

    override fun clickOnSwitch(isCheck: Boolean) {
        attendanceList[0].isAttendanceAnywhere = isCheck
    }

    override fun clickOnVisit(position: Int) {
        val data = logYourVisitList[position]
        val currentLatLong =
            LatLng(data.Latitude?.toDouble() ?: 0.0, data.Longitude?.toDouble() ?: 0.0)
        setZoomMap(currentLatLong)
    }

}