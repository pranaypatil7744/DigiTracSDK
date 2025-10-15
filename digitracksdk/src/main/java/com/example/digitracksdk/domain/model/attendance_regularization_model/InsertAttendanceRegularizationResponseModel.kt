package com.example.digitracksdk.domain.model.attendance_regularization_model

import androidx.annotation.Keep

@Keep
data class InsertAttendanceRegularizationRequestModel(
    var Empcode: String = "",
    var RequestTypeId: Int = 0,
    var RegularizationDate: String = "",
    var InTime: String = "00:00",
    var OutTime: String = "00:00",
    var Location: String= "null",
    var Remarks: String = "",
    var ToDate: String = "",
    var LeaveAppliedFor:String = ""
)

@Keep
data class InsertAttendanceRegularizationResponseModel(
    var Status: String? = "",
    var Message:String? = "",
    var date  : String ? = ""
)