package com.example.digitracksdk.domain.model.pay_slip

import androidx.annotation.Keep

@Keep
data class PaySlipDownloadResponseModelNew(
    var Payslip:String? = "",
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class PaySlipDownloadRequestModelNew(
    var GNETAssociateID:String? ="",
    var Month:String? ="",
    var Year:String? ="",
)
