package com.example.digitracksdk.domain.model.onboarding.work_experience

import androidx.annotation.Keep

@Keep
data class InsertWorkExpResponseModel(
    var status:String? ="",
    var Message:String? ="",
    var OTP:String? ="",
    var TokenID:String? ="",
    var InnovID:String? =""
)

@Keep
data class InsertWorkExpRequestModel(
    var InnovID:String ="",
    var TotalExpInYear:String ="",
    var TotalExpMonth:String ="",
    var totalRelevantExpYear:String ="",
    var totalRelevantExpMonth:String ="",
    var CompanyName:String ="",
    var DateOfJoining:String ="",
    var LastWorkingDate:String ="",
    var Designation:String ="",
    var LastCTC:String ="",
    var CurrentlyEmployed:String ="",
    var isFresher:String =""
)
