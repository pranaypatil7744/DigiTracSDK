package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class POBInsertWorkExpModel(
    val CompanyName: String,
    val CurrentlyEmployed: String,
    val DateOfJoining: String,
    val Designation: String,
    val InnovID: String,
    val LastCTC: String,
    val LastWorkingDate: String,
    val TotalExpInYear: String,
    val TotalExpMonth: String,
    val isFresher: String,
    val totalRelevantExpMonth: String,
    val totalRelevantExpYear: String
)

@Keep
data class POBInsertWorkExpResponseModel(
    val status: String,
    val Message: String,
    val OTP: String,
    val TokenID: String,
    val InnovID: String
)