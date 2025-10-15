package com.example.digitracksdk.domain.model.onboarding.documents

import androidx.annotation.Keep

@Keep
data class ViewDocumentsResponseModel(
    var lstDoc:ArrayList<ListDocModel>?= ArrayList(),
    var Status:String?= "",
    var Message:String?= "",
)

@Keep
data class ListDocModel(
    var AssociateID: String?="",
    var DocID: String?="",
    var DocName: String?="",
    var FilePath: String?="",
    var GnetAssociateID: String?="",
    var IsSubmitted: String?="",
    var Picture: String?="",
    var Status: String?=""
)


