package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class DownloadCandidateOfferLetterResponseModel(
    var Message:String? = "",
    var OfferLetterImage:String? = "",
    var Status:String? = "",
)

@Keep
data class DownloadCandidateOfferLetterRequestModel(
    val CandidateType:String? = "",
    val InnovID:String? = "",
    val OfferId:String? = "",
    val OfferPatternId:String? = "",
)
