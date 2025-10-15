package com.example.digitracksdk.domain.usecase.attendance_usecase

import androidx.annotation.Keep

@Keep
data class InsertAttendancePicResponseModel(
    var InnovID:String? ="",
    var AttendanceDate:String? ="",
    var InTime:String? ="",
    var OutTime:String? ="",
    var Picture:String? ="",
    var Status:String? ="",
    var Message:String? =""
)

@Keep
data class InsertAttendancePicRequestModel(
    var InnovID:String= "",
    var AttendanceDate:String= "",
    var InTime:String= "",
    var OutTime:String= "",
    var Picture:String= "",
)
