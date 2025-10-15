package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class BalanceLeaveStatusResponseModel(
    var AssociateLeaveId:String? = "",
    var AssociateId:String? = "",
    var PL:String? = "",
    var CL:String? = "",
    var SL:String? = "",
    var PLText:String? ="",
    var CLText:String? ="",
    var SLText:String? ="",
    var Month:String? = "",
    var Year:String? = "",
    var CreatedDate:String? = "",
    var CreatedBy:String? = "",
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class BalanceLeaveStatusRequestModel(
    var AssociateID:String = "",
    var InnovId:String = ""
)
