package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class UpdateAttendanceStatusResponseModel(
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class UpdateAttendanceStatusRequestModel(
    var AttendanceDate:String = "",
    var GNETAssociateID:String? = ""
)
