package com.example.digitracksdk.domain.model.login_model

import androidx.annotation.Keep

@Keep
data class LoginResponseModel(
    val CandidateStatus:String? = "",
    val InnovID:String? = "",
    val IsDummy:Boolean? = false,
    val IsMigrated:String? = "",
    val IsTransferred:String? = "",
    val Mobile:String? = "",
    val OTP:Int? = 0,
    val Status:String? = "",
)

@Keep
data class LoginRequestModel(
    var APKVersion:String? = "",
    var AndroidVersion:String? = "",
    var BuildNo:String? = "",
    var EmployeeCode:String? = "",
    var Mobile:String? = "",
    var ModelNo:String? = "",
    var SignupSource:String? = "",
)
