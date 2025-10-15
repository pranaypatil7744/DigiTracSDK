package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class ReimbursementValidationResponseModel(
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class ReimbursementValidationRequestModel(
    var Amount:Double =0.0,
    var BillDate:String ="",
    var BillNo:String ="",
    var ExpenseTypeId:String ="",
    var GNETAssociateID:String ="",
    var GrossAmount:Double =0.0,
)
