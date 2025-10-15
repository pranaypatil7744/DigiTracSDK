package com.example.digitracksdk.domain.model.pay_slip

import androidx.annotation.Keep

@Keep
data class SalaryReleaseStatusResponseModel(
    var GNETAssociateID :String? ="",
    var status :String? ="",
    var Message :String? ="",
)

@Keep
data class SalaryReleaseStatusRequestModel(
    var GNETAssociateID:String = "",
    var Month:String = "",
    var Year:String = ""
)
