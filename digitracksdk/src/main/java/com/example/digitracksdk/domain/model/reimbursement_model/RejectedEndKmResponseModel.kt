package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class RejectedEndKmResponseModel(
    var StartReading:Int? =0,
    var EndReading:Int? =0,
    var Status:String? ="",
    var Message:String? ="",
)

@Keep
data class RejectedEndKmRequestModel(
    var GNETAssociateID:String = "",
    var ReimbursementDetailId:String = ""
)
