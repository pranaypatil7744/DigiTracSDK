package com.example.digitracksdk.presentation.home.geo_tracking.tracking_details

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.data.database.GeoTrackingDataModel
import com.example.digitracksdk.databinding.ActivityGeoTrackingDetailsBinding
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.lstGeoTrackingDetails
import com.example.digitracksdk.presentation.home.geo_tracking.GeoTrackingSummaryViewModel
import com.example.digitracksdk.service.LocationUpdateService
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import io.realm.Realm
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
//import org.jetbrains.anko.async
//import org.jetbrains.anko.uiThread
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Mo Khurseed Ansari on 20-Jul-2022.
 */
class GeoTrackingDetailsActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityGeoTrackingDetailsBinding
    private val geoTrackingViewModel: GeoTrackingSummaryViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private var currentLatitude: String? = ""
    private var currentLongitude: String? = ""
    private var trackingId: String? = ""
    private lateinit var gMap: GoogleMap
    private lateinit var markerOptions: MarkerOptions
    private var myMarker: Marker? = null
    private var realm: Realm = Realm.getDefaultInstance()
    private lateinit var origin: LatLng
    private lateinit var dest: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGeoTrackingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        locationUtil.launch(Intent(this@GeoTrackingDetailsActivity, LocationUtils::class.java))
        setUpToolbar()
        getIntentData()
