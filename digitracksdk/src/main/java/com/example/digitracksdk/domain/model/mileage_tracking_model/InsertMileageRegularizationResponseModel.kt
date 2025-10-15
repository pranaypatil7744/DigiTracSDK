package com.example.digitracksdk.domain.model.mileage_tracking_model

import androidx.annotation.Keep

@Keep
data class InsertMileageRegularizationResponseModel(
    var Status:String? ="",
    var Message:String? ="",
)

@Keep
data class InsertMileageRegularizationRequestModel(
    var Empcode:String? = "",
    var EndReading:String? = "",
    var Remark:String? = "",
    var StartReading:String? = "",
    var TravelDate:String? = "",
)
