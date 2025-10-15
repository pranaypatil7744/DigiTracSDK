package com.example.digitracksdk.domain.model.onboarding.pan_verification

import androidx.annotation.Keep

@Keep
data class PanCardVerificationRequestModel(
    var InnovId: String,
    var PANNumber: String,
    val Source: String="Digitrac"
)