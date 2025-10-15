package com.innov.digitrac.webservice_api.request_resonse

import androidx.annotation.Keep

@Keep
data class AttendanceMarkRequestModel(
    val Attendancedate: String? = "",
    val GnetAssociateId: String? = "",
    val Latitude: String? = "",
    val Longitude: String? = ""
)

@Keep
data class AttendanceMarkResponseModel(
    var Status: String? = "",
    var Message: String? = ""
)