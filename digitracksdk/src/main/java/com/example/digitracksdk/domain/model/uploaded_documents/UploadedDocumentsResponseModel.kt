package com.example.digitracksdk.domain.model.uploaded_documents

import androidx.annotation.Keep

@Keep
data class UploadedDocumentsResponseModel(
    var LstCandidateDoc:ArrayList<CandidateDocListModel> = ArrayList(),
    var Status:String? = "",
    var Message:String? =""
)

@Keep
data class CandidateDocListModel(
    var DocName:String? = "",
    var UploadDate:String?= "",
    var CandidateDocumentMappingID:String?= "",
    var DocID:String?= "",
    var ModifiedDate:String?= ""
)

@Keep
data class UploadedDocumentsRequestModel(
    var InnovID:String? ="",
    var DocType:String? =""
)
