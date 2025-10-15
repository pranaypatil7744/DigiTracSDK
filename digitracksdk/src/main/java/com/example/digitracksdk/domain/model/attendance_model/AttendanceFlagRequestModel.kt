package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class AttendanceFlagRequestModel(
    val GnetAssociateId: String,
    val Attendancedate: String
)



@Keep
data class AttendanceFlagResponseModel(
    var AttendanceFlag: Int? = null,
    var Status: String? = "",
    var Message: String? = ""
)
