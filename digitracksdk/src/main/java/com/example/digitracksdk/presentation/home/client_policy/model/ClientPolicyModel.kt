package com.example.digitracksdk.presentation.home.client_policy.model

import androidx.annotation.Keep

@Keep
data class ClientPolicyModel(
    var policyType:String? = "",
    var policyName:String? = "",
    var date:String? = "",
    var policyStatusType: PolicyStatusType,
    var policyUrl:String? = null,
    var policyId:Int? = 0
)

enum class PolicyStatusType(val value:Int){
    ACKNOWLEDGED(1),
    PENDING(2)
}
