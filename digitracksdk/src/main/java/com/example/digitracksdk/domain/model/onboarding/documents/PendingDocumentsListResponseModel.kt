package com.example.digitracksdk.domain.model.onboarding.documents

import androidx.annotation.Keep


@Keep
data class PendingDocumentsListRequestModel(
    var InnovID: String,
    var DocsPendingforUpload: String
)

@Keep
data class PendingDocumentsListResponseModel(
    var InnovID: String ? = "",
    var Message: String? ="",
    var Status: String?="",
    var lstDocument:ArrayList<ListDocumentModel>? = ArrayList(),
)

@Keep
data class ListDocumentModel(
    var DocID: Int=0,
    var Doctype: String?=""
)

