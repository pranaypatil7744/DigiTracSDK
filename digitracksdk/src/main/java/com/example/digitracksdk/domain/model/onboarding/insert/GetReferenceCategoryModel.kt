package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

class GetReferenceCategoryModel : ArrayList<GetReferenceCategoryModelItem>()

@Keep
data class GetReferenceCategoryModelItem(
    val ReferenceCategoryID: Int,
    val ReferenceCategoryName: String
)