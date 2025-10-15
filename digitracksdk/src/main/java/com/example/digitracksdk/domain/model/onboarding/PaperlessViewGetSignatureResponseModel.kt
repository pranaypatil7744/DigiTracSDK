package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep

@Keep
data class PaperlessViewGetSignatureResponseModel(
    val Message: String,
    val Signature: String,
    val Status: String
)