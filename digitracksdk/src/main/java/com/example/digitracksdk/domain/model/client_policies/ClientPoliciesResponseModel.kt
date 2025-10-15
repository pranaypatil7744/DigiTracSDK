package com.example.digitracksdk.domain.model.client_policies

import androidx.annotation.Keep

@Keep
data class ClientPoliciesResponseModel(
    //TODO Update model
    var lstClientPolicyDetails:ArrayList<ListClientPolicyDetailsModel> = ArrayList(),
    var status:String? = "",
    var Message:String? = "",
)

@Keep
data class ListClientPolicyDetailsModel(
    var InnovId:String? ="",
    var ClientName:String? ="",
    var ClientPolicyID:Int? =0,
    var ClientPolicyType:String? ="",
    var ClientPolicyName:String? ="",
    var ClientPolicyFileName:String? ="",
    var ClientPolicyFilePath:String? ="",
    var CreatedDate:String? ="",
    var AcknowledgementStatus:String? ="",
    var AcknowledgementOn:String? ="",
)

@Keep
data class ClientPoliciesRequestModel(
    var InnovID:String = ""
)