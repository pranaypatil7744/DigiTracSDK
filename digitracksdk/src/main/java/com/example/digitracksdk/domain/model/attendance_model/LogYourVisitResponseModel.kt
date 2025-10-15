package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class LogYourVisitResponseModel(
    var LogDataList: ArrayList<LogDataListModel>? = ArrayList(),
    var Status: String? = "",
    var Message: String? = "",
)

@Keep
data class LogDataListModel(
    var SrNo: String? = "",
    var LogTime: String? = "",
    var Latitude: String? = "",
    var Longitude: String? = "",
    var Type: String? = "",
)

@Keep
data class LogYourVisitRequestModel(
    var AssociateId:String ="",
    var LogDate:String =""
)
