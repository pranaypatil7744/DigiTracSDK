package com.example.digitracksdk.domain.model.onboarding.aadhaar_verification

import androidx.annotation.Keep

@Keep
data class GetAadhaarVerificationDetailsResponseModel(
    val InnovId: String?="",
    val Message: String?="",
    val UserFullName: String?="",
    val status: String?=""
)