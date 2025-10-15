package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model

import androidx.annotation.Keep

@Keep
data class ReferralProfileDetailsModel(
    var name:String? = "",
    var designation:String? = "",
    var department :String? = "",
    var profilePic:Int? = null,
    var icon1:Int? = null,
    var icon2:Int? = null,
    var title:String? = "",
    var subTitle:String? = "",
    var title2:String? = "",
    var subTitle2:String? = "",
    var refDetailsType: ProfileDetailsType,
    var profileMoreDetailsModel: ArrayList<ProfileMoreDetailsModel>? = ArrayList()
)
@Keep
data class ProfileMoreDetailsModel(
    var icon: Int?=null,
    var compName:String? = "",
    var designation:String? = "",
    var jobType:String? = "",
    var timePeriod:String? = "",
    var totalYear:String? = "",
    var location:String? = "",
    var clgName:String? = "",
    var educationName:String? = ""
)
enum class ProfileDetailsType(val value:Int){
    PROFILE(1),
    PROFILE_DETAILS(2),
    DIVIDER(3),
    TITLE(4),
    WORK_EXPERIENCE(5),
    EDUCATION_DETAILS(6)
}
