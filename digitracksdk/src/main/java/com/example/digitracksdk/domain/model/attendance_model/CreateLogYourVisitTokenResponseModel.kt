package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class CreateLogYourVisitTokenResponseModel(
    var LogYourVisitTokenID:String? ="",
    var DateAndTime:String? ="",
    var Status:String? ="",
    var Error:String? =""
)

@Keep
data class CreateLogYourVisitTokenRequestModel(
    var InnovID:String = "",
    var AssociateId:String = "",
    var DeviceID:String = "",
    var IMEI:String = "",
    var DeviceToken:String = "",
    var Latitude:String = "",
    var Longitude:String = "",
    var Distance:String = "",
    var LogYourVisit:String = "",
)
