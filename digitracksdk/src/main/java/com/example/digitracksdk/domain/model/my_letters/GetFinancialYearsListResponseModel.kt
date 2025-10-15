package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class GetFinancialYearsListResponseModel(
    val FYlist: ArrayList<FyListModel>? = ArrayList(),
    var Status: String? = "",
    var Message: String? = "",
)

@Keep
data class FyListModel(
    var FinancialYearID: Int? = 0,
    var FinancialYearName: String? = "",
)

@Keep
data class GetFinancialYearsListRequestModel(
    var UserId: String = ""
)
