package com.example.digitracksdk.domain.model.onboarding.aadhaar_verification

import androidx.annotation.Keep

@Keep
data class AadhaarVerificationSendOtpRequestModel(
    var AadhaarNumber:String = "",
    var Consent:String = "",
    var ConsentText:String = "",
    var InnovId : String = ""
)

@Keep
data class AadhaarVerificationSendOtpResponseModel(
    var InnovId: String?="",
    var AadhaarNumber:String? = "",
    var RequestId:String? = "",
    var IsOtpSent:Boolean? = null,
    var IsNumberLinked:Boolean? = null,
    var IsAadhaarValid:Boolean? = null,
    var Success:String? = "",
    var ResponseCode:String? = "",
    var ResponseMessage:String? = "",
    var RequestTimeStamp:String? = "",
    var ResponseTimeStamp:String? = "",
    var Message:String? = "",
    var Status:String? = ""
)
