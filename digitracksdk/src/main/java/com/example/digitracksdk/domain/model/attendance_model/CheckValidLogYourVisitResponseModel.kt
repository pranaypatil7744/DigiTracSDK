package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class CheckValidLogYourVisitResponseModel(
    var Error:String? = "",
    var Status:String? = ""
)

@Keep
data class CheckValidLogYourVisitRequestModel(
    var LogYourVisitTokenID:String = "",
    var LogYourVisit:String = "",
    var Picture:String = ""
)
