package com.example.digitracksdk.domain.model.profile_model

import androidx.annotation.Keep

@Keep
data class CheckOtpResponseModel(
    var status:String? ="",
    var Message:String? ="",
    var OTP:String? ="",
    var TokenID:String? ="",
    var InnovID:String? =""
)
@Keep
data class CheckOtpRequestModel(
    var TokenID:String="",
    var OTP:String="",
)
