package com.example.digitracksdk.domain.model.profile_model

import androidx.annotation.Keep

@Keep
data class ValidCandidateResponseModel(
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class ValidCandidateRequestModel(
    var DateOfBirth:String=""
)
