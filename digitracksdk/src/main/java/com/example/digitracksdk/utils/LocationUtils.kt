package com.example.digitracksdk.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.digitracksdk.Constant
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.LayoutLocationUtilsBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class LocationUtils : BaseActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    lateinit var binding: LayoutLocationUtilsBinding
    private var addresses: List<Address>? = ArrayList<Address>()
    lateinit var preferenceUtils: PreferenceUtils
    var latitude: Double? = 0.0
    var longitude: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutLocationUtilsBinding.inflate(layoutInflater)
        preferenceUtils = PreferenceUtils(this)
        setContentView(binding.root)
        getLastLocation()
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Constant.PermissionRequestCodes.PERMISSION_REQUEST_ACCESS_FINE_LOCATION
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        //TODO
        geocoder = Geocoder(baseContext, Locale.getDefault())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            preferenceUtils.setValue(Constant.AskedPermission.LOCATION_PERMISSION_COUNT, 0)
            if (isLocationEnabled()) {
                getLocation()
            } else {
                enableLocationSettings()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        toggleLoader(true)
        mFusedLocationClient.lastLocation.addOnCompleteListener {
            val location = it.result
            if (location == null) {
                requestNewLocationData()
            } else {
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()
                intent.putExtra(Constant.IntentExtras.EXTRA_LATITUDE, latitude)
                intent.putExtra(Constant.IntentExtras.EXTRA_LONGITUDE, longitude)
                intent.putExtra(
                    Constant.IntentExtras.EXTRA_PICKUP_ADDRESS,
                    getAddressFromLatLon(latitude.toDouble(), longitude.toDouble()) ?: ""
                )
                setResult(Activity.RESULT_OK, intent)
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                    try{
                        toggleLoader(false)
                    }catch (e:java.lang.Exception){
                    }
                    finally {
                        finish()
                    }
                }
                else
                {
                    toggleLoader(false)
                    finish()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
//        val mLocationRequest = LocationRequest.create().apply {
//            interval = 100
//            fastestInterval = 50
//            priority = Priority.PRIORITY_HIGH_ACCURACY
//            maxWaitTime= 100
//            numUpdates = 1
//        }

        val mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(50)
            .setMaxUpdateDelayMillis(100)
            .build()

        Looper.myLooper()?.let {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback, it)
        }

        getLocation()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            latitude = mLastLocation?.latitude
            longitude = mLastLocation?.longitude
            intent.putExtra(Constant.IntentExtras.EXTRA_LATITUDE, latitude.toString())
            intent.putExtra(Constant.IntentExtras.EXTRA_LATITUDE, latitude.toString())
            val address = getAddressFromLatLon(latitude ?: 0.00, longitude ?: 0.00)
            intent.putExtra(Constant.IntentExtras.EXTRA_PICKUP_ADDRESS, address)
            setResult(Activity.RESULT_OK, intent)
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                try{
                    toggleLoader(false)
                }catch (e:java.lang.Exception){
                }
                finally {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        finish()
                    }
                }
            }
            else
            {
                toggleLoader(false)
                finish()
            }
        }
    }

    private fun getAddressFromLatLon(latitude: Double, longitude: Double): String? {
        /**
         * Returns Address based on lat,lon
         */
        var strAdd: String? = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    latitude, longitude, 1
                ) {
                    addresses = it
                }
            } else {
                addresses = geocoder.getFromLocation(
                    latitude, longitude, 1
                )
            }
            if (addresses != null) {
                val returnedAddress = addresses!![0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                AppUtils.INSTANCE?.logMe(
                    "My Current location address",
                    strReturnedAddress.toString()
                )
            } else {
                AppUtils.INSTANCE?.logMe("My Current location address", "No Address returned!")
            }
        } catch (e: Exception) {
            AppUtils.INSTANCE?.logMe("Exception :", e.toString())
            return strAdd
        }
        return strAdd
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constant.PermissionRequestCodes.PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                val locationPerCount = preferenceUtils.getIntValue(
                    Constant.AskedPermission.LOCATION_PERMISSION_COUNT,
                    0
                )
                preferenceUtils.setValue(
                    Constant.AskedPermission.LOCATION_PERMISSION_COUNT,
                    locationPerCount.plus(1)
                )
                if (preferenceUtils.getIntValue(
                        Constant.AskedPermission.LOCATION_PERMISSION_COUNT,
                        0
                    ) >= 2
                ) {
                    DialogUtils.showPermissionDialog(
                        this,
                        getString(com.example.digitracksdk.R.string.please_grant_the_location_permission_to_continue),
                        getString(com.example.digitracksdk.R.string.allow_permission),
                        getString(com.example.digitracksdk.R.string.go_to_settings),
                        getString(com.example.digitracksdk.R.string.deny)
                    )
                } else {
                    AppUtils.INSTANCE?.showLongToast(
                        this,
                        getString(com.example.digitracksdk.R.string.please_grant_the_location_permission_to_continue)
                    )
                    finish()
                }
            }
        }

    }


    private fun enableLocationSettings() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(50)
            .setMaxUpdateDelayMillis(100)
            .build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(
                this) { }
            .addOnFailureListener(
                this
            ) { ex: java.lang.Exception? ->
                if (ex is ResolvableApiException) {
                    // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                        ex.startResolutionForResult(
                            this,
                            Constant.PermissionRequestCodes.REQUEST_CODE_CHECK_SETTINGS
                        )

//                        requestPermissions()
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
    }

   @Deprecated("Deprecated in Java")
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if(requestCode == Constant.PermissionRequestCodes.REQUEST_CODE_CHECK_SETTINGS) {
           if(Activity.RESULT_OK == resultCode){
               getLastLocation()
           } else {
               showToast(msg =  getString(com.example.digitracksdk.R.string.please_turn_on_location))
               val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
               startActivity(intent)
               setResult(Activity.RESULT_CANCELED, intent)
               finish()
           }
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

}
