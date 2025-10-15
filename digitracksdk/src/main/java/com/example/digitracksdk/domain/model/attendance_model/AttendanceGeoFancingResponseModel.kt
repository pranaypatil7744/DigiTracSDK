package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class AttendanceGeoFancingResponseModel(
    var Latitude:String? = "",
    var Longitude:String? = "",
    var Radius:Int? = 0,
    var status:String? = "",
    var Message:String? = "",
)
