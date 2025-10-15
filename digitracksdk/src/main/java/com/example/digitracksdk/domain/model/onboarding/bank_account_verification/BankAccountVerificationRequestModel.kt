package com.example.digitracksdk.domain.model.onboarding.bank_account_verification

import androidx.annotation.Keep

@Keep
data class BankAccountVerificationRequestModel(
    val Account: String,
    val IFSC: String,
    val InnovId: String,
    val Source: String="Digitrac"
)