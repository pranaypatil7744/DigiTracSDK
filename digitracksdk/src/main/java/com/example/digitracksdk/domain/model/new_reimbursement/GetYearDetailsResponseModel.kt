package com.example.digitracksdk.domain.model.new_reimbursement

import androidx.annotation.Keep

@Keep
data class GetYearDetailsResponseModel(
    val LstYearDetails: ArrayList<LstYearDetail>? = ArrayList(),
    val Message: String? = "",
    val Status: String? = ""
)

@Keep
data class LstYearDetail(
    val YearDesc: String? = "",
    val YearId: Int? = 0
)

@Keep
data class GetMonthYearDetailsRequestModel(
    var Associates: String,
    var InnovID: Int,
    val Module: String = "NewReimbursement"
)