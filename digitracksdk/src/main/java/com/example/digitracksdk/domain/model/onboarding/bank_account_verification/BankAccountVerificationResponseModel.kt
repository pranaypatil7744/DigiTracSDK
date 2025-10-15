package com.example.digitracksdk.domain.model.onboarding.bank_account_verification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BankAccountVerificationResponseModel(
    val Message: String?="",
    val Status: String?="",
    @SerializedName("data")
    val Details: Details?= Details(),
//    val id: String?="",
    val request_timestamp: String?="",
//    val response_code: String?="",
//    val response_message: String?="",
//    val response_timestamp: String?="",
//    val transaction_status: String?=""
)
@Keep
data class Details(
    val BeneName: String?="",
    val BankRef: String?="",
    val Remark: String?="",
    val Status: String?="",
    //Extra Field For Transaction Date
    var TransactionTime: String?="",
    )