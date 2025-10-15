package com.example.digitracksdk.domain.model.client_policies

import io.realm.internal.Keep

@Keep
data class ViewAckPolicyResponseModel(
    var FilePath : String?="",
    var Message : String? = "",
    var Status : String ?=""
)

@Keep
data class ViewAckPolicyRequestModel(
    var GnetassociateId: String? = "",
    var ClientId: String? = "",
    var PolicyId: String? = ""
)

