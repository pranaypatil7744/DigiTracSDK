package com.example.digitracksdk.presentation.attendance.attendance_map

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.developers.imagezipper.ImageZipper
import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusResponseModel
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAttendanceMapBinding
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicRequestModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicResponseModel
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.attendance_camera.AttendanceCameraActivity
import com.example.digitracksdk.presentation.map_screen.MapViewFragment
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PermissionUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class AttendanceMapActivity : BaseActivity(), DialogUtils.DialogManager {
    lateinit var binding: ActivityAttendanceMapBinding
    private var mapViewFragment = MapViewFragment.newInstance()
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private lateinit var bluetoothAdapter : BluetoothAdapter

    lateinit var preferenceUtils: PreferenceUtils
    var attendanceImage: Bitmap? = null
    var innovId: String = ""
    var isCheckOut: Boolean = false
    var currentLatitude: Double? = 0.0
    var currentLongitude: Double? = 0.0
    var attendanceToken: String = ""
    var logVisitToken: String = ""
    var inOrOut: String = "1"
    var switchKey = "1"
    var attendanceDate: String = ""
    var isBecoanApplicable: Int = 0
    var inTimeFromServer:String = ""
    var outTimeFromServer:String = ""
    var isFromLogYourVisit:Boolean = false
    var isCameraAttendance:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        bluetoothAdapter=(getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        setUpToolbar()
        getPreferenceData()
        getIntentData()
        setUpObserver()
        setFragment(mapViewFragment)
        if (!isFromLogYourVisit){
            callCurrentDayAttendanceStatus()
        }else{
            setUpLogYourVisitData()
        }
        setUpListener()
    }


    private fun setUpObserver() {
        binding.apply {
            with(attendanceViewModel) {
                currentDayAttendanceStatusResponseData.observe(this@AttendanceMapActivity
                ) { it ->
                    toggleLoader(false)
                    setUpView(it)
                }

                validateLogVisitTokenResponseData.observe(this@AttendanceMapActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        val resultIntent = Intent()
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        showToast(it.Error.toString())
                    }
                }

                createLogVisitTokenResponseData.observe(this@AttendanceMapActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        logVisitToken = it.LogYourVisitTokenID.toString()
                        captureImage()
                    } else {
                        showToast(it.Error.toString())
                    }
                }

                checkValidAttendanceTokenResponseData.observe(this@AttendanceMapActivity
                ) { it ->
                    // toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        inTimeFromServer = it.InTime.toString()
                        outTimeFromServer = it.OutTime.toString()
                        insertAttendanceImageOnServerApi()
                    } else {
                        toggleLoader(false)
                        showToast(it.Message.toString())
                    }
                }

                createAttendanceTokenResponseData.observe(this@AttendanceMapActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        attendanceToken = it.AttendanceTokenID.toString()
                        isBecoanApplicable = it.IsBecaonApplicable ?: 0
                        if (it.IsBecaonApplicable == 0 && it.GPSAttendanceCriteriaFlag == "F") {
                            showToast(getString(R.string.attendance_input_is_outside_the_zone_please_enter_zone_and_mark_attendance))
                        } else if (it.GPSAttendanceCriteriaFlag == "null") {
                            showToast(getString(R.string.validation_failed_try_again))
                        } else if (isBecoanApplicable == 1 && it.GPSAttendanceCriteriaFlag != "N") {
                            if (!bluetoothAdapter.isEnabled) {
                                if (ActivityCompat.checkSelfPermission(
                                        this@AttendanceMapActivity,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return@observe
                                }
                                bluetoothAdapter.enable()
                            }
                            callBecaonListApi()
                        } else {
                            if (isCameraAttendance == "0") {
                                callCheckAttendanceTokenApi()
                            } else {
                                captureImage()
                            }
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@AttendanceMapActivity
                ) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }
            }
        }
    }

    private fun callBecaonListApi() {

    }

    private fun setUpLogYourVisitData() {
        binding.apply {
            hideViews()
            tvLat.visibility = VISIBLE
            tvLatValue.visibility = VISIBLE
            tvLong.visibility = VISIBLE
            tvLongValue.visibility = VISIBLE
            btnLog.visibility = VISIBLE
            tvLatValue.text = currentLatitude.toString()
            tvLongValue.text = currentLongitude.toString()
        }
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.mapContainer.id, fragment)
        mapViewFragment.currentLatitude = currentLatitude
        mapViewFragment.currentLongitude = currentLongitude
        transaction.commit()
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        isCameraAttendance = preferenceUtils.getValue(Constant.PreferenceKeys.IsAttendanceCamera)
    }

    private fun callCurrentDayAttendanceStatus() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                attendanceViewModel.callCurrentDayAttendanceStatusApi(
                    request = CurrentDayAttendanceStatusRequestModel(
                        InnovID = innovId,
                        attendanceDate = attendanceDate
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            currentLatitude = getString(Constant.IntentExtras.EXTRA_LATITUDE)?.toDoubleOrNull()
            currentLongitude = getString(Constant.IntentExtras.EXTRA_LONGITUDE)?.toDoubleOrNull()
            switchKey = getString(Constant.ATTENDANCE_SWITCH_KEY).toString()
            attendanceDate = getString(Constant.ATTENDANCE_DATE).toString()
            isFromLogYourVisit = getBoolean(Constant.IS_FROM_LOG_YOUR_VISIT,false)
        }
    }

    private fun setUpView(data: CurrentDayAttendanceStatusResponseModel) {
        hideViews()
        binding.apply {
            if (data.InStatus == false) {
                isCheckOut = false
                btnAttendance.setImageResource(R.drawable.ic_attendance_finger)
                inOrOut = "1"

            } else {
                isCheckOut = true
                btnAttendance.setImageResource(R.drawable.ic_attendance_check_out)
                inOrOut = "0"
            }
            tvDay.visibility = VISIBLE
            tvDate.visibility = VISIBLE
            btnAttendance.visibility = VISIBLE
            tvDate.text = attendanceDate.replace("-"," ")
            tvDay.text = data.Day
        }
    }

    private fun hideViews() {
        binding.apply {
            tvDate.visibility = INVISIBLE
            tvDay.visibility = INVISIBLE
            tvLong.visibility = INVISIBLE
            tvLongValue.visibility = INVISIBLE
            tvLat.visibility = INVISIBLE
            tvLatValue.visibility = INVISIBLE
            btnLog.visibility = INVISIBLE
            btnAttendance.visibility = INVISIBLE
        }
    }

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitmapFile =
                    File(url.toString())
                val imageZipperFile = ImageZipper(this).compressToFile(bitmapFile)


             /*   val imageZipperFile = ImageZipper(this@AttendanceMapActivity)
                    .setQuality(10)
                    .setMaxWidth(400)
                    .setMaxHeight(400)
                    .compressToFile(bitmapFile)*/
                attendanceImage = BitmapFactory.decodeFile(imageZipperFile.toString())

                binding.apply {
                    toolbar.appBarLayout.visibility = INVISIBLE
                    layoutViewImage.visibility = VISIBLE
                    imgCandidate.rotation = 180f
                    ImageUtils.INSTANCE?.loadLocalImage(imgCandidate, bitmapFile)
                }
            }
        }

    private val locationUtil =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentLatitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LATITUDE)?.toDoubleOrNull()
                currentLongitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LONGITUDE)?.toDoubleOrNull()

                if (PermissionUtils.getCameraPermission(this)) {
                    preferenceUtils.setValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT, 0)
                    if (isFromLogYourVisit) {
                        callCreateLogVisitTokenApi()
                    } else {
                        callCreateAttendanceTokenApi()
                    }
                } else {
                    PermissionUtils.requestCameraPermissions(this)
                }
            }
        }

    private fun setUpListener() {
        binding.apply {
            btnAttendance.setOnClickListener {

                if (PermissionUtils.getStoragePermission(this@AttendanceMapActivity)) {
                    preferenceUtils.setValue(Constant.AskedPermission.STORAGE_PERMISSION_COUNT, 0)
                    locationUtil.launch(
                        Intent(
                            this@AttendanceMapActivity,
                            LocationUtils::class.java
                        )
                    )
                }else{
                    PermissionUtils.requestStoragePermissions(this@AttendanceMapActivity)
                }
            }
            btnDiscard.setOnClickListener {
                binding.layoutViewImage.visibility = GONE
                binding.toolbar.appBarLayout.visibility = VISIBLE
                attendanceImage = null
            }
            btnSave.setOnClickListener {
                binding.layoutViewImage.visibility = GONE
                binding.toolbar.appBarLayout.visibility = VISIBLE
                if (isFromLogYourVisit){
                    callValidateLogVisitApi()
                }else{
                    callCheckAttendanceTokenApi()
                }
            }
            btnLog.setOnClickListener {
                locationUtil.launch(Intent(this@AttendanceMapActivity, LocationUtils::class.java))
            }
        }
    }

    private fun callValidateLogVisitApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callValidateLogVisitTokenApi(request =
            CheckValidLogYourVisitRequestModel(
                LogYourVisitTokenID = logVisitToken,
                Picture = attendanceImage?.let { ImageUtils.INSTANCE?.bitMapToString(it) }.toString(),
                LogYourVisit = preferenceUtils.getValue(Constant.PreferenceKeys.LogYourVisit)
            )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callCreateLogVisitTokenApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callCreateLogVisitTokenApi(request = getLogVisitTokenRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getLogVisitTokenRequestModel(): CreateLogYourVisitTokenRequestModel {
        val deviceId = AppUtils.INSTANCE?.getDeviceId(this)
        val request = CreateLogYourVisitTokenRequestModel()
        request.AssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID)
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.DeviceID = deviceId.toString()
        request.IMEI = deviceId.toString()
        request.DeviceToken = preferenceUtils.getValue(Constant.PreferenceKeys.FIREBASE_TOKEN)
        request.Latitude = currentLatitude.toString()
        request.Longitude = currentLongitude.toString()
        request.Distance = "5"
        request.LogYourVisit = preferenceUtils.getValue(Constant.PreferenceKeys.LogYourVisit)
        return request
    }
    private fun callCheckAttendanceTokenApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callCheckValidAttendanceTokenApi(
                request = CheckValidAttendanceTokenRequestModel(
                    attendanceToken
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PermissionRequestCodes.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                if (isFromLogYourVisit) {
                    callCreateLogVisitTokenApi()
                } else {
                    callCreateAttendanceTokenApi()
                }
            } else {
                val cameraPerCount =
                    preferenceUtils.getIntValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT, 0)
                preferenceUtils.setValue(
                    Constant.AskedPermission.CAMERA_PERMISSION_COUNT,
                    cameraPerCount.plus(1)
                )
                if (preferenceUtils.getIntValue(
                        Constant.AskedPermission.CAMERA_PERMISSION_COUNT,
                        0
                    ) >= 2
                ) {
                    DialogUtils.showPermissionDialog(
                        this,
                        getString(R.string.please_grant_the_camera_permission_to_continue),
                        getString(R.string.allow_permission),
                        getString(R.string.go_to_settings),
                        getString(R.string.deny),isFinish = false
                    )
                } else {
                    AppUtils.INSTANCE?.showLongToast(
                        this,
                        getString(R.string.please_grant_the_camera_permission_to_continue)
                    )
                }
                setResult(Activity.RESULT_CANCELED, intent)
            }
        }
    }

    private fun insertAttendanceImageOnServerApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callInsertAttendancePicApi(
                request = InsertAttendancePicRequestModel(
                    InnovID = innovId,
                    AttendanceDate = attendanceDate,
                    InTime = inTimeFromServer,
                    OutTime = outTimeFromServer,
                    Picture = attendanceImage?.let { ImageUtils.INSTANCE?.bitMapToString(it) }
                        .toString()
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
        with(attendanceViewModel) {
            insertAttendancePicResponseData.observe(this@AttendanceMapActivity,
                object : Observer<InsertAttendancePicResponseModel> {
                    override fun onChanged(it: InsertAttendancePicResponseModel) {
                        toggleLoader(false)
                        insertAttendancePicResponseData.removeObserver(this)
                        if (it.Status == Constant.UPDATED) {
                            openSuccessDialog()
                        } else {
                            showToast(it.Message.toString())
                        }
                    }
                })
            messageData.observe(this@AttendanceMapActivity,
                object : Observer<String> {
                override fun onChanged(t: String) {
                    toggleLoader(false)
                    messageData.removeObserver(this)
                    showToast(t.toString())
                }

            })
        }
    }

    private fun captureImage() {
        val intent = Intent(this, AttendanceCameraActivity::class.java)
        val b = Bundle()
        b.putBoolean(Constant.IS_CAMERA, true)
        b.putBoolean(Constant.IS_SELFIE_CAMERA,true)
        intent.putExtras(b)
        addImageUtils.launch(intent)
    }

    private fun openSuccessDialog() {
        if (isCheckOut) {
            DialogUtils.showAttendanceCheckInDialog(
                this, this, getString(R.string.successfully_checked_out),
                R.drawable.ic_attendance_check_out
            )
        } else {
            DialogUtils.showAttendanceCheckInDialog(
                this, this, getString(R.string.successfully_checked_in),
                R.drawable.ic_attendance_finger
            )
        }
    }

    private fun getCreateAttendanceTokenRequestModel(): CreateAttendanceTokenRequestModel {
        //TODO update hard coded values
        val deviceId = AppUtils.INSTANCE?.getDeviceId(this)
        val request = CreateAttendanceTokenRequestModel()
        request.InnovID = innovId
        request.Distance = "5"
        request.Latitude = currentLatitude.toString()
        request.Longitude = currentLongitude.toString()
        request.InorOut = inOrOut
        request.AttendanceFromAnyWhereValue = switchKey
        request.DeviceID = deviceId.toString()
        request.IMEI = deviceId.toString()
        request.DeviceToken = preferenceUtils.getValue(Constant.PreferenceKeys.FIREBASE_TOKEN)
        request.attendanceDate = attendanceDate
        request.attendanceFlag = preferenceUtils.getValue(Constant.PreferenceKeys.IN_OUT_FLAG)
        return request
    }

    private fun callCreateAttendanceTokenApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            attendanceViewModel.callCreateAttendanceTokenApi(
                request = getCreateAttendanceTokenRequestModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
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

    override fun onOkClick() {
        super.onOkClick()
        finish()
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }
}