package com.example.digitracksdk.domain.model.pay_slip

import androidx.annotation.Keep

@Keep
data class PaySlipDownloadResponseModel(
    var Empcode:String = "",
    var Year:String = "",
    var Month:String = "",
    var FileData:String = "",
    var Status:String = "",
    var Error:String = "",
)

@Keep
data class PaySlipDownloadRequestModel(
    var InnovID:String = "",
    var Month:String = "",
    var Year:String = "",
)
