package com.example.digitracksdk.domain.model.onboarding.documents

import androidx.annotation.Keep

@Keep
data class InsertPendingDocumentsRequestModel(
    val InnovID: String="",
    val ImageArr: String="",
    val DocID: String="",
    val Doctype: String="",
    val Extn: String=""
)

@Keep
data class InsertPendingDocumentsResponseModel(
    val CandiHasDoc: String?="",
    val DocID: String?="",
    val DocPath: String?="",
    val Doctype: String?="",
    val Extn: String?="",
    val FilePath: String?="",
    val ImageArr: String?="",
    val InnovID: String?="",
    val Status: String?="",
    val Message:String? =""
)