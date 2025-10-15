package com.example.digitracksdk.domain.model.onboarding.kyc

import androidx.annotation.Keep

@Keep
data class KycDocumentListResponseModel(
    val lstDoc: ArrayList<ListDocModel>? = ArrayList()
)

@Keep
data class ListDocModel(
    val AssociateID:String?="",
    val DocID: Int?=0,
    val DocName: String?="",
    val FilePath: String?="",
    val GnetAssociateID: String?="",
    val IsSubmitted: String?="",
    val Picture: String?="",
    val Status: String?=""
)