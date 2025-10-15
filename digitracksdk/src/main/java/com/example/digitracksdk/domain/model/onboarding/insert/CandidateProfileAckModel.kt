package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class CandidateProfileAckModel(
    val ImageDocArray: String,
    val InnovId: String,
    val Source: String,
    val status: String
)

@Keep
data class CandidateProfileAckResponseModel(
    val status: String,
    val Message: String,
)
