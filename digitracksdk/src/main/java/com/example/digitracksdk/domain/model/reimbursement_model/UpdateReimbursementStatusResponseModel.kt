package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class UpdateReimbursementStatusResponseModel(
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class UpdateReimbursementStatusRequestModel(
    var AssociateReimbursementId:String =""
)
