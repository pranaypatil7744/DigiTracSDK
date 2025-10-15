package com.example.digitracksdk.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.data.database.GeoTrackingDataModel
import com.example.digitracksdk.data.source.remote.RetrofitService
import com.innov.digitrac.databinding.DialogSystemAlertWindowBinding
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingUpdateRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingUpdateResponseModel
import com.example.digitracksdk.presentation.home.HomeActivity
import com.example.digitracksdk.presentation.home.geo_tracking.tracking_details.GeoTrackingDetailsActivity
import com.example.digitracksdk.presentation.walkthrough.SplashScreenActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import io.reactivex.rxjava3.core.Emitter
import io.reactivex.rxjava3.core.Flowable
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.UUID


/**
 * Created by Mo Khurseed Ansari on 21-Jul-2022.
 */
class LocationUpdateService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "notification_location"
    private val SERVICE_LOCATION_REQUEST_CODE = 0
    private val LOCATION_SERVICE_NOTIF_ID = 2
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var flagStopService = false
    private var myTask: TimerTask? = null
    var stringLatitude: String? = null
    var stringLongitude: String? = null
    private var myTimer = Timer()
    private var timeInterval = 1
    private var requestAssociateGeoTrackingID = ""
    lateinit var preferenceUtils: PreferenceUtils
    var isOkPressedTime: Long = 0L
    var isSystemAlertWindowAdded: Boolean = false
    var myNotificationTask: TimerTask? = null
    var myNotificationTimer = Timer()
    var isLastLocationStatusOn: Boolean = true
    override fun onCreate() {
        super.onCreate()
        preferenceUtils = PreferenceUtils(this)
        initData()
    }


    private fun initData() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            UPDATE_INTERVAL_IN_MILLISECONDS
        )
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(0)
            .setMaxUpdateDelayMillis(UPDATE_INTERVAL_IN_MILLISECONDS)
            .build()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action.equals(
                ACTION_STOP_FOREGROUND_SERVICE,
                ignoreCase = true
            )
        ) {
            if (!isNetworkAvailable()) {
                Toast.makeText(
                    this,
                    getString(R.string.there_is_a_network_issue_please_check_the_proper_network_connectivity),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                flagStopService = true
                insertLatLong()
                myTimer.cancel()
                myNotificationTimer.cancel()
                stopForegroundService()
            }
        } else {
            flagStopService = false
            startLocationUpdates()
            prepareForegroundNotification(isFirstTime = true)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                insertLatLong()
            }, 100)
        }
        return START_STICKY
    }

    private fun updateNotificationForLocationServicesDisabled() {
        prepareForegroundNotification(isLocationOff = true, isNotificationTextUpdate = true)
    }

    private fun prepareForegroundNotification(
        isLocationOff: Boolean = false,
        isFirstTime: Boolean = false,
        isNotificationTextUpdate: Boolean = false
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.location_service_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
        var resultIntent = Intent(this, SplashScreenActivity::class.java)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(SplashScreenActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        stackBuilder.addNextIntent(resultIntent)
        resultIntent = Intent(this, HomeActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        resultIntent = Intent(this, GeoTrackingDetailsActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                stackBuilder.getPendingIntent(
                    SERVICE_LOCATION_REQUEST_CODE,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                stackBuilder.getPendingIntent(
                    SERVICE_LOCATION_REQUEST_CODE,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        val notification: Notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentTitle(getString(R.string.innov_geo_tracking))
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentText(
                if (isLocationOff) getString(R.string.please_enable_the_location) else getString(
                    R.string.innov_geo_tracking
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.appicon)
            .setContentIntent(pendingIntent)
            .build()

//        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
        if (isNotificationTextUpdate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getSystemService(NotificationManager::class.java).notify(
                    LOCATION_SERVICE_NOTIF_ID,
                    notification
                )
            }
        } else if (isFirstTime)
        startForeground(LOCATION_SERVICE_NOTIF_ID, notification)


    }
    private fun openSystemWindowAlert() {
        isOkPressedTime = 0L
        val binding = DialogSystemAlertWindowBinding.inflate(LayoutInflater.from(this))
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        binding.apply {
            tvOk.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    windowManager.removeView(binding.root)
                }
                startActivity(Intent(
                    this@LocationUpdateService,
                    LocationUtils::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
                isOkPressedTime = System.currentTimeMillis()
                isSystemAlertWindowAdded = false
            }
            tvDeny.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    windowManager.removeView(binding.root)
                }
                isSystemAlertWindowAdded = false
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            windowManager.addView(binding.root, params)
        }

        isSystemAlertWindowAdded = true
    }

    private fun insertLatLong() {
        if (stringLatitude != null && stringLongitude != null) {
            Realm.getDefaultInstance().use { dataBase ->
                try {
                    val timeStamp =
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.ENGLISH
                        ).format(Calendar.getInstance().time)

                    dataBase.executeTransaction { r: Realm ->
                        val dataModel = r.createObject(
                            GeoTrackingDataModel::class.java,
                            UUID.randomUUID().toString()
                        )
                        dataModel.trackingLatitude = stringLatitude
                        dataModel.trackingLongitude = stringLongitude
                        dataModel.trackingTimeStamp = timeStamp
                        dataBase.insertOrUpdate(dataModel)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    dataBase.close()
                }

            }
            getDataFromDB()
        }
    }

    private fun getDataFromDB() {

        Realm.getDefaultInstance().use { dataBase ->
            try {
                if (isNetworkAvailable()) {
                    dataBase.executeTransaction { realm ->
                        val query = realm.where(GeoTrackingDataModel::class.java).findAll()
                        callRX(realm.copyFromRealm(query)).blockingForEach { booleanEmitted ->
                            if (booleanEmitted) {
                                dataBase.delete(GeoTrackingDataModel::class.java)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                dataBase.close()
            }
        }
    }


    private fun callRX(arrayListLatLong: List<GeoTrackingDataModel> = arrayListOf()): Flowable<Boolean> {
        return Flowable.generate { booleanEmitter: Emitter<Boolean> ->
            try {
                for (geoTrackingDataModel in arrayListLatLong) {
                    val request = GeoTrackingUpdateRequestModel()
                    request.GNETAssociateID =
                        preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
                    if (flagStopService && arrayListLatLong.indexOf(geoTrackingDataModel) == arrayListLatLong.size.minus(
                            1
                        )
                    ) {
                        request.InputType = "End"
                    } else {
                        val geoTrackingID =
                            preferenceUtils.getValue(Constant.PreferenceKeys.GeoTrackingID)
                        if (geoTrackingID.isEmpty()) {
                            request.InputType = "Start"
                        } else {
                            request.InputType = ""
                        }
                    }
                    request.Latitude = geoTrackingDataModel.trackingLatitude
                    request.Longitude = geoTrackingDataModel.trackingLongitude
                    request.GeotrackingDateTime = geoTrackingDataModel.trackingTimeStamp
                    request.AssociateGeoTrackingID = requestAssociateGeoTrackingID
                    request.VehicleType = getString(R.string.two_wheeler)
                    if (!geoTrackingDataModel.trackingLatitude.isNullOrEmpty()) {
                        request.Address = getAddress(
                            geoTrackingDataModel.trackingLatitude?.toDouble() ?: 0.0,
                            geoTrackingDataModel.trackingLongitude?.toDouble() ?: 0.0
                        )
                    }
                    if (!isNotificationActive(LOCATION_SERVICE_NOTIF_ID)) {
                        prepareForegroundNotification(  isFirstTime = true)
                    }
                    val service = RetrofitService.create()
                    val data: GeoTrackingUpdateResponseModel =
                        service.callGeoTrackingUpdateApi(request)?.blockingSingle()!!
                    if (data.Status?.lowercase() == Constant.success) {
                        if (data.Timeinterval != null) {
                            timeInterval = data.Timeinterval?.toInt() ?: 0
                        }
                        if (data.AssociateGeoTrackingID?.isNotEmpty() == true) {
                            requestAssociateGeoTrackingID = data.AssociateGeoTrackingID ?: ""
                            preferenceUtils.setValue(
                                Constant.PreferenceKeys.GeoTrackingID,
                                requestAssociateGeoTrackingID
                            )
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed(
                                {
                                    startTimer()
                                },
                                (timeInterval * (60 * 1000)).toLong()
                            )

                            startNotificationTimer()
                        }
                    } else {
                        AppUtils.INSTANCE?.showLongToast(this, data.Message.toString())
                    }
                }
                //                        deleteDataFromDb();
                booleanEmitter.onNext(true)
                booleanEmitter.onComplete()
            } catch (e: Throwable) {
                e.printStackTrace()
                booleanEmitter.onError(e)
            }
        }
    }


    private fun stopForegroundService() {
        // stop fore ground service and remove notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
        mFusedLocationClient?.removeLocationUpdates(locationCallback)


    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        this.locationRequest?.let {
            mFusedLocationClient?.requestLocationUpdates(
                it,
                this.locationCallback, Looper.myLooper()
            )
        }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation: Location? = locationResult.lastLocation
            if (currentLocation != null) {
                stringLatitude = currentLocation.latitude.toString()
                stringLongitude = currentLocation.longitude.toString()
                Log.e(TAG, "Lat and Long -> $stringLatitude,$stringLongitude")
            }

        }
    }

    private fun startTimer() {
        myTask = object : TimerTask() {
            override fun run() {
                // your code
                if (stringLatitude != null) {
                    insertLatLong()
                }
            }
        }
        val runTimeInterVal: Int = timeInterval * (60 * 1000)
        Log.e(TAG, "runTimeInterval - > $runTimeInterVal")
        myTimer.scheduleAtFixedRate(myTask, 0L, runTimeInterVal.toLong())

    }


    private fun getAddress(latitude: Double, longitude: Double): String? {

        return try {
            var addresses: List<Address>? = null
            val geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(
                latitude, longitude, 1
            )
            //            }
            if (addresses != null && addresses.size > 0) {
                addresses[0].getAddressLine(0)
            } else ""
        } catch (e: java.lang.Exception) {
            Log.e(TAG , e.printStackTrace().toString())
            ""
        }
    }

    private fun startNotificationTimer() {
        myNotificationTask = object : TimerTask() {
            override fun run() {
                // your code

                val isLocationOn = AppUtils.INSTANCE?.isLocationEnabled() ?: false
                AppUtils.INSTANCE?.logMe("SERVICE", "isLocation Enable: $isLocationOn")
                if (!isLocationOn) {
                    if (isLastLocationStatusOn) {
                        isLastLocationStatusOn = false
                        updateNotificationForLocationServicesDisabled()
                    }
                    if (!isSystemAlertWindowAdded)
                        if ((isOkPressedTime + 10 * 1000) < System.currentTimeMillis())
                            openSystemWindowAlert()
                } else {
                    if (!isLastLocationStatusOn) {
                        prepareForegroundNotification(isNotificationTextUpdate = true)
                        isLastLocationStatusOn = true
                    }
//                    insertLatLong()
                }
            }
        }
        val runTimeInterVal: Int = 5 * 1000
        try {
            myNotificationTimer.scheduleAtFixedRate(
                myNotificationTask,
                0L,
                runTimeInterVal.toLong()
            )
        } catch (e: Exception) {
        }

    }

    companion object {
        private val TAG = LocationUpdateService::class.java.simpleName
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        private const val UPDATE_INTERVAL_IN_MILLISECONDS = (1000 * 60 * 2 // 2 minute
                ).toLong()
    }
    private fun isNotificationActive(notificationId: Int): Boolean {

        /*val activeNotifications = notificationManager.activeNotifications
        for (notification in activeNotifications) {
            if (notification.id == notificationId) {
                return true
            }
        }
        return false*/


        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        val activeNotifications = notificationManager?.activeNotifications
        activeNotifications?.forEach { notification ->
            if (notification.id == notificationId) {
                return true
            }
        }
        return false
    }
}