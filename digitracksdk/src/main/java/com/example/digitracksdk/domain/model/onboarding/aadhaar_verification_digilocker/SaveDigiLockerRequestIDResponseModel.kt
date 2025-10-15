package com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker

import androidx.annotation.Keep

@Keep
data class SaveDigiLockerRequestIDResponseModel(
    var Message: String? = "",
    var status: String? = ""
)


@Keep
data class SaveDigiLockerRequestIDRequestModel(
    var AadharNumber: String? = "",
    var RequestID: String? = "",
    var InnovId: String? = ""
)

@Keep
data class GetAadhaarDetailDigiLockerResponseModel(
    var Message: String?="",
    var data : Data?= Data(),
    var status: String?=""
)

@Keep
data class Data(
    var AadhaarNo: String?="",
    var Name: String?="",
    var Father_Husband_Name: String?="",
    var Profile: String?="",
    var Gender: String?="",
    var DOB: String?="",
    var Address: String?="",
    var SubDistrict: String?="",
    var PostOffice: String?="",
    var District: String?="",

    var City: String?="",
    var State: String?="",
    var Country: String?="",
    var Pin: String?="",


)
@Keep
data class GetAadhaarDetailDigiLockerRequestModel(
    var RequestID: String? = "",
    var InnovID : String?="",
    var AadhaarNo : String?=""
)
