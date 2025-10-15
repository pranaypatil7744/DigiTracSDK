package com.example.digitracksdk.presentation.home.geo_tracking_2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.GeoTrackingActivityBinding
import com.example.digitracksdk.presentation.home.geo_tracking_2.model.GeoTrackingDataBaseModel
import com.example.digitracksdk.service.LocationTrackingService
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import io.realm.Realm
import io.realm.kotlin.where

class GeoTrackingActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var binding: GeoTrackingActivityBinding
    private lateinit var preferenceUtils: PreferenceUtils
    private var currentLatitude: String? = ""
    private var currentLongitude: String? = ""
    private lateinit var gMap: GoogleMap
    private var serviceForLocation: Intent? = null
    private var isServiceOngoing: Boolean = false

    private val locationUtil =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentLatitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LATITUDE)
                currentLongitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LONGITUDE)
                binding.mapView.getMapAsync(this@GeoTrackingActivity)

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = GeoTrackingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        locationUtil.launch(Intent(this, LocationUtils::class.java))
        preferenceUtils = PreferenceUtils(this)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        setUpToolbar()
        setUpListeners()

    }

    private fun plotMarker(locationMarkerList: Array<GeoTrackingDataBaseModel>) {
        locationMarkerList.forEach { location ->
            try {
                val latitude = location.latitude
                val longitude = location.longitude
                val latLng = latitude.toDoubleOrNull()?.let {
                    longitude.toDoubleOrNull()
                        ?.let { it1 -> LatLng(it, it1) }
                }
                latLng?.let { MarkerOptions().position(it) }?.let { gMap.addMarker(it) }

            } catch (e: NumberFormatException) {
                AppUtils.INSTANCE?.logMe("TAG:", "" + e.message)
            }
        }

        if (locationMarkerList.isNotEmpty()) {
            val firstLocation = locationMarkerList[0]
            try {
                val lat = firstLocation.latitude.toDoubleOrNull() ?: 0.0
                val lon = firstLocation.longitude.toDoubleOrNull() ?: 0.0
                val latLng = LatLng(lat, lon)
                CameraUpdateFactory.newLatLngZoom(latLng, 10f)
                latLng.let { CameraUpdateFactory.newLatLngZoom(it, 10f) }
                    .let { gMap.moveCamera(it) }
            } catch (e: NumberFormatException) {
                AppUtils.INSTANCE?.logMe("TAG123", "" + e.message)
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.geo_tracking)
        }
    }

    private fun setUpListeners() {
        binding.apply {
            btnStartStopTracking.setOnClickListener {
                if (!isServiceOngoing) {
                    serviceForLocation =
                        Intent(
                            this@GeoTrackingActivity,
                            LocationTrackingService::class.java
                        )
                    startService(serviceForLocation)
                    /*    val serviceComponent = startService(serviceForLocation)

                      if (serviceComponent != null) {

                            Log.d("ServiceDebug", "Service started successfully: $serviceComponent")
                        } else {

                            Log.e("ServiceDebug", "Failed to start service")
                        }*/
                    isServiceOngoing = true
                    preferenceUtils.setValue(
                        Constant.IntentExtras.IS_SERVICE_ONGOING,
                        isServiceOngoing
                    )
                }
                /* else  {
                          if (isServiceRunning(LocationTrackingService::class.java)){
                              stopService(serviceForLocation)
                              plotMarker(locationMarkerList)
                          }
                 }*/
                btnStartStopTracking1.setOnClickListener {
                    if (isServiceRunning(LocationTrackingService::class.java)) {
                        stopService(serviceForLocation)
                        fetchDataFromDatabase()
//                            plotMarker(locationMarkerList)
                    }
                }
            }


        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        map.let {

            gMap = map
            gMap.isMyLocationEnabled = true
            gMap.uiSettings.isMyLocationButtonEnabled = true

            val latLng = LatLng(
                currentLatitude?.toDouble() ?: 0.0,
                currentLongitude?.toDouble() ?: 0.0
            )

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13f)

            gMap.animateCamera(cameraUpdate)

            val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(15f)
                .bearing(90f)
                .tilt(40f)
                .build()

            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()


    }

    private fun fetchDataFromDatabase() {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<GeoTrackingDataBaseModel>().findAll()

        val locationMarkerList = realm.copyFromRealm(results)
        AppUtils.INSTANCE?.logMe("Location TAG", "" + locationMarkerList.toString())
        processLocationData(locationMarkerList.toTypedArray()) // Pass locationMarkerList as parameter

        realm.close()
    }


    private fun processLocationData(locationMarkerList: Array<GeoTrackingDataBaseModel>) {

        /*   locationMarkerList.forEach { location ->

               val latitude = location.latitude
               val longitude = location.longitude
           }*/
        plotMarker(locationMarkerList)
        AppUtils.INSTANCE?.logMe("TAG", "" + locationMarkerList)
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}