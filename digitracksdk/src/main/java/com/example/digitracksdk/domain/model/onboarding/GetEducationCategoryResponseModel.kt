package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep

@Keep
data class GetEducationCategoryResponseModel(
    val lstCategory: ArrayList<LstCategory>
)

@Keep
data class LstCategory(
    val CategoryName: String,
    val EducationCategoryID: String
)