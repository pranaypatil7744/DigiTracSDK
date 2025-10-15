package com.example.digitracksdk.domain.model.onboarding.cibil_score

import androidx.annotation.Keep

@Keep
data class GetCibilScoreResponseModel(
    val Status:String?="",
    val Message:String?="",
    val CibilScore:String?="",
)
@Keep
data class GetCibilScoreRequestModel(
    var InnovId: String?="",
    var PanCard: String,
    val Source: String="Digitrac"
)
