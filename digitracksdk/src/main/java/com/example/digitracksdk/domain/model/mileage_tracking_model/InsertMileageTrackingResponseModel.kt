package com.example.digitracksdk.domain.model.mileage_tracking_model

import androidx.annotation.Keep

@Keep
data class InsertMileageTrackingResponseModel(
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class InsertMileageTrackingRequestModel(
    var EmployeeCode:String? ="",
    var TravelDate:String? ="",
    var StartReading:String? ="",
    var EndReading:String? ="",
    var StartReadingImageArr:String? ="",
    var EndReadingImageArr:String? =""
)
