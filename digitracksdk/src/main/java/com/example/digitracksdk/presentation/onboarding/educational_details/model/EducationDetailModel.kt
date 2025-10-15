package com.example.digitracksdk.presentation.onboarding.educational_details.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class EducationDetailModel(
    var qualitifcation: String? = "",
    var university: String? = "",
    var monthYear: String? = "",
    var percentage: String? = ""
):Serializable


@Keep
data class AddEducationDetailModel(
    var title: String? = "",
    var value: String? = ""
)