package com.example.digitracksdk.domain.model.new_reimbursement

import androidx.annotation.Keep

@Keep
data class GetMonthDetailsResponseModel(
    val LstMonthDetails: ArrayList<LstMonthDetail>? = ArrayList(),
    val Message: String?="",
    val Status: String?=""
)
@Keep
data class LstMonthDetail(
    val MonthId: Int?=0,
    val Month_Name: String?=""
)