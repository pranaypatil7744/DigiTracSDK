package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class PaperlessUpdateCandidateBasicDetailsRequestModel(
    var AadharNo: String?="",
    var AlternateNo: String?="",
    var AlternateNoFlag : String?="",
    var Address1: String?="",
    var Address2: String?="",
    var Address3: String?="",
    var BloodGroup: String?="",
    var EmailId: String?="",
    var EmergencyContactName: String?="",
    var EmergencyContactNo: String?="",
    var Gender: String?="",
    var InnovID: String?="",
    var Location: String?="",
    var MartialStatusID: String?="",
    var NoOfChildren: String?="",
    var Pincode: String?="",
    var MartialStatus : String?="",
    var StateId : Int ? = 0,
    var StateName : String?=""

)

@Keep
data class PaperlessUpdateCandidateBasicDetailsResponseModel(
    var Message: String?="",
    var status: String?="",
)

