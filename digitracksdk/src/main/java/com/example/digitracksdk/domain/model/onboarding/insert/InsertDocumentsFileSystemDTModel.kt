package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class InsertDocumentsFileSystemDTModel(
    val InnovID: String,
    val ImageArr: String,
    val DocID: String,
    val Doctype: String,
    val Extn: String
)

@Keep
data class InsertDocumentsFileSystemDTResponse(
    val CandiHasDoc: Any,
    val DocID: Int,
    val DocPath: Any,
    val Doctype: Any,
    val Extn: Any,
    val FilePath: Any,
    val ImageArr: String,
    val InnovID: Int,
    val Status: String
)