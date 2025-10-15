package com.example.digitracksdk.domain.model.onboarding.aadhaar_verification

import androidx.annotation.Keep

@Keep
data class ValidateAadhaarResponseModel(
    val Message: String?="",
    val status: String?=""
)
@Keep
data class ValidateAadhaarRequestModel(
    var AadharNo: String,
    var InnovID: String
)