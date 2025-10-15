package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class AttendanceZoneResponseModel(
    var worklocation:String? ="",
    var latitude:String? ="",
    var longitude:String? ="",
    var BluetoothName:String? ="",
    var BlutoothMacAdress:String? ="",
    var Status:String? ="",
    var Message:String? ="",
)
