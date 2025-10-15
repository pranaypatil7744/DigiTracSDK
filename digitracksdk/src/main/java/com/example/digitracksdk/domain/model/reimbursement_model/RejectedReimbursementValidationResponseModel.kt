package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class RejectedReimbursementValidationResponseModel(
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class RejectedReimbursementValidationRequestModel(
    var Amount:Double= 0.0,
    var AssociateReimbursementDetailId:String ="",
    var BillDate:String ="",
    var BillNo:String ="",
    var ExpenseTypeId:Int = 0,
    var GNETAssociateID:String="",
    var GrossAmount:Double=0.0
)
