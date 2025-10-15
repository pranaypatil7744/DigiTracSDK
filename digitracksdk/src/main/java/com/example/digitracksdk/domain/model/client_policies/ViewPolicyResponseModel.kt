package com.example.digitracksdk.domain.model.client_policies

import androidx.annotation.Keep

@Keep
data class ViewPolicyResponseModel(
    var ClientPolicyID:String?= "",
    var ClientPolicyURL : String?="",
    var status:String?= "",
    var Message : String?="",
//    var ClientPolicyFilePath:String?= "",
//    var ClientPolicyImageArr:String?= ""

)

@Keep
data class ViewPolicyRequestModel(
    var ClientPolicyID:Int=0
)