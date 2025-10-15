package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class CurrentDayAttendanceStatusResponseModel(
    var InnovID: String? = "",
    var Attendencedate: String? = "",
    var Day: String? = "",
    var InStatus: Boolean? = false,
    var OutStatus: Boolean? = false,
    var InPicture: String? = "",
    var OutPicture: String? = "",
    var Status: String? = "",
    var Message: String? = ""
)

@Keep
data class CurrentDayAttendanceStatusRequestModel(
    var InnovID: String = "",
    var attendanceDate: String = ""
)
