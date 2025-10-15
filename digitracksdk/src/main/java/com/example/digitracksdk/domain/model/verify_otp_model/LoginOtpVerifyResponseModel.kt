package com.example.digitracksdk.domain.model.verify_otp_model

import androidx.annotation.Keep

@Keep
data class LoginOtpVerifyResponseModel(
    var InnovID:String? = "",
    var OTPStatus:String? = "",
    var TokenID:String? = "",
)

@Keep
data class LoginOtpVerifyRequestModel(
    var Mobile:String? = "",
    var OTP:String? = "",
)
