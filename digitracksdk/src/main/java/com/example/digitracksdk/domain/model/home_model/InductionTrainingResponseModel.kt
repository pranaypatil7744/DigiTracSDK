package com.example.digitracksdk.domain.model.home_model


import androidx.annotation.Keep

@Keep
data class InductionTrainingResponseModel(
    var Content: String? = "",
    var Message: String? = "",
    var Status: String? = "",
    var URL: String? = ""
)

/*
@Keep
data class InductionTrainingRequestModel(
    var InnovId: String? = ""
)*/
