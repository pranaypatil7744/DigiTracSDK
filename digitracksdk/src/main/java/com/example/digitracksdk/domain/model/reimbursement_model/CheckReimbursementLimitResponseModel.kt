package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class CheckReimbursementLimitResponseModel(
    var Message:String? ="",
    var ToalAllocatedAmount:String? ="",
    var AmountUsed:String? ="",
    var Limit:Double? =0.0,
    var AllocationType:String? ="",
    var status:String? ="",
)

@Keep
data class CheckReimbursementLimitRequestModel(
    var Amount:Double = 0.0,
    var Date:String ="",
    var ExpenseType:Int =0,
    var GNETAssociateId:String ="",
)

