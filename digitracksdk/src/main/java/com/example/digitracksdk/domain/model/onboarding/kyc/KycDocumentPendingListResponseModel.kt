package com.example.digitracksdk.domain.model.onboarding.kyc

import androidx.annotation.Keep


@Keep
data class KycDocumentPendingListRequestModel(
    val InnovID: String="",
    val DocsPendingforUpload: String=""
)

@Keep
data class KycDocumentPendingListResponseModel(
    val InnovID: String ="",
    val Message: String="",
    val Status: String="",
    var lstDocument: ArrayList<ListDocumentModel>?= ArrayList()
)

@Keep
data class ListDocumentModel(
    val DocID: Int?=0,
    val Doctype: String?=""
)

