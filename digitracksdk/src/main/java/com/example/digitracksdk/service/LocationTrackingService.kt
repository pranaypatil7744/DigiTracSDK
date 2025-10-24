package com.example.digitracksdk.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.example.digitracksdk.Constant
import com.example.digitracksdk.Constant.PermissionRequestCodes.Companion.NOTIFICATION_PERMISSION_CODE
import com.example.digitracksdk.R
import com.example.digitracksdk.presentation.home.geo_tracking_2.model.GeoTrackingDataBaseModel
import com.example.digitracksdk.utils.AppUtils
import io.realm.Realm
import io.realm.RealmResults
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class LocationTrackingService : Service() {
    private lateinit var locationManager: LocationManager

    companion object {
        const val CHANNEL_ID = Constant.PermissionRequestCodes.CHANNEL_ID
        const val NOTIFICATION_ID = NOTIFICATION_PERMISSION_CODE
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null

    private var notificationManager: NotificationManager? = null

    private var location: Location? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        ).setIntervalMillis(2000).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocationFetch(locationResult)
            }
        }
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, "locations", NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    @Suppress("MissingPermission")
    private fun createLocationRequest() {
        try {
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest!!, locationCallback!!, null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun removeLocationUpdates() {
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun onNewLocationFetch(locationResult: LocationResult) {
        location = locationResult.lastLocation

        saveLocationToDatabase(
            latitude = location?.latitude.toString(),
            longitude = location?.longitude.toString()
        )
       /* EventBus.getDefault().post(
            GeoDatabaseModel(
                latitude = location?.latitude ?: 0.0,
                longitude = location?.longitude ?: 0.0
            )
        )*/
        startForeground(NOTIFICATION_ID, getNotification())

    }

    private fun getNotification(): Notification {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Updates")
            .setContentText(
                "Latitude--> ${location?.latitude}\nLongitude --> ${location?.longitude}"
            )
            .setSmallIcon(R.drawable.ic_loi)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createLocationRequest()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdates()
    }

    private fun saveLocationToDatabase(latitude: String, longitude: String) {
        val realm = Realm.getDefaultInstance()
        try {
            val lat = latitude.toDouble()
            val lon = longitude.toDouble()
            realm.beginTransaction()
            val existingData = realm.where(GeoTrackingDataBaseModel::class.java).findAll()
//             if (existingData.isEmpty() || !distanceBetweenPoints(existingData, lat, lon)) {

            val geoTrackingData = GeoTrackingDataBaseModel().apply {
                geoTrackingId = UUID.randomUUID().toString()
                this.latitude = lat.toString()
                this.longitude = lon.toString()
            }
            realm.copyToRealm(geoTrackingData)
//            }
            val locationDataList = realm.where(GeoTrackingDataBaseModel::class.java).findAll()

            realm.commitTransaction()

            locationDataList.forEach { locationData ->
                AppUtils.INSTANCE?.logMe(
                    "LocationData",
                    "GeoTrackingId: ${locationData.geoTrackingId}"
                )

            }
        } catch (e: NumberFormatException) {
            AppUtils.INSTANCE?.logMe("TAG:123", "" + e.message.toString())
        } finally {
            realm.close() // Close the Realm instance in the finally block
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c * 1000 // Distance in meters
    }

    private fun distanceBetweenPoints(
        existingData: RealmResults<GeoTrackingDataBaseModel>,
        newLatitude: Double,
        newLongitude: Double
    ): Boolean {
        for (data in existingData) {
            val existingLatitude = data.latitude.toDouble()
            val existingLongitude = data.longitude.toDouble()
            AppUtils.INSTANCE?.logMe("longitude123", "" + data.longitude)
            val distance =
                calculateDistance(existingLatitude, existingLongitude, newLatitude, newLongitude)
            if (distance < 10) {
                return true
            }
        }
        return false
    }
}







