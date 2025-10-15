package com.example.digitracksdk.domain.model.profile_model

import androidx.annotation.Keep

@Keep
data class NewInnovIdCreationResponseModel(
    var status:String? ="",
    var Message:String? ="",
    var OTP:String? ="",
    var TokenID:String? ="",
    var InnovID:String? =""
)

@Keep
data class NewInnovIdCreationRequestModel(
    var FirstName:String="",
    var MiddleName:String="",
    var LastName:String="",
    var Skill:String="",
    var EmergencyContactNo:String="",
    var EmergencyContactName:String="",
    var BloodGroup:String="",
    var Address1:String="",
    var Address2:String="",
    var Address3:String="",
    var StateID:Int=0,
    var CityID:Int=0,
    var PIN:String="",
    var PermanentAddress1:String="",
    var PermanentAddress2:String="",
    var PermanentAddress3:String="",
    var PermanentStateID:Int=0,
    var PermanentCityID:Int=0,
    var PermanentAddressPIN:String="",
    var RecruiterID:String="",
    var TokenID:String="",
    var MaritalStatus:Int=0,
    var Picture:String=""
)
