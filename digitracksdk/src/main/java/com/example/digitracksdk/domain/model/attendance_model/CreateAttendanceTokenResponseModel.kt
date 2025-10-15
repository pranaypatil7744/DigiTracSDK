package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class CreateAttendanceTokenRequestModel(
    var InnovID: String = "",
    var DeviceID: String = "",
    var IMEI: String = "",
    var DeviceToken: String = "",
    var Latitude: String = "",
    var Longitude: String = "",
    var Distance: String = "",
    var InorOut: String = "",
    var AttendanceFromAnyWhereValue: String = "",
    var attendanceDate: String = "",
    var attendanceFlag: String = "",
)

@Keep
data class CreateAttendanceTokenResponseModel(
    var AttendanceTokenID: String? = "",
    var IsBecaonApplicable: Int? = 0,
    var GPSAttendanceCriteriaFlag: String? = "",
    var Status: String? = "",
    var Message: String? = "",
)


