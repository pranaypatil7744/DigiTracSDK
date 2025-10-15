package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class CheckReimbursementLimitV1ResponseModel(
    var Message:String? = "",
    var ToalAllocatedAmount:String? = "",
    var AmountUsed:String? = "",
    var Limit:Int? = 0,
    var AllocationType:String? = "",
    var status:String? = "",
)

@Keep
data class CheckReimbursementLimitV1RequestModel(
    var Amount:Double = 0.0,
    var Date:String = "",
    var ExpenseType:Int = 0,
    var GNETAssociateId:String = "",
    var ToDate:String = "",
    var FromDate:String = ""
)
