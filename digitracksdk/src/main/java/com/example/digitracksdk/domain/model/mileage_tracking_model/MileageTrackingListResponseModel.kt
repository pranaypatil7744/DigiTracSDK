package com.example.digitracksdk.domain.model.mileage_tracking_model

import androidx.annotation.Keep

@Keep
data class MileageTrackingListResponseModel(
    var lstMileage:ArrayList<ListMileageModel>? = ArrayList(),
)

@Keep
data class ListMileageModel(
    var AssociateId:String? = "",
    var Empcode:String? = "",
    var EmployeeCode:String? = "",
    var EndReading:String? = "",
    var EndReadingFilePath:String? = "",
    var EndReadingImageArr:String? = "",
    var InnovID:String? = "",
    var IsActive:String? = "",
    var MappingID:String? = "",
    var MileageTrackingId:String? = "",
    var SrNo:String? = "",
    var StartReading:String? = "",
    var StartReadingFilePath:String? = "",
    var StartReadingImageArr:String? = "",
    var Status:String? = "",
    var Message:String? = "",
    var TravelDate:String? = ""
)

@Keep
data class MileageTrackingListRequestModel(
    var EmployeeCode:String? =""
)