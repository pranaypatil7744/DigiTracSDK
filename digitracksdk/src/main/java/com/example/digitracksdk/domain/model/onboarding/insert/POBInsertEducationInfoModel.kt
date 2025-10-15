package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class InsertEducationInfoRequestModel(


    var BoardName: String ="",
    var EducationCategoryID: String ="",
    var EducationCategoryName  : String ="",
    var InnovID: String ="",
    var InstituteName: String ="",
    var PassYear: String ="",
    var Percentage: String ="",
    var QualificationType: String=""
)

@Keep
data class InsertEducationInfoResponseModel(
    val status: String?="",
    val Message: String?="",
    val OTP: String?="",
    val TokenID: String?="",
    val InnovID: String?=""
)

