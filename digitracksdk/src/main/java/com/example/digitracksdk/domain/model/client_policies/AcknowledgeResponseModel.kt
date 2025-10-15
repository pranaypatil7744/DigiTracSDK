package com.example.digitracksdk.domain.model.client_policies

import androidx.annotation.Keep

@Keep
data class AcknowledgeResponseModel(
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class AcknowledgeRequestModel(
    var ClientId:String = "",
    var GNETAssociateId:String = "",
    var PolicyId:Int = 0,
)