//        setUpObserver()
        setUpListener()
        handleView()
    }


    private fun setUpObserver() {
        binding.apply {
            with(geoTrackingViewModel) {
                geoTrackingDetailsResponseData.observe(
                    this@GeoTrackingDetailsActivity
                ) {
                    toggleLoader(false)
                    AppUtils.INSTANCE?.logMe("API Check", "status ->${it.Message}")
                    if (it.Status == Constant.SUCCESS) {
                        if (it.lstGeoTrackingDetails.isNotEmpty()) {
                            binding.layoutNoData.root.visibility = View.GONE
                            plotMarker(it.lstGeoTrackingDetails)

                        } else {
                            binding.layoutNoData.tvNoData.text = it.Message
                            binding.layoutNoData.root.visibility = View.VISIBLE
                        }
                    } else {
                        showToast(it.Message.toString())
                    }

                }
                messageData.observe(this@GeoTrackingDetailsActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }

        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            loader = binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }


    private fun plotMarker(listLatLng: ArrayList<lstGeoTrackingDetails>) {

        if (listLatLng.size >= 2) {

            val startLat: Double = listLatLng[0].Latitute?.toDouble()?:0.0
            val startLng: Double = listLatLng[0].Longitude?.toDouble()?:0.0
            val endLat: Double = listLatLng[listLatLng.size - 1].Latitute?.toDouble()?:0.0
            val endLng: Double = listLatLng[listLatLng.size - 1].Longitude?.toDouble()?:0.0
            val lastMarker = LatLng(startLat, startLng)
            val startMarker = LatLng(endLat, endLng)
            var address = ""
            var endAddress = " "
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                var addresses: MutableList<Address>? = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        startMarker.latitude, startMarker.longitude, 1
                    ) {
                        addresses = it
                    }
                } else {
                    addresses = geocoder.getFromLocation(
                        startMarker.latitude, startMarker.longitude, 1
                    )
                }


                var endAddresses: MutableList<Address>? = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        lastMarker.latitude, lastMarker.longitude, 1
                    ) {
                        endAddresses = it
                    }
                } else {
                    endAddresses = geocoder.getFromLocation(
                        lastMarker.latitude, lastMarker.longitude, 1
                    )
                }

                address = addresses?.get(0)?.getAddressLine(0) ?: ""
                endAddress = endAddresses?.get(0)?.getAddressLine(0) ?: ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val iconStart = BitmapDescriptorFactory.fromResource(R.drawable.log_visit_in_big)
            markerOptions = MarkerOptions().position(startMarker)
                .title(getString(R.string.start_tracking))
                .snippet(address)
                .icon(iconStart)

            myMarker = gMap.addMarker(markerOptions)
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.log_visit_out_big)
            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            markerOptions = MarkerOptions().position(lastMarker)
                .title(getString(R.string.stop_tracking))
                .snippet(endAddress)
                .icon(icon)

            myMarker = gMap.addMarker(markerOptions)

            if (!listLatLng[0].Latitute.isNullOrEmpty() && !listLatLng[listLatLng.size - 1].Longitude.isNullOrEmpty()) {

                downloadTask(getDirectionAPI(listLatLng))

            }

        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun downloadTask(directionAPI: String) {

        // declare bounds object to fit whole route in screen
        val latLongB = LatLngBounds.Builder()

        // Declare polyline object and set up color and width
        val options = PolylineOptions()
        options.color(Color.BLUE)
        options.width(5f)


        GlobalScope.launch(Dispatchers.Main) {
            // Connect to URL, download content and convert into string asynchronously
            val resultIO = withContext(Dispatchers.IO) {
                val result = URL(directionAPI).readText()



                val parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")
                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }

                // Add  points to polyline and bounds
                options.add(origin)
                latLongB.include(origin)
                for (point in polypts) {
                    options.add(point)
                    latLongB.include(point)
                }
                options.add(
                    dest
                )
                latLongB.include(dest)
                // build bounds
                val bounds = latLongB.build()
                // add polyline to the map
                gMap.addPolyline(options)
                // show map with route centered
                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }

    }


    private fun handleView() {
        binding.apply {
            if (getIntentData()) {
                layoutBottom.visibility = View.GONE
                btnStartTracking.visibility = View.GONE
            } else {
                layoutBottom.visibility = View.VISIBLE
                if (isServiceRunning(LocationUpdateService::class.java)) {
                    btnStopTracking.visibility = View.VISIBLE
                    btnStartTracking.visibility = View.GONE
                } else {
                    btnStopTracking.visibility = View.GONE
                    btnStartTracking.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) if (serviceClass.name == service.service.className) {
            return true
        }
        return false

    }

    private fun setUpListener() {
        binding.apply {

            binding.apply {
                layoutNoInternet.btnTryAgain.setOnClickListener {
                    callAPIForGeoTrackingDetails(trackingId)
                }
            }

            btnStartTracking.setOnClickListener {
                btnStopTracking.visibility = View.VISIBLE
                btnStartTracking.visibility = View.GONE
                setTrackingStart()
            }
            btnStopTracking.setOnClickListener {
                if (!isNetworkAvailable()) {
                    showToast(getString(R.string.no_internet_connection))
                    return@setOnClickListener
                }

                btnStopTracking.visibility = View.GONE
                btnStartTracking.visibility = View.VISIBLE
                stopTracking()
            }
        }
    }

    private fun setTrackingStart() {
        gMap.clear()

        var address = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            var addresses: MutableList<Address>? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    currentLatitude?.toDouble() ?: 0.0, currentLongitude?.toDouble() ?: 0.0, 1
                ) {
                    addresses = it
                }
            } else {
                addresses = geocoder.getFromLocation(
                    currentLatitude?.toDouble() ?: 0.0, currentLongitude?.toDouble() ?: 0.0, 1
                )
            }
            address = addresses?.get(0)?.getAddressLine(0) ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val latLng = LatLng(currentLatitude?.toDouble()?:0.0, currentLongitude?.toDouble()?:0.0)
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.visit_marker)
        val currentTime = SimpleDateFormat("dd/MM/yyyy,HH:mm:ss", Locale.getDefault()).format(
            Date()
        )

        markerOptions = MarkerOptions().position(latLng)
            .title(getString(R.string.start_tracking))
            .snippet(currentTime)
            .snippet(address)
            .icon(icon)

        myMarker = gMap.addMarker(markerOptions)
        preferenceUtils.setValue(Constant.PreferenceKeys.GeoTrackingID, "")

        realm.executeTransaction { r: Realm ->
            r.delete(GeoTrackingDataModel::class.java)
        }
        val intent = Intent(this, LocationUpdateService::class.java)
        intent.action = LocationUpdateService.ACTION_START_FOREGROUND_SERVICE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopTracking() {
        val intent = Intent(this, LocationUpdateService::class.java)
        intent.action = LocationUpdateService.ACTION_STOP_FOREGROUND_SERVICE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        finish()
    }


    private fun getIntentData(): Boolean {
        intent.extras?.run {
            currentLatitude = getString(Constant.IntentExtras.EXTRA_LATITUDE)
            currentLongitude = getString(Constant.IntentExtras.EXTRA_LONGITUDE)
            if (!getString(Constant.IntentExtras.GEO_TRACKING_ID).isNullOrEmpty()) {
                binding.btnStartTracking.visibility = View.GONE
                trackingId = getString(Constant.IntentExtras.GEO_TRACKING_ID)
                callAPIForGeoTrackingDetails(trackingId)
            }
            return true
        }
        return false
    }

    private fun callAPIForGeoTrackingDetails(trackingId: String?) {
        if (isNetworkAvailable()) {
            binding.layoutNoInternet.root.visibility = View.GONE
            toggleLoader(true)
            geoTrackingViewModel.callGeoTrackingDetailsApi(
                requestModel(trackingId?:"")
            )

        } else {
            binding.layoutNoInternet.root.visibility = View.VISIBLE
        }
    }

    private fun requestModel(trackingId: String): GeoTrackingDetailsRequestModel {
        val request = GeoTrackingDetailsRequestModel()
        request.AssociateGeoTrackingID = trackingId
        return request
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

    private val locationUtil =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentLatitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LATITUDE)
                currentLongitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LONGITUDE)
                binding.mapView.getMapAsync(this@GeoTrackingDetailsActivity)
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

            if (getIntentData()) {
                setUpObserver()
            }


        }
    }


    private fun getDirectionAPI(listLatLng: ArrayList<lstGeoTrackingDetails>): String {

        origin = LatLng(
            listLatLng[0].Latitute?.toDouble()?:0.0,
            listLatLng[0].Longitude?.toDouble()?:0.0
        )
        dest = LatLng(
            listLatLng[listLatLng.size - 1].Latitute?.toDouble()?:0.0,
            listLatLng[listLatLng.size - 1].Longitude?.toDouble()?:0.0
        )
        // origin of route
        val strOrigin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val strDest = "destination=" + dest.latitude + "," + dest.longitude
        //Sensor Enable
        val sensor = "sensor=false"
        // way points
        var waypoints = ""
        val size: Int = if (listLatLng.size > 25) {
            25
        } else {
            listLatLng.size
        }
        for (i in 2 until size) {
            val point = LatLng(
                listLatLng[i].Latitute?.toDouble()?:0.0,
                listLatLng[i].Longitude?.toDouble()?:0.0
            )
            if (i == 2) waypoints = "waypoints="
            waypoints += point.latitude.toString() + "," + point.longitude + "|"
        }


        // Building the parameters to the web service
        val parameters = "$strOrigin&$strDest&$sensor&$waypoints"
        // Output format
        val output = "json"
        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        //        AIzaSyD_L8g3AcwXBKnEjhvLJwBXwI3L51LjQUU
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyDdwvwO5QLlpR_rQ_d28Xhh7r0JWZHC0js"
    }


    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }

}