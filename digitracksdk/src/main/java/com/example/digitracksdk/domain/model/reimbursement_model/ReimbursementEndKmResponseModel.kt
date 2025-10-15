package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class ReimbursementEndKmResponseModel(
    var EndKM:String? = "",
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class ReimbursementEndKmRequestModel(
    var GNETAssociateId:String = ""
)