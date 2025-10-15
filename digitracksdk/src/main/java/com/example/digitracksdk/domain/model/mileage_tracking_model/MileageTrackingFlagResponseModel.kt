package com.example.digitracksdk.domain.model.mileage_tracking_model

import androidx.annotation.Keep

@Keep
data class MileageTrackingFlagResponseModel(
    var Status:Int? =0,
    var Message:String? ="",
)

@Keep
data class MileageTrackingFlagRequestModel(
    var EmployeeCode:String? =""
)


