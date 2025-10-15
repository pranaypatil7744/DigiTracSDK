package com.example.digitracksdk.domain.model.onboarding.aadhaar_verification

import androidx.annotation.Keep

@Keep
data class AadhaarVerificationOtpValidationResponseModel(
    var RequestId:String? = "",
    var Result: ResultModel? = ResultModel(),
    var IsBillable:String? = "",
    var Success:String? = "",
    var ResponseCode:String? = "",
    var ResponseMessage:String? = "",
    var RequestTimeStamp:String? = "",
    var ResponseTimeStamp:String? = "",
//    var lstAadhaar:ArrayList<ResultModel>? = null,
    var Message:String? = "",
    var Status:String? = ""
)

@Keep
data class ResultModel(
    var UserFullName:String? = "",
    var UserAadhaarNumber:String? = "",
    var UserDOB:String? = "",
    var UserGender:String? = "",
    var UserAddressCountry:String? = "",
    var UserAddressDist:String? = "",
    var UserAddressState:String? = "",
    var UserAddressPO:String? = "",
    var UserAddressLOC:String? = "",
    var UserAddressVTC:String? = "",
    var UserAddressSubDist:String? = "",
    var UserAddressStreet:String? = "",
    var UserAddressHouse:String? = "",
    var UserAddressLandmark:String? = "",
    var AddressZip:String? = "",
    var UserImage:String? = "",
    var UserHasImage:Boolean? = null,
    var UserParentName:String? = "",
    var AadhaarShareCode:String? = "",
    var UserMobileVerified:String? = "",
    var ReferenceId:String? = "",
    val UserImageFilePath: String? = "",
)

@Keep
data class AadhaarVerificationOtpValidationRequestModel(
    var Consent:String = "",
    var ConsentText:String = "",
    var InnovId:String = "",
    var OTP:String = "",
    var RequestId:String = "",
)
