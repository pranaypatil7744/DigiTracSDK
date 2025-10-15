package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class AttendanceValidationRequestModel(
    var FromDate:String ="",
    var GNETAssociateID:String ="",
    var InnovID:String ="",
    var ToDate:String ="",
    var Type:String =""

)

@Keep
data class AttendanceValidationResponseModel(
    var status:String? ="",
    var Message:String? ="",
    var date  : String? =""
)
