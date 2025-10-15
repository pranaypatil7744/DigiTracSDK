package com.example.digitracksdk.presentation.home.geo_tracking.model

import androidx.annotation.Keep

@Keep
data class GeoTrackingModel(

    var GeoTrackingID: String = "",
    var CreatedDate: String = "",
    var TrackingStartDateTime: String = "",
    var TrackingEndDateTime: String = "",
    var TrackingStartLat: String = "",
    var TrackingStartLon: String = "",
    var TrackingEndLat: String = "",
    var TrackingEndLon: String = "",
    var StartAddress: String = "",
    var EndAddress : String = ""
)

