package com.example.digitracksdk.domain.model.refer_a_friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReferralFriendResponseModel(
    var status:String? = "",
    var Message:String? = "",
    var OTP:String? = "",
    var Mobile:String? = ""
)

@Keep
data class ReferralFriendRequestModel(
    var AdharCard:String? = "",
    var ClientrequirementID:String? = "",
    var EmailID:String? = "",
    @SerializedName("FacilityId")
    var BranchId:Int = 0,
    var FirstName:String? = "",
    var Gender:String? = "",
    var Location:String? = "",
    var MobileNo:String? = "",
    var RefferedByInnovId:String? = "",
    var Skill:String? = "",
    var SkillId:Int = 0,
)
