package com.example.digitracksdk.presentation.map_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class MapViewFragment : BaseFragment(), OnMapReadyCallback {

    lateinit var binding: FragmentMapBinding
    private lateinit var gMap: GoogleMap
    var currentLatitude: Double? = 0.0
    var currentLongitude: Double? = 0.0
    companion object{
        fun newInstance(): MapViewFragment {
            return MapViewFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
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
        }
    }

}