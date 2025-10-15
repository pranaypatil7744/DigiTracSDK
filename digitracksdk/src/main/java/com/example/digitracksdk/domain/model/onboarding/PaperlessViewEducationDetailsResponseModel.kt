package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class PaperlessViewEducationDetailsResponseModel(
    val status: String? = "",
    val Message: String? = "",
    var lstEducationDetails: ArrayList<ListEducationDetails> = ArrayList()
)

@Keep
data class ListEducationDetails(
    val EducationCategoryID: String? = "",
    val InnovID: String? = "",
    val BoardName: String? = "",
    val InstituteName: String? = "",
    val Percentage: String? = "",
    val PassYear: String? = "",
    val QualificationType: String? = "",
    val MappingID: String? = "",
    val CategoryName: String? = ""
) : Serializable

