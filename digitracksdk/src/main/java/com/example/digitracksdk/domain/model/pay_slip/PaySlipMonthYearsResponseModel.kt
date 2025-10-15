package com.example.digitracksdk.domain.model.pay_slip

import androidx.annotation.Keep

@Keep
data class PaySlipMonthYearsResponseModel(
    var InnovID:String? = "",
    var lstYearMonthName:ArrayList<ListYearMonthName>? =ArrayList(),
    var Status:String? =""
)

@Keep
data class ListYearMonthName(
    var Year:String? = "",
    var lstMonthName:ArrayList<String>? = ArrayList()
)

@Keep
data class PaySlipMonthYearRequestModel(
    var InnovID:String = ""
)
