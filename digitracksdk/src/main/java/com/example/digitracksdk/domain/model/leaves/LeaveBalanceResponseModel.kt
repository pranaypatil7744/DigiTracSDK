package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class LeaveBalanceResponseModel(
    var PL:String? ="",
    var CL:String? ="",
    var SL:String? ="",
    var PLText:String? ="",
    var CLText:String? ="",
    var SLText:String? ="",
    var status:String? ="",
    var Message:String? ="",
)

@Keep
data class LeaveBalanceRequestModel(
    var GNETAssociateID:String? = "",
    var InnovID:String? = "",
    var Month:Int? = 0,
    var Year:Int? = 0
)
