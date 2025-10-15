package com.example.digitracksdk.domain.model.home_model

import androidx.annotation.Keep

@Keep
data class SurveyLinkResponseModel(
    var SurveyLink: String? = "",
    var status: String? = "",
    var Message: String? = ""
)

@Keep
data class SurveyLinkRequestModel(
    var AssociateId: String? = "",
    var InnovID: String? = "",
    var SurveyType: String? = "",
    var Category: String? = "",
)
