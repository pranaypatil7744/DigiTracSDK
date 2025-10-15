package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class CheckValidAttendanceTokenResponseModel(
    var AssociateID:String? ="",
    var AttendanceDate:String? ="",
    var InnovID:String? ="",
    var InorOutFlag:String? ="",
    var InTime:String? ="",
    var OutTime:String? ="",
    var Picture:String? ="",
    var Tolworkinghrs:String? ="",
    var WorkingLocationID:String? ="",
    var AttendanceTokenID:String? ="",
    var Status:String? ="",
    var Message:String? =""
)

@Keep
data class CheckValidAttendanceTokenRequestModel(
    var AttendanceTokenId:String= ""
)
