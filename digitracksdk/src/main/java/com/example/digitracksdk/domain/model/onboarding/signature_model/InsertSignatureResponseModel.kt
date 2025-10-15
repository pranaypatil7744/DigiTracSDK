package com.example.digitracksdk.domain.model.onboarding.signature_model

import androidx.annotation.Keep

@Keep
data class InsertSignatureResponseModel(
    var status:String? = "",
    var Message:String? = ""
)
@Keep
data class InsertSignatureRequestModel(
    var ImageDocArray:String = "",
    var InnovId:String = "",
    var Source:String = "",
    var status:String = "",
)
