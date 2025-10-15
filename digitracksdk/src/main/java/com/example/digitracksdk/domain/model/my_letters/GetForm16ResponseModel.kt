package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class GetForm16ResponseModel(
    var ImageArr: String? = "",
    var Status: String? = "",
    var Message: String? = ""
)

@Keep
data class GetForm16RequestModel(
    var GNETAssociateID:String = "",
    var FinancialYear: String = ""
)
