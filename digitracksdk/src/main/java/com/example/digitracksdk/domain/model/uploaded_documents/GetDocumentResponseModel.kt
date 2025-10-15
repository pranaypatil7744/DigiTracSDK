package com.example.digitracksdk.domain.model.uploaded_documents

import androidx.annotation.Keep

@Keep
data class GetDocumentResponseModel(
    var FilePath:String? = "",
    var Ext:String? = "",
    var File:String? = "",
    var Link:String? = "",
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class GetDocumentRequestModel(
    var CandidateDocumentMappingID:String =""
)
