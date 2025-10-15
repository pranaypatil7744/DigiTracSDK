package com.example.digitracksdk.domain.model.client_policies

import androidx.annotation.Keep

@Keep
data class PolicyAcknowledgeResponseModel(
    var status: String? = "",
    var Message: String? = "",
    var PolicyAcknowledgeStatus: Int? = 0
)

@Keep
data class PolicyAcknowledgeRequestModel(
    var InnovId: String? = "",
    var GnetAssociateId: String? = ""

)

